package astroidsgameai.oyun;

import java.awt.geom.Path2D;
import java.util.ArrayList;

public class Gemi {
    
    public final int OyunGenislik;
    public final int OyunYukseklik;
    public int fps;
    public boolean carpisma;//Gemi ile asteroit carpistiysa.
    public double x;
    public double y;
    public int r;//gemi yar��ap
    public double a;//Gemi a��s� 
    public double aRadian;//Gemi a��s� radians cinsinden
    public double donme; //Sonraki yenilemede donmesi gereken bir yer varsa burada tutulur.
    public int canlar; //Yeniden canlanma hakk�
    public boolean ittirme;//ileri ok tusuna b�s�l�nca gemiyi ileri ittir
    public double ittirmeX;//x ekseni uzerinde gemi ne kadar itilecek
    public double ittirmeY;//y ekseni uzerinde gemi ne kadar itilecek
    public double olumsuzlukKalanSure;
    public final int IvmelenmeGucu = 5;//Geminin saniyede ivmelenme pixel say�s�.
    public final double SurtunmeKatsayisi = 0.7;//Gemiyi ittirmiyorken yava�lamas� i�in s�rt�nme katsay�s�
    public final int Boy = 30;//Gemi boyu
    public final int GemiDonusH�z� = 360; //Fpsye ba�l� gemi donus h�z�.
    public final double GorunmezlikSuresi = 0.2*100;// yaniden canlan�nca belirtilen saniyede bir tekrar gorunur ol
    public final int OlumsuzlukSuresi = 3;//Yeniden canlan�nca ilk 3 saniye olumsuz ol
    Path2D gemiPath2D;
    Path2D gemiAlevPath2D;
    
    public ArrayList<Mermi> mermiler;
    public final int MaxMermiSayisi = 10;
    public boolean atesEdebilir;//Ates etmeye izin var m� 
    
    public Gemi(int OyunGenislik,int OyunYukseklik,int canlar, int fps){
        this.OyunGenislik = OyunGenislik;
        this.OyunYukseklik = OyunYukseklik;
        this.carpisma = false;
        this.x = OyunGenislik / 2;
        this.y = OyunYukseklik / 2;
        this.r = Boy / 2;
        this.a = 90; // Ba�lang�� a��s� 90 derece
        this.aRadian = Math.toRadians(a); // Ba�lang�� a��s� radians cinsinden 
        this.canlar = canlar;
        this.fps = fps;
        this.ittirme = false;
        this.ittirmeX = 0;
        this.ittirmeY = 0;
        this.olumsuzlukKalanSure = OlumsuzlukSuresi * 1000;
        this.gemiPath2D = new Path2D.Double();
        this.gemiAlevPath2D = new Path2D.Double();
        
        this.mermiler = new ArrayList<>();
        this.atesEdebilir = true;
    }
    
    public void atesEt() {
        if(mermiler.size() < MaxMermiSayisi && atesEdebilir)
            mermiler.add(new Mermi(this, OyunGenislik, OyunYukseklik)); 
    }
    
    public Path2D getGemiPath2D() {
        gemiPath2D.reset();
        if (olumsuzlukKalanSure <= 0 || Math.ceil(olumsuzlukKalanSure) % GorunmezlikSuresi == 0) {
            gemiPath2D.moveTo(  x + (double)4 / 3 * r * Math.cos(aRadian), 
                                y - (double)4 / 3 * r * Math.sin(aRadian)
            );

            gemiPath2D.lineTo(  x - r * ((double)2/3*Math.cos(aRadian) + Math.sin(aRadian) ),
                                y + r * ((double)2/3*Math.sin(aRadian) - Math.cos(aRadian) )
            );

            gemiPath2D.lineTo(  x - r * ((double)2/3*Math.cos(aRadian) - Math.sin(aRadian) ),
                                y + r * ((double)2/3*Math.sin(aRadian) + Math.cos(aRadian) )
            );
            gemiPath2D.closePath();
        }
        return gemiPath2D;
    }
    
