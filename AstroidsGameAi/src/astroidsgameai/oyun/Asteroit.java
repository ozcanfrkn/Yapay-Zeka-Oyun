package astroidsgameai.oyun;

import java.awt.geom.Path2D;

public class Asteroit {
    
    public int fps;//Oyun saniyede kare sayýsý
    public double x;//x koordinat
    public double y;//Y koordinat
    public double r;//Asteroid yarýcap
    public double a;//asteroid açýsý
    public double aRadyan;//Asteroid radyan cinsinden aci
    public double xIvme;//X eksenindeki ivmesi
    public double yIvme;//Y eksenindeki ivmesi
    public int kose;//Asteroidin kose sayisi
    public double puruzler[];//Asteroidin yuzeyindeki puruzlerin miktarý
    public final int OyunGenislik;
    public final int OyunYukseklik;
    public final int BaslangicHiz = 50; // saniyede piksel cinsinden asteroitlerin maksimum baþlatma hýzý
    public final int BaslangicBoyut = 100; // asteroitlerin baþlangýç boyutu piksel cinsinden
    public final int OrtalamaKoseSayisi = 10; // her bir asteroit üzerinde ortalama köþe sayýsý
    public final double PuruzKatsayisi = 0.3; //Asteroid yuzeyindeki puruzler 0 az 1 çok
    public Path2D asteroitPath2D;
    
    
    public Asteroit(double gemiX, double gemiY, int OyunGenislik,int OyunYukseklik,int fps) {
        this.OyunGenislik = OyunGenislik;
        this.OyunYukseklik = OyunYukseklik;
        this.fps = fps;
        do {            
            this.x = Math.random() * OyunGenislik; 
            this.y = Math.random() * OyunYukseklik;
        } while (ikiNoktaArasindakiUzaklik(this.x, this.y, gemiX, gemiY) < BaslangicBoyut * 2 + BaslangicBoyut / 2);
        this.xIvme = Math.random() * BaslangicHiz / fps * (Math.random() < 0.5 ? 1 : -1);
        this.yIvme = Math.random() * BaslangicHiz / fps * (Math.random() < 0.5 ? 1 : -1);
        this.r = BaslangicBoyut/2;
        this.a = Math.random() * 360;
        this.aRadyan = Math.toRadians(a);
        this.kose = (int) Math.floor(Math.random() * (OrtalamaKoseSayisi + 1) + OrtalamaKoseSayisi / 2);
        this.asteroitPath2D = new Path2D.Double();
        this.puruzler = new double[kose];
        for (int i = 0; i < this.kose; i++) {
            puruzler[i] = Math.random() * PuruzKatsayisi * 2 + 1 - PuruzKatsayisi;
        }
    }
    
    public Asteroit(int OyunGenislik,int OyunYukseklik,int fps,double x, double y, double boyut) {
        this.OyunGenislik = OyunGenislik;
        this.OyunYukseklik = OyunYukseklik;
        this.x = x;
        this.y = y;
        this.xIvme = Math.random() * BaslangicHiz / fps * (Math.random() < 0.5 ? 1 : -1);
        this.yIvme = Math.random() * BaslangicHiz / fps * (Math.random() < 0.5 ? 1 : -1);
        this.r = boyut/2;
        this.a = Math.random() * 360;
        this.aRadyan = Math.toRadians(a);
        this.kose = (int) Math.floor(Math.random() * (OrtalamaKoseSayisi + 1) + OrtalamaKoseSayisi / 2);
        this.asteroitPath2D = new Path2D.Double();
        this.puruzler = new double[kose];
        for (int i = 0; i < this.kose; i++) {
            puruzler[i] = Math.random() * PuruzKatsayisi * 2 + 1 - PuruzKatsayisi;
        }
    }

    public Path2D getAsteroitPath2D() {
        asteroitPath2D.reset();
        asteroitPath2D.moveTo(
                x + r * puruzler[0] * Math.cos(aRadyan), 
                y + r * puruzler[0] * Math.sin(aRadyan)
        );
        for (int i = 1; i < kose; i++) {
            asteroitPath2D.lineTo(
                    x + r * puruzler[i] * Math.cos(Math.toRadians(a + i * 360 / kose)), 
                    y + r * puruzler[i] * Math.sin(Math.toRadians(a + i * 360 / kose))
            );
        }
        asteroitPath2D.closePath();
        return asteroitPath2D;
    }
    
    public double ikiNoktaArasindakiUzaklik(double x1 ,double y1,double x2,double y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getxIvme() {
        return xIvme;
    }

    public void setxIvme(double xIvme) {
        this.xIvme = xIvme;
    }

    public double getyIvme() {
        return yIvme;
    }

    public void setyIvme(double yIvme) {
        this.yIvme = yIvme;
    }
    
    
    
}
