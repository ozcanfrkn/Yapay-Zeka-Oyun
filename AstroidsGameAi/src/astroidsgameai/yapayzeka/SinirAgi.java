package astroidsgameai.yapayzeka;

public class SinirAgi {
    
    public int gNoronS;//giris katman noron sayisi
    public int kNoronS;//gizli katman noron sayisi
    public int cNoronS;//cikis katman noron sayisi
    
    public Matris giris;//giris datalar� tutadak matris
    public Matris gizlikatman;//gizli katman datas�n� tutan matris
    
    public Matris girisAgirlik;//her giris verisinin a��rl���
    public Matris gizlikatmanAgirlik;//her gizli katman�n a��rl���
    
    public Matris gizlikatmanBias;//gizli katmandaki bias de�eri
    public Matris cikisBias;//��k���n �nyarg�s�
    
    public SinirAgi(int gNoronS, int kNoronS, int cNoronS) {
        this.gNoronS = gNoronS;
        this.kNoronS = kNoronS;
        this.cNoronS = cNoronS;
        
        this.gizlikatmanBias = new Matris(1, kNoronS);
        this.cikisBias = new Matris(1, cNoronS);
        
        this.girisAgirlik = new Matris(gNoronS,kNoronS);
        this.gizlikatmanAgirlik = new Matris(kNoronS, cNoronS);
        
        //Baslang��ta a��rl�klar� ratgele olusturma
        gizlikatmanBias.rastgeleAgirlik();
        cikisBias.rastgeleAgirlik();
        girisAgirlik.rastgeleAgirlik();
        gizlikatmanAgirlik.rastgeleAgirlik();
        
    }
    
    public void egitim(double[] girisVeri, double[] beklenenCikis){
        Matris cikis = this.ileriBesleme(girisVeri);// mevcut a� uzerinde ��k�� de�eri �retme
        
        // ��kt� hatalar�n� hesaplamak (hedef - ��kt�)
        Matris hedef = new Matris(beklenenCikis); //Hedafin de�erini matrise koy
        Matris cikisHata = Matris.cikarMatris(hedef, cikis);
        
        // deltalar� hesaplar (hata * ��k���n t�revi)
        Matris cikisTurev = Matris.sigmoid(cikis, true);//cikisin sigmoid t�revini al
        Matris cikisDelta = Matris.carpMatris(cikisHata, cikisTurev); // hata ile ciksin sigmoid t�revinin �arp�m� deltad�r
        
        // gizli katman hatalar�n� hesapla (delta "nokta" a��rl�klar�n transpoze1)
        Matris gizlikatmanAgirlikTranspoz = Matris.transpoze(this.gizlikatmanAgirlik);//Gizli katman�n a��rl���n�n transpozu(�arpma i�lemi i�in getrekli)
        Matris gizlikatmanHata = Matris.dotCarpimi(cikisDelta, gizlikatmanAgirlikTranspoz);//gizli katman�n hata oran�
        
        // gizli deltalar� hesaplama (Hata * gizli katman�n t�revi)
        Matris gizlikatmanTurev = Matris.sigmoid(this.gizlikatman, true); // gizli katman sigmoit t�revi al
        Matris gizlikatmanDelta = Matris.carpMatris(gizlikatmanHata, gizlikatmanTurev);//gizli katman�n deltalar�
        
        // a��rl�klar� g�ncelleme 
        Matris gizlikatmanT = Matris.transpoze(this.gizlikatman);//gizli katman transpozu
        this.gizlikatmanAgirlik = Matris.toplaMatris(this.gizlikatmanAgirlik, Matris.dotCarpimi(gizlikatmanT, cikisDelta));
        Matris girisT = Matris.transpoze(this.giris);
        this.girisAgirlik = Matris.toplaMatris(this.girisAgirlik, Matris.dotCarpimi(girisT, gizlikatmanDelta));
        
        //bias guncelleme
        this.cikisBias = Matris.toplaMatris(this.cikisBias, cikisDelta);
        this.gizlikatmanBias = Matris.toplaMatris(this.gizlikatmanBias, gizlikatmanDelta);
    }
    
    public Matris ileriBesleme(double[] girisVeri){//Mevcut a� �zerinde ��k�� �ret
        this.giris = new Matris(girisVeri);//giris verisini matrise donustur.
        
        // gizli de�erleri bul ve aktivasyon i�levini uygula
        this.gizlikatman = Matris.dotCarpimi(this.giris, this.girisAgirlik);//giri� katman�n� a��rl�klar ile �arp gizli katmana koy
        this.gizlikatman = Matris.toplaMatris(this.gizlikatman, this.gizlikatmanBias); // gizli katmana �nyarg� uygula
        this.gizlikatman = Matris.sigmoid(this.gizlikatman, false);//gizli katmandaki her hucreye sigmoid uygula
        
        // ��k�� de�erlerini bulmak ve aktivasyon fonksiyonunu uygulamak
        Matris cikis = Matris.dotCarpimi(gizlikatman, gizlikatmanAgirlik); //Gizli katman� a��rl�klar� ile �arp ��k�� katman�na koy
        cikis = Matris.toplaMatris(cikis, this.cikisBias);//��k�� katman�na �nyarg� uygulama
        cikis = Matris.sigmoid(cikis, false);//��k�� katman�ndaki her h�creye sigmoid uygula
        
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
