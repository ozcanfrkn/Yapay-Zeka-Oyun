package astroidsgameai.yapayzeka;

public class SinirAgi {
    
    public int gNoronS;//giris katman noron sayisi
    public int kNoronS;//gizli katman noron sayisi
    public int cNoronS;//cikis katman noron sayisi
    
    public Matris giris;//giris datalarý tutadak matris
    public Matris gizlikatman;//gizli katman datasýný tutan matris
    
    public Matris girisAgirlik;//her giris verisinin aðýrlýðý
    public Matris gizlikatmanAgirlik;//her gizli katmanýn aðýrlýðý
    
    public Matris gizlikatmanBias;//gizli katmandaki bias deðeri
    public Matris cikisBias;//çýkýþýn önyargýsý
    
    public SinirAgi(int gNoronS, int kNoronS, int cNoronS) {
        this.gNoronS = gNoronS;
        this.kNoronS = kNoronS;
        this.cNoronS = cNoronS;
        
        this.gizlikatmanBias = new Matris(1, kNoronS);
        this.cikisBias = new Matris(1, cNoronS);
        
        this.girisAgirlik = new Matris(gNoronS,kNoronS);
        this.gizlikatmanAgirlik = new Matris(kNoronS, cNoronS);
        
        //Baslangýçta aðýrlýklarý ratgele olusturma
        gizlikatmanBias.rastgeleAgirlik();
        cikisBias.rastgeleAgirlik();
        girisAgirlik.rastgeleAgirlik();
        gizlikatmanAgirlik.rastgeleAgirlik();
        
    }
    
    public void egitim(double[] girisVeri, double[] beklenenCikis){
        Matris cikis = this.ileriBesleme(girisVeri);// mevcut að uzerinde çýkýþ deðeri üretme
        
        // çýktý hatalarýný hesaplamak (hedef - çýktý)
        Matris hedef = new Matris(beklenenCikis); //Hedafin deðerini matrise koy
        Matris cikisHata = Matris.cikarMatris(hedef, cikis);
        
        // deltalarý hesaplar (hata * çýkýþýn türevi)
        Matris cikisTurev = Matris.sigmoid(cikis, true);//cikisin sigmoid türevini al
        Matris cikisDelta = Matris.carpMatris(cikisHata, cikisTurev); // hata ile ciksin sigmoid türevinin çarpýmý deltadýr
        
        // gizli katman hatalarýný hesapla (delta "nokta" aðýrlýklarýn transpoze1)
        Matris gizlikatmanAgirlikTranspoz = Matris.transpoze(this.gizlikatmanAgirlik);//Gizli katmanýn aðýrlýðýnýn transpozu(çarpma iþlemi için getrekli)
        Matris gizlikatmanHata = Matris.dotCarpimi(cikisDelta, gizlikatmanAgirlikTranspoz);//gizli katmanýn hata oraný
        
        // gizli deltalarý hesaplama (Hata * gizli katmanýn türevi)
        Matris gizlikatmanTurev = Matris.sigmoid(this.gizlikatman, true); // gizli katman sigmoit türevi al
        Matris gizlikatmanDelta = Matris.carpMatris(gizlikatmanHata, gizlikatmanTurev);//gizli katmanýn deltalarý
        
        // aðýrlýklarý güncelleme 
        Matris gizlikatmanT = Matris.transpoze(this.gizlikatman);//gizli katman transpozu
        this.gizlikatmanAgirlik = Matris.toplaMatris(this.gizlikatmanAgirlik, Matris.dotCarpimi(gizlikatmanT, cikisDelta));
        Matris girisT = Matris.transpoze(this.giris);
        this.girisAgirlik = Matris.toplaMatris(this.girisAgirlik, Matris.dotCarpimi(girisT, gizlikatmanDelta));
        
        //bias guncelleme
        this.cikisBias = Matris.toplaMatris(this.cikisBias, cikisDelta);
        this.gizlikatmanBias = Matris.toplaMatris(this.gizlikatmanBias, gizlikatmanDelta);
    }
    
    public Matris ileriBesleme(double[] girisVeri){//Mevcut að üzerinde çýkýþ üret
        this.giris = new Matris(girisVeri);//giris verisini matrise donustur.
        
        // gizli deðerleri bul ve aktivasyon iþlevini uygula
        this.gizlikatman = Matris.dotCarpimi(this.giris, this.girisAgirlik);//giriþ katmanýný aðýrlýklar ile çarp gizli katmana koy
        this.gizlikatman = Matris.toplaMatris(this.gizlikatman, this.gizlikatmanBias); // gizli katmana önyargý uygula
        this.gizlikatman = Matris.sigmoid(this.gizlikatman, false);//gizli katmandaki her hucreye sigmoid uygula
        
        // Çýkýþ deðerlerini bulmak ve aktivasyon fonksiyonunu uygulamak
        Matris cikis = Matris.dotCarpimi(gizlikatman, gizlikatmanAgirlik); //Gizli katmaný aðýrlýklarý ile çarp çýkýþ katmanýna koy
        cikis = Matris.toplaMatris(cikis, this.cikisBias);//Çýkýþ katmanýna önyargý uygulama
        cikis = Matris.sigmoid(cikis, false);//Çýkýþ katmanýndaki her hücreye sigmoid uygula
        
        return cikis;
    }
    
    public double sigmoid (double x, boolean turev){
        if (turev) {
            return x * (1 - x); 
        }
        return 1 / (1 + Math.exp(-x));
    }

    public int getgNoronS() {
        return gNoronS;
    }

    public void setgNoronS(int gNoronS) {
        this.gNoronS = gNoronS;
    }

    public int getkNoronS() {
        return kNoronS;
    }

    public void setkNoronS(int kNoronS) {
        this.kNoronS = kNoronS;
    }

    public int getcNoronS() {
        return cNoronS;
    }

    public void setcNoronS(int cNoronS) {
        this.cNoronS = cNoronS;
    }

    public Matris getGiris() {
        return giris;
    }

    public void setGiris(Matris giris) {
        this.giris = giris;
    }

    public Matris getgizlikatman() {
        return gizlikatman;
    }

    public void setgizlikatman(Matris gizlikatman) {
        this.gizlikatman = gizlikatman;
    }

    public Matris getGirisAgirlik() {
        return girisAgirlik;
    }

    public void setGirisAgirlik(Matris girisAgirlik) {
        this.girisAgirlik = girisAgirlik;
    }

    public Matris getgizlikatmanAgirlik() {
        return gizlikatmanAgirlik;
    }

    public void setgizlikatmanAgirlik(Matris gizlikatmanAgirlik) {
        this.gizlikatmanAgirlik = gizlikatmanAgirlik;
    }

    public Matris getgizlikatmanBias() {
        return gizlikatmanBias;
    }

    public void setgizlikatmanBias(Matris gizlikatmanBias) {
        this.gizlikatmanBias = gizlikatmanBias;
    }

    public Matris getCikisBias() {
        return cikisBias;
    }

    public void setCikisBias(Matris cikisBias) {
        this.cikisBias = cikisBias;
    }
    
    
}