    public Path2D getGemiAlevPath2D() {
        gemiAlevPath2D.reset();
        gemiAlevPath2D.moveTo( 
            x - r * ((double)2 / 3 * Math.cos(aRadian) + 0.5 * Math.sin(aRadian)),
            y + r * ((double)2 / 3 * Math.sin(aRadian) - 0.5 * Math.cos(aRadian))
        );
        gemiAlevPath2D.lineTo( 
            x - r * (double)5 / 3 * Math.cos(aRadian),
            y + r * (double)5 / 3 * Math.sin(aRadian)
        );
        gemiAlevPath2D.lineTo( 
            x - r * ((double)2 / 3 * Math.cos(aRadian) - 0.5 * Math.sin(aRadian)),
            y + r * ((double)2 / 3 * Math.sin(aRadian) + 0.5 * Math.cos(aRadian))
        );
        //gemiAlevPath2D.closePath();
        return gemiAlevPath2D;
    }
    
    public void dondur(boolean yon){
        //yon: true ise sa� false ise sol
        int sinyal = yon ? -1 : 1;
        donme = GemiDonusH�z� / fps * sinyal;
    }
    
    public void aciyiAyarla(){// donme isleminin fps h�z�nda tamamlanmas�
        a += donme;
        if (a < 0) {
            a += 360;
        }else if(a > 360){
            a -= 360;
        }
        aRadian = Math.toRadians(a);
    }
    
    public void ittir (){
        if (ittirme) {//yukar� ok tusuna bas�l�yorsa ittir
            ittirmeX += IvmelenmeGucu * Math.cos(aRadian) / fps;
            ittirmeY -= IvmelenmeGucu * Math.sin(aRadian) / fps;
        }else{//Bas�lm�yorsa s�rt�nme kat say�s�na g�re yava�lat
            ittirmeX -= SurtunmeKatsayisi * ittirmeX / fps;
            ittirmeY -= SurtunmeKatsayisi * ittirmeY / fps;
        }
    }
    public void konumuAyarla(){//Gemiyi ittirme sinden sonra degisen konumu ayarla
        x += ittirmeX;
        y += ittirmeY;
    }
    
    public void ekrandaTut() {//Ekrandan c�kt�ysa yeniden ekrana konumla.
        if (x < 0 - r) {
            x = OyunGenislik + r;
        }else if(x > OyunGenislik + r){
            x = 0 - r;
        }
        if (y < 0 - r) {
            y = OyunYukseklik + r;  
        }else if(y > OyunGenislik + r){
            y = 0 - r;
        }
    }
    public void olumsuzlukKontrolu(){
        if (olumsuzlukKalanSure > 0) {
            olumsuzlukKalanSure-= 1000/fps;
        }
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

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public int getCanlar() {
        return canlar;
    }

    public void setCanlar(int canlar) {
        this.canlar = canlar;
    }

    public double getaRadian() {
        return aRadian;
    }

    public void setaRadian(double aRadian) {
        this.aRadian = aRadian;
    }

    public double getDonme() {
        return donme;
    }

    public void setDonme(double donme) {
        this.donme = donme;
    }

    public boolean isIttirme() {
        return ittirme;
    }

    public void setIttirme(boolean ittirme) {
        this.ittirme = ittirme;
    }

    public double getIttirmeX() {
        return ittirmeX;
    }

    public void setIttirmeX(double ittirmeX) {
        this.ittirmeX = ittirmeX;
    }

    public double getIttirmeY() {
        return ittirmeY;
    }

    public void setIttirmeY(double ittirmeY) {
        this.ittirmeY = ittirmeY;
    }

    public boolean isCarpisma() {
        return carpisma;
    }

    public void setCarpisma(boolean carpisma) {
        this.carpisma = carpisma;
    }

    public ArrayList<Mermi> getMermiler() {
        return mermiler;
    }

    public void setMermiler(ArrayList<Mermi> mermiler) {
        this.mermiler = mermiler;
    }

    public boolean isAtesEdebilir() {
        return atesEdebilir;
    }

    public void setAtesEdebilir(boolean atesEdebilir) {
        this.atesEdebilir = atesEdebilir;
    }
    
    
    
}
