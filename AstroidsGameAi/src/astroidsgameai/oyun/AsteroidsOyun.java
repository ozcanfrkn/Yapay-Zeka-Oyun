package astroidsgameai.oyun;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import javax.swing.Timer;

public class AsteroidsOyun implements ActionListener{
    
    //Oyun sabitleri
    public final int Oyun_Genislik = 600; //Sabit genislik 
    public final int Oyun_Yukseklik = 600;//Sabit yukseklik
    public final int Fps = 144; // saniyede  yenilenme
    public final int Canlar = 2;//Geminin yeniden canlanma haklari.
    public final int BaslangicAsteroidSayisi = 3;
    public final int AsteroitBaslangicBoyut = 100; // asteroitlerin baþlangýç boyutu piksel cinsinden
    public final int KucukAsteroitSkor = 100;
    public final int OrtaAsteroitSkor = 50;
    public final int BuyukAsteroitSkor = 20;
    
    
    public boolean oyunBitti;
    
    public Gemi gemi;
    public ArrayList<Asteroit> asteroitler;
    public int seviye;
    public int skor;
    public Timer oyunTimer;
    
    public AsteroidsOyun() {
        YeniOyun();
    }
    public void YeniOyun(){
        gemi = new Gemi(Oyun_Genislik,Oyun_Yukseklik,Canlar,Fps);
        asteroitler = new ArrayList<>();
        seviye = 0;
        skor = 0;
        oyunBitti = false;
        asteroitleriOlustur(BaslangicAsteroidSayisi);
        oyunTimer = new Timer(1000 / Fps, this);
        oyunTimer.start();
    }
    
    public void asteroitleriOlustur(int sayi) {//Her yeni seviye basýnda asteroidleri olustur
        for (int i = 0; i < sayi; i++) {
            asteroitler.add(new Asteroit(gemi.getX(), gemi.getY(), Oyun_Genislik, Oyun_Yukseklik, Fps));
        }
        
    }
    
    public void yeniSeviye(){//Eðer tüm asteroitler yok edildiyse yeni seviyeye gec
        seviye += 1;
        asteroitleriOlustur(BaslangicAsteroidSayisi+seviye);
        System.out.println(System.identityHashCode(this) + " Seviye:"+seviye+" AsteroitSayýsý:"+ asteroitler.size()+" Skor:"+skor);
    }
    
    public void asteroitleriIlerlet() {//Zamanla asteroitler ilerler
        for (Asteroit asteroit : asteroitler) {
            asteroit.x += asteroit.getxIvme();
            asteroit.y += asteroit.getyIvme();
            
            if (asteroit.x < 0 - asteroit.r) {
                asteroit.x = Oyun_Genislik + asteroit.r;
            }else if(asteroit.x > Oyun_Genislik + asteroit.r){
                asteroit.x = 0 - asteroit.r;
            }
            if (asteroit.y < 0 - asteroit.r) {
                asteroit.y = Oyun_Yukseklik + asteroit.r;
            }else if(asteroit.y > Oyun_Yukseklik + asteroit.r){
                asteroit.y = 0 - asteroit.r;
            }
        }
        
    }
    
    public void mermileriIlerlet() {
        ArrayList<Mermi> silineceklerList = new ArrayList<>();
        for (Mermi mermi : gemi.getMermiler()) {
            mermi.ilerlet();
            mermi.ekrandaTut();//ekrandan cýkarlarsa yeniden çiz
            if (mermi.yoluTamamladiMi()) {
                silineceklerList.add(mermi);
            }
        }
        gemi.mermiler.removeAll(silineceklerList);
    }
    
