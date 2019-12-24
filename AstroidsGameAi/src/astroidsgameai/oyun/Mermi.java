package astroidsgameai.oyun;

public class Mermi {

    public final int OyunGenislik;
    public final int OyunYukseklik;
    public final int Mermi_Hiz = 15; // saniyede piksel cinsinden lazerlerin hýzý
    public final double Menzil = 0.65;//Max gidilecek yol
    private final Gemi gemi;
    public double x;
    public double y;
    public double ittirmeX;//x ekseni uzerinde gemi ne kadar itilecek
    public double ittirmeY;//y ekseni uzerinde gemi ne kadar itilecek
    public double katEdilenYol;
    
    public Mermi(Gemi gemi,int OyunGenislik,int OyunYukseklik) {
        this.OyunGenislik = OyunGenislik;
        this.OyunYukseklik = OyunYukseklik;
        this.gemi = gemi;
        this.x = gemi.x + 4 / 3 * gemi.r * Math.cos(gemi.aRadian);
        this.y = gemi.y - 4 / 3 * gemi.r * Math.sin(gemi.aRadian);
        this.ittirmeX = Mermi_Hiz * Math.cos(gemi.aRadian);
        this.ittirmeY = Mermi_Hiz * Math.sin(gemi.aRadian);
        this.katEdilenYol = 0;
    }
    
    public void ilerlet() {
        x += ittirmeX;
        y -= ittirmeY;
        katEdilenYol += Math.sqrt(Math.pow(ittirmeX, 2) + Math.pow(ittirmeY, 2));
    }
    
    public void ekrandaTut() {//Ekrandan cýktýysa yeniden ekrana konumla.
        if (x < 0 ) {
            x = OyunGenislik ;
        }else if(x > OyunGenislik ){
            x = 0 ;
        }
        if (y < 0 ) {
            y = OyunYukseklik ;  
        }else if(y > OyunGenislik ){
            y = 0 ;
        }
    }
    
    public boolean yoluTamamladiMi(){//Gidilen yol menzilden buyukse mermi silinecek.
        return katEdilenYol > OyunGenislik * 3 / 4;
    }
    
}
