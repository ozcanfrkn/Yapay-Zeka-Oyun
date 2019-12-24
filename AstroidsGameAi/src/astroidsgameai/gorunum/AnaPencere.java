package astroidsgameai.gorunum;

import astroidsgameai.yapayzeka.Matris;
import astroidsgameai.yapayzeka.SinirAgi;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class AnaPencere extends JFrame{
    
    private final int OYUN_GENEISLIK     = 700;   
    private final int OYUN_YUKSEKLIK     = 700;
    private final int PENCERE_GENEISLIK  = 600;   
    private final int PENCERE_YUKSEKLIK  = 600;
    
    private final JButton sinirAgiOlustur;
    private final JButton sinirAginiEgit;
    private final JButton yapayZekayiKaydet;
    private final JButton yapayZekaKaydiniDosyadanAl;
    private final JButton oyunuYapayZekaIleBaslat;
    private final JButton oyunBaslat;
    private final JTextField yapayZekaKayit;
    
    SinirAgi sinirAgi;
    final int girisKatmani = 4;
    final int gizlikatmanKatmani = 12;
    final int cikisKatmani = 1;
    final int egitimSayisi = 100000;
    final int asteroitBoyut = 100;
    
    public AnaPencere() {
        setTitle("Astroid Oyunu");                  //basligini ayarlama
        setDefaultCloseOperation(EXIT_ON_CLOSE);    //X butonuna basildiginda program sonlansin.
        setBounds(0, 0, PENCERE_GENEISLIK, PENCERE_YUKSEKLIK); //Pencerenin boyutunu ayarlama
        setLayout( new BorderLayout() ); //kenarlardan 10ar px bosluk olsun
        JPanel satir1 = new JPanel();
        satir1.setLayout(new FlowLayout());
        
        oyunBaslat = new OyunBaslat();
        satir1.add(oyunBaslat);
        
        sinirAgiOlustur = new SinirAgiOlustur();
        satir1.add(sinirAgiOlustur);
        
        sinirAginiEgit = new SinirAginiEgit();
        satir1.add(sinirAginiEgit);
        
        oyunuYapayZekaIleBaslat = new OyunuYapayZekaILeBaslat();
        satir1.add(oyunuYapayZekaIleBaslat);
        
        JPanel satir2 = new JPanel();
        satir2.setLayout(new FlowLayout());
        yapayZekaKayit = new JTextField("YapaySinirAgi.txt");
        yapayZekaKayit.setPreferredSize(new Dimension(150, 25));
        satir2.add(yapayZekaKayit);
        
        yapayZekayiKaydet = new YapayZekayiKaydet();
        satir2.add(yapayZekayiKaydet);
        
        yapayZekaKaydiniDosyadanAl = new YapayZekaKaydiniDosyadanAl();
        satir2.add(yapayZekaKaydiniDosyadanAl);
        
        add(satir1, BorderLayout.CENTER);
        add(satir2, BorderLayout.SOUTH);
        pack();
        setVisible(true); //Pencereyi görünür yapma
    }
    
    class YapayZekaKaydiniDosyadanAl extends JButton{
        public YapayZekaKaydiniDosyadanAl(){
            setText("Yapay zekayý kaydýný dosyadan al");
            setEnabled(true);
            addActionListener((ActionEvent e) -> {
                if (sinirAgi != null) {
                    String dosyaAdi = yapayZekaKayit.getText();
                    if (!dosyaAdi.isEmpty()) {
                        try {
                            File file = new File(dosyaAdi);
                            Scanner sc = new Scanner(file);
                            sc.useLocale(Locale.US);
                            if (!sc.hasNext()) {
                                System.out.println("Dosya bos yada ismi degistirilmis.");
                            }else{
                                //Noron sayýlarýný okuma
                                int girisSayisi = sc.nextInt();
                                int gizlikatmanSayisi = sc.nextInt();
                                int cikisSayisi = sc.nextInt();

                                sinirAgi.setgNoronS(girisSayisi);
                                sinirAgi.setkNoronS(gizlikatmanSayisi);
                                sinirAgi.setcNoronS(cikisSayisi);

                                //giris katman aðýrlýklarý okuma
                                Matris girisAgirlik = new Matris(girisSayisi, gizlikatmanSayisi); //giris katman sayýsý ve gizli katman sayýsý kadar olmalý
                                for (int i = 0; i < girisSayisi; i++) {
                                    for (int j = 0; j < gizlikatmanSayisi; j++) {
                                        girisAgirlik.data[i][j] = sc.nextDouble();
                                    }
                                }
                                sinirAgi.setGirisAgirlik(girisAgirlik);

                                //gizli katman aðýrlýklarý okuma
                                Matris gizlikatmanAgirlik = new Matris(gizlikatmanSayisi, cikisSayisi); //gizlikatman sayisi kadar  satir, cikis kadar sutun olmalý
                                for (int i = 0; i < gizlikatmanSayisi; i++) {
                                    for (int j = 0; j < cikisSayisi; j++) {
                                        gizlikatmanAgirlik.data[i][j] = sc.nextDouble();
                                    }
                                }
                                sinirAgi.setgizlikatmanAgirlik(gizlikatmanAgirlik);
                                //gizli katman bias okuma
                                Matris gizlikatmanBias = new Matris(1, gizlikatmanSayisi); //1 satýr ve gizlikatman kadar sutun
                                for (int j = 0; j < gizlikatmanSayisi; j++) {
                                    gizlikatmanBias.data[0][j] = sc.nextDouble();
                                }
                                sinirAgi.setgizlikatmanBias(gizlikatmanBias);
                                //cýkýþ katman bias okuma
                                Matris cikisBias = new Matris(1, cikisSayisi); //1 satýr ve cikis kadar sutun
                                for (int j = 0; j < cikisSayisi; j++) {
                                    cikisBias.data[0][j] = sc.nextDouble();
                                }
                                sinirAgi.setCikisBias(cikisBias);
                            }

                            sc.close();
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(AnaPencere.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("Sinir aðý "+dosyaAdi+" dosyasýndan alýndý.");
                    }else{
                        System.out.println("Dosya ismi yazmalýsýnýz");
                    }
                    
                }else{
                    System.out.println("Önce bir sinir aðý yapýsý oluþturmalýsýnýz.");
                }
                
            });
            
            
        }
        
    }
    
    class YapayZekayiKaydet extends JButton{
        public YapayZekayiKaydet() {
            setText("Yapay zekayý kaydet");
            setEnabled(true);
            addActionListener((ActionEvent e) -> {
                if (sinirAgi != null) {
                    String dosyaAdi = yapayZekaKayit.getText();
                    if (!dosyaAdi.isEmpty()) {
                        try {
                            PrintWriter writer = new PrintWriter(new FileOutputStream(dosyaAdi,false));
                            writer.println(sinirAgi.gNoronS+" "+sinirAgi.kNoronS+" "+sinirAgi.cNoronS);//Noron sayýlarýný sýrasýyla kaydetme

                            for (double[] dizi : sinirAgi.girisAgirlik.data) {//giris katman aðýrlýklarýný kaydetme
                                String satir = "";
                                for (double d : dizi) {
                                    satir = satir + d +" ";
                                }
                                writer.println(satir);
                            }

                            for (double[] dizi : sinirAgi.gizlikatmanAgirlik.data) {//gizli katman aðýrlýklarýný kaydetme
                                String satir = "";
                                for (double d : dizi) {
                                    satir = satir + d +" ";
                                }
                                writer.println(satir);
                            }

                            for (double[] dizi : sinirAgi.gizlikatmanBias.data) {//gizli katman bias kaydetme
                                String satir = "";
                                for (double d : dizi) {
                                    satir = satir + d +" ";
                                }
                                writer.println(satir);
                            }

                            for (double[] dizi : sinirAgi.cikisBias.data) {//cikis katman bias kaydetme
                                String satir = "";
                                for (double d : dizi) {
                                    satir = satir + d +" ";
                                }
                                writer.println(satir);
                            }

                            writer.close();
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(AnaPencere.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        System.out.println("Sinir aðý "+dosyaAdi+" dosyasýna kayýt edildi.");
                    }else{
                        System.out.println("Dosya ismi yazmalýsýnýz");
                    }
                }else{
                    System.out.println("Önce bir sinir aðý yapýsý oluþturmalýsýnýz.");
                }
                
            });
        }
        
        
    }
    
    class OyunuYapayZekaILeBaslat extends JButton {
        public OyunuYapayZekaILeBaslat() {
            setText("Oyunu yapay zeka ile baþlat");
            setEnabled(true);
            addActionListener((ActionEvent e) -> {
                if (sinirAgi != null) {
                    SwingUtilities.invokeLater(() -> {
                        OyunPencere oyunPencere = new OyunPencere(sinirAgi);   //oyun pencereyi olusturma
                        oyunPencere.setLocationRelativeTo(null);      //Ekranda ortalama
                    });
                    
                }else{
                    System.out.println("Once bir sinir aðý yapýsý oluþturmalýsýn.");
                }
                
            });
        }
    }
    
    class SinirAginiEgit extends JButton{
        public SinirAginiEgit() {
            setText("Sinir aðýný eðit");
            setEnabled(true);
            addActionListener((ActionEvent e) -> {
                if (sinirAgi != null) {
                    double ax, ay, ga, gx, gy;
                    for (int i = 0; i < egitimSayisi; i++) {
                        // rasgele asteroit yeri (ekran dýþý verileri dahil et)
                        ax = Math.random() * (OYUN_GENEISLIK + asteroitBoyut - asteroitBoyut / 2);
                        ay = Math.random() * (OYUN_YUKSEKLIK + asteroitBoyut - asteroitBoyut / 2);
                        
                        // geminin açýsý ve konumu
                        ga = Math.random() * 360;
                        gx = OYUN_GENEISLIK / 2;
                        gy = OYUN_YUKSEKLIK / 2;
                        
                        double aci = acinoktosý(gx, gy, ga, ax, ay);//açýyý hesapla
                        
                        // dönüþ yönünü belirlemek
                        double yön = aci > 180 ? 0 : 1;
                        
                        sinirAgi.egitim(normalise(ax, ay, aci, ga), new double[]{yön});
                    }
                    System.out.println("Sinir aðý eðitildi.");
                }else{
                    System.out.println("Sinir aðý mevcut deðil önce yeni sini aðý oluþtur.");
                }
                
            });
            
        }
        
    }
    
    class SinirAgiOlustur extends JButton{
        public SinirAgiOlustur() {
            setText("Sinir Aðý Oluþtur");
            setEnabled(true);
            addActionListener((ActionEvent e) -> {
                if (sinirAgi == null) {
                    sinirAgi = new SinirAgi(girisKatmani, gizlikatmanKatmani, cikisKatmani);
                    System.out.println("Sinir aðý oluþturuldu.");
                }else{
                    System.out.println("Sinir agi zaten olusturulmuþ.");
                }
                
            });
        }
    }
    
    class OyunBaslat extends JButton {
        public OyunBaslat() {
            setText("Oyunu Baþlat");
            setEnabled(true);
            addActionListener((ActionEvent e) -> {
                SwingUtilities.invokeLater(() -> {
                    OyunPencere oyunPencere = new OyunPencere();   //oyun pencereyi olusturma
                    oyunPencere.setLocationRelativeTo(null);      //Ekranda ortalama
                });
            });
        }
    }
    
    double acinoktosý(double x,double y,double mevcutaci,double targetX,double targetY) {
        double hedefaci = Math.atan2(-targetY + y, targetX - x) * 180 / Math.PI; //Arctan ile açýyý bulma
        double fark = mevcutaci - hedefaci;//mevcut açý ile hedef açý arasýndaki fark
        return (fark+360) % 360;//açý 0 ile 360 arasýnda
    }
    
    double[] normalise(double roidX,double roidY,double roidA,double shipA) {
        // deðerleri 0 ile 1 arasýnda normalleþtirme
        double[] input = new double[4];
        input[0] = roidX / OYUN_GENEISLIK;
        input[1] = roidY / OYUN_YUKSEKLIK;
        input[2] = roidA / 360;
        input[3] = shipA / 360;
        return input;
    }
}