    public double ikiNoktaArasindakiUzaklik(double x1 ,double y1,double x2,double y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    public void astroitYokEt(Asteroit silinecekAsteroit){
        if (silinecekAsteroit.r == AsteroitBaslangicBoyut/2) {
            asteroitler.add(new Asteroit(Oyun_Genislik, Oyun_Yukseklik, Fps,silinecekAsteroit.x, silinecekAsteroit.y, AsteroitBaslangicBoyut/2));
            asteroitler.add(new Asteroit(Oyun_Genislik, Oyun_Yukseklik, Fps,silinecekAsteroit.x, silinecekAsteroit.y, AsteroitBaslangicBoyut/2));  
            skor += BuyukAsteroitSkor;
        }else if(silinecekAsteroit.r == AsteroitBaslangicBoyut/4){
            asteroitler.add(new Asteroit(Oyun_Genislik, Oyun_Yukseklik, Fps,silinecekAsteroit.x, silinecekAsteroit.y, AsteroitBaslangicBoyut/4));
            asteroitler.add(new Asteroit(Oyun_Genislik, Oyun_Yukseklik, Fps,silinecekAsteroit.x, silinecekAsteroit.y, AsteroitBaslangicBoyut/4));
            skor += OrtaAsteroitSkor;
        }else{
            skor += KucukAsteroitSkor;
        }
        asteroitler.remove(silinecekAsteroit);
        if (asteroitler.isEmpty()) {
            yeniSeviye();
        }
    }
    public void astreotlerCarpismaKontrolu() {
        Asteroit silinecekAsteroit =null;
        for (Asteroit asteroit : asteroitler) {
            if (ikiNoktaArasindakiUzaklik(gemi.x, gemi.y, asteroit.x, asteroit.y) < gemi.r + asteroit.r && gemi.olumsuzlukKalanSure <= 0) {
                gemi.setCarpisma(true);//Geminin carpistigini isaretle.
                silinecekAsteroit  =asteroit;
                break;
            }
            Mermi silinecekMermi = null;
            for(Mermi mermi : gemi.getMermiler()){
                if (ikiNoktaArasindakiUzaklik(mermi.x, mermi.y, asteroit.x, asteroit.y) < asteroit.r) {
                    silinecekMermi = mermi;
                    silinecekAsteroit = asteroit;
                    break;
                }
            }
            if (silinecekMermi != null)
                gemi.mermiler.remove(silinecekMermi);  
        }
        if (silinecekAsteroit != null) {
            astroitYokEt(silinecekAsteroit);
        }
        
    }
    
    public void carpismaVarsaYapilacaklar() {
        if (gemi.isCarpisma()) {//carpisma varsa
            int kalanCan = --gemi.canlar;
            gemi = new Gemi(Oyun_Genislik,Oyun_Yukseklik,kalanCan,Fps);
            if (gemi.canlar < 1) {//Oyun biter
                System.out.println(System.identityHashCode(this) + " Oyun bitti"+" Skor:"+skor);
                setOyunBitti(true);
                oyunTimer.stop();
                //Skoru dosyaya yaz
                Date date = new Date();
                DateFormat sdf = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
                PrintWriter writer;
                try {
                    writer = new PrintWriter(new FileOutputStream("skorlar.txt",true));
                    writer.println(sdf.format(date)+" Skor:"+skor);
                    writer.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AsteroidsOyun.class.getName()).log(Level.SEVERE, null, ex);
                }
                YeniOyun();
            }else{
                System.out.println(System.identityHashCode(this) + " Kalan can:" + kalanCan+" Skor:"+skor);
            }
        }
    }
    
    public void oyunGenelKontrolleri(){//Yenileme islemleri oyunun grafik harici motoru
        gemi.aciyiAyarla();
        gemi.ittir();//ittirilmesi gerekiyorsa ittir
        gemi.konumuAyarla();//Geminin konumunu guncelle
        gemi.ekrandaTut();//Ekrandan cýkmasýna izin verme
        gemi.olumsuzlukKontrolu();//Geminin olumsuz oldugu zamaný ayarla
        asteroitleriIlerlet();//Asteroitleri ilerlet
        astreotlerCarpismaKontrolu();
        carpismaVarsaYapilacaklar();
        mermileriIlerlet();
    }
    //Fpsye göre oyun verilerini yeniden isleme
    @Override
    public void actionPerformed(ActionEvent e) {
        oyunGenelKontrolleri();
    }

    public boolean isOyunBitti() {
        return oyunBitti;
    }

    public void setOyunBitti(boolean oyunBitti) {
        this.oyunBitti = oyunBitti;
    } 
}
