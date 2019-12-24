/*
 * Oyun �izimlerini i�eren class. Asteroids i�erisinde olusturulan nesneleri �izer.
 */
package astroidsgameai.gorunum;


import astroidsgameai.oyun.Asteroit;
import astroidsgameai.oyun.Mermi;
import astroidsgameai.yapayzeka.SinirAgi;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JPanel;

public class AsteroidsOyunGorunum extends astroidsgameai.oyun.AsteroidsOyun {
    
    JPanel oyunJPanel;
    Boolean cemberleriGoster = false; // Kesisim cemberleri gosterip gizle
    BasicStroke gemiVeAsteroitStroke;
    BasicStroke gemiAlevStroke;
    Font skorFont;
    
    SinirAgi sinirAgi;
    boolean ysaAktif;
    final double cikisborusu = 0.1; // Tahminin bir d�n��e ne kadar yak�n olmas� gerekti�i
    final int SaniyedekiAtesSayi = 10; // Saniyedeki ate� say�s�
    double ysaAtesZamani;
    
    public AsteroidsOyunGorunum() {
        this.ysaAktif = false;
        this.oyunJPanel = new GorunumJpanel();
        this.gemiVeAsteroitStroke = new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        this.gemiAlevStroke = new BasicStroke(3,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
        this.skorFont = new Font("Arial", Font.PLAIN, 22);
    }
    
    public AsteroidsOyunGorunum(SinirAgi sinirAgi){//Oyunun oyomatik oynas�n
        this.ysaAktif = true;
        this.oyunJPanel = new GorunumJpanel();
        this.gemiVeAsteroitStroke = new BasicStroke(2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
        this.gemiAlevStroke = new BasicStroke(3,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER);
        this.skorFont = new Font("Arial", Font.PLAIN, 22);
        this.sinirAgi = sinirAgi;
        this.ysaAtesZamani = 0;
    }
    
    double acinoktas�(double x,double y,double mevcutaci,double targetX,double targetY) {
        double hedefaci = Math.atan2(-targetY + y, targetX - x) * 180 / Math.PI; //Arctan ile a��y� bul
        double fark = mevcutaci - hedefaci;//mevcut a�� ile hedef a�� aras�ndaki fark
        return (fark+360) % 360;//a�� 0 ile 360 aras�nda
    }
    
    double[] normalise(double roidX,double roidY,double roidA,double shipA) {
        // de�erleri 0 ile 1 aras�nda normalle�tirme
        double[] input = new double[4];
        input[0] = roidX / Oyun_Genislik;
        input[1] = roidY / Oyun_Yukseklik;
        input[2] = roidA / 360;
        input[3] = shipA / 360;
        return input;
    }
    
    public void yapayZekaHamleler(){
        Asteroit enYakinAsteroit = asteroitler.get(0);
        double min = 9999;
        for (Asteroit asteroit : asteroitler) {//En yak�n asteroiti bul
            double uzaklik = ikiNoktaArasindakiUzaklik(gemi.x, gemi.y, asteroit.x, asteroit.y);
            if (uzaklik < min) {
                min = uzaklik;
                enYakinAsteroit = asteroit;
            } 
        }
        double aci = acinoktas�(gemi.x, gemi.y, gemi.a, enYakinAsteroit.x, enYakinAsteroit.y);//a��y� hesapla
        double sonuc = sinirAgi.ileriBesleme(normalise(enYakinAsteroit.x, enYakinAsteroit.y, aci, gemi.a)).data[0][0];
        
        double dSol = Math.abs(sonuc - 0); //mutlak de�erini al
        double dSag = Math.abs(sonuc - 1); //mutlak de�erini al
        if (dSol < cikisborusu) {
            gemi.dondur(false);
        } else if (dSag < cikisborusu) {
            gemi.dondur(true);
        } 
        
        if (ysaAtesZamani <= 0) {
            ysaAtesZamani = Math.ceil(Fps / SaniyedekiAtesSayi);
            gemi.atesEt();
        } else {
            ysaAtesZamani--;
        }
        
    }
    
    //Jpanel icerisini yeniden ciz.
    @Override
    public void actionPerformed(ActionEvent e) {
        oyunGenelKontrolleri();
        //oyunJPanel.validate();
        if (ysaAktif) {
            yapayZekaHamleler();
        }
        if (!oyunJPanel.getRootPane().isValid()) {
            oyunTimer.stop();
        }
        oyunJPanel.repaint();
    }
    
    
    //�izimler burada yap�lacak
    //G�r�n�ml� aray�z kullan�ld���nda tu�lar buradan dinlenecek.
    public class  GorunumJpanel extends JPanel implements KeyListener{
        
        public GorunumJpanel(){
            setPreferredSize( new Dimension( Oyun_Genislik, Oyun_Yukseklik) );//Jpaneli boyutland�r.
            
            if (!ysaAktif) {//ysa aktifken tu�lar pasif olmal�
                addKeyListener(this);
            }
            
            setFocusable(true);
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); //Olu�turulan y�ntemlerin g�vdesini de�i�tirmek i�in Ara�lar | �ablonlar.
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            
            
            //Arka plan� �iz
            arkaPlaniCiz(g2);
            
            //Gemiyi �iz
            gemiyiCiz(g2);
            if (gemi.isIttirme()) {
                gemiAlevCiz(g2);
            }
            
            //Asteroitleri ciz
            asteroitleriCiz(g2);
            
//            if (cemberleriGoster) {
//                gemiCemberCiz(g2);
//                asteroitlerCemberCiz(g2);
//            }
            
            //mermileri ciz
            mermileriCiz(g2);
            
            //Skoru yaz
            skoruCiz(g2);
            
            //g2.drawLine(0, Oyun_Yukseklik/2, Oyun_Genislik, Oyun_Yukseklik/2);
            //g2.drawLine(Oyun_Genislik/2, 0, Oyun_Genislik/2, Oyun_Yukseklik);
        }

        public void skoruCiz(Graphics2D g2) {
            g2.setFont(skorFont);
            g2.setColor(Color.WHITE);
            g2.drawString("Skor:"+skor, Oyun_Genislik-Oyun_Genislik/4, 25);
        }
        
        public void mermileriCiz(Graphics2D g2){
            for (Mermi mermi : gemi.getMermiler()) {
                g2.setColor(Color.white);
                g2.draw(new Line2D.Double(mermi.x,mermi.y,mermi.x,mermi.y));
            }
        }
        
        void asteroitlerCemberCiz(Graphics2D g2) {
            g2.setColor(Color.CYAN);
            g2.setStroke(gemiVeAsteroitStroke);
            for (Asteroit asteroit : asteroitler) {
                Ellipse2D.Double asteroitCember = new Ellipse2D.Double(asteroit.x - asteroit.r, asteroit.y - asteroit.r, asteroit.r * 2, asteroit.r *2);
                g2.draw(asteroitCember);
            }
            
        }
        
        void gemiCemberCiz(Graphics2D g2) {
            g2.setColor(Color.red);
            g2.setStroke(gemiVeAsteroitStroke);
            Ellipse2D.Double gemiCember = new Ellipse2D.Double(gemi.x - gemi.r, gemi.y - gemi.r, gemi.r * 2, gemi.r *2);
            g2.draw(gemiCember);
        }
        
        void asteroitleriCiz(Graphics2D g2){
            for (Asteroit asteroit : asteroitler) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(gemiVeAsteroitStroke);//�izginin kal�nl��� ve u� keskinli�i ayarland�
                g2.draw(asteroit.getAsteroitPath2D());
            }
        }
        
        void gemiAlevCiz(Graphics2D g2) {
            g2.setStroke(gemiAlevStroke);
            g2.setPaint(Color.red);
            g2.fill(gemi.getGemiAlevPath2D());
            g2.setColor(Color.yellow);
            g2.draw(gemi.getGemiAlevPath2D());
            
        }
        
        void gemiyiCiz(Graphics2D g2){
            g2.setColor(Color.white);
            g2.setStroke(gemiVeAsteroitStroke);//�izginin kal�nl��� ve u� keskinli�i ayarland�
            g2.draw(gemi.getGemiPath2D());
        }

        void arkaPlaniCiz(Graphics2D g2){
            g2.setColor(Color.black);
            g2.fillRect(0, 0, Oyun_Genislik, Oyun_Yukseklik);
        }
        @Override
        public void keyTyped(KeyEvent e) {
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //Butona bas�lma i�lemleri
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    //Space tusuna basma
                    gemi.atesEt();
                    gemi.setAtesEdebilir(false);
                    break;
                case KeyEvent.VK_LEFT:
                    //Sol oka basma
                    gemi.dondur(false);
                    break;
                case KeyEvent.VK_RIGHT:
                    //Sa� oka basma
                    gemi.dondur(true);
                    break;
                case KeyEvent.VK_UP:
                    //Yukar� ok tusuna basma
                    gemi.setIttirme(true);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    //Space tusundan parmak kald�rma
                    gemi.setAtesEdebilir(true);
                    break;
                case KeyEvent.VK_LEFT:
                    //Sol oktan parmak kald�rma
                    gemi.setDonme(0);
                    break;
                case KeyEvent.VK_RIGHT:
                    //Sa� oktan parmak kald�rma
                    gemi.setDonme(0);
                    break;
                case KeyEvent.VK_UP:
                    //Yukar� ok tusuna basma
                    gemi.setIttirme(false);
                    break;
                default:
                    break;
            }
        }
        
    }
    
    
}
