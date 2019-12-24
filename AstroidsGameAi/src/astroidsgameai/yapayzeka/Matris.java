package astroidsgameai.yapayzeka;

    public class Matris {
    
    public int satir;
    public int sutun;
    public double data[][];
    
    public Matris (int satir,int sutun){
        this.satir = satir;
        this.sutun = sutun;
        this.data = bosData(satir, sutun);
    }
    
    public Matris (double[] dizi){//verilen tek boyutlu diziyi matrise donusturur
        this.satir = 1;
        this.sutun = dizi.length;
        this.data = new double[satir][sutun];
        data[0] = dizi;
    }
    
    public Matris(int satir,int sutun, double data[][]){
        this.satir = satir;
        this.sutun = sutun;
        this.data =data;
        
        if (data==null || data.length == 0) {// gelen data bossa 0 ile doldur
            this.data = bosData(satir, sutun);
        }else if(data.length != satir || data[0].length != sutun){
            throw new Error("Hatalı matris olusturma");
        }
    } 
    
    static double sigmoidF(double x, boolean turev){
        if (turev) {
            return x * (1 - x); 
        }
        return 1 / (1 + Math.exp(-x));
    }
    
    static Matris sigmoid(Matris a,boolean turev){ //Verilen matrise sigmoid uygula
        Matris m = new Matris(a.getSatir(), a.getSutun());
        for (int i = 0; i < m.satir; i++) {
            for (int j = 0; j < m.sutun; j++) {
                m.data[i][j] = sigmoidF(a.data[i][j], turev);
            }
        }
        return m;
    }
    
    public void rastgeleAgirlik(){//datayı ratgele ağırlıklar ile doldur -1 ile 1 arasında 
        for (int i = 0; i < this.satir; i++) {
            for (int j = 0; j < this.sutun; j++) {
                this.data[i][j] = Math.random() *2 - 1 ;
            }
        }
    }
    
    static Matris transpoze(Matris a){ // verilen matrisin transpozu
        Matris m = new Matris(a.getSutun(), a.getSatir());
        for (int i = 0; i < a.getSatir(); i++) {
            for (int j = 0; j < a.getSutun(); j++) {
                m.data[j][i] = a.data[i][j];
            }
        }
        return m;
    }
    
    static Matris cikarMatris(Matris a, Matris b){
        Matris.boyutKontrol(a, b);
        Matris m = new Matris(a.getSatir(), a.getSutun());
        for (int i = 0; i < m.getSatir(); i++) {
            for (int j = 0; j < m.getSutun(); j++) {
                m.data[i][j] = a.data[i][j] - b.data[i][j];
            }
        }
        return m;
    }
    
    static Matris carpMatris(Matris a, Matris b) {
        Matris.boyutKontrol(a, b);
        Matris m = new Matris(a.getSatir(), a.getSutun());
        for (int i = 0; i < m.getSatir(); i++) {
            for (int j = 0; j < m.getSutun(); j++) {
                m.data[i][j] = a.data[i][j] * b.data[i][j];
            }
        }
        return m;
    }
    
    static Matris dotCarpimi (Matris a, Matris b){//iki matrisin dot carpımı
        if (a.getSutun() != b.getSatir()) {
            throw new Error("Matrisler dot carpımı yapılamaz");
        }
        Matris m = new Matris(a.getSatir(), b.getSutun());
        for (int i = 0; i < m.getSatir(); i++) {
            for (int j = 0; j < m.getSutun(); j++) {
                double dot = 0;
                for (int k = 0; k < a.sutun; k++) {
                    dot += a.data[i][k] * b.data[k][j];
                }
                m.data[i][j] = dot; 
            }
        }
        return m;
    }
    
    static Matris toplaMatris (Matris a, Matris b){ // iki matrisi topla
        boyutKontrol(a, b);
        Matris m = new Matris(a.getSatir(), a.getSutun());
        for (int i = 0; i < a.getSatir(); i++) {
            for (int j = 0; j < a.getSutun(); j++) {
                m.data[i][j] = a.data[i][j] + b.data[i][j];
            }
        }
        return m;
    }
    
    static void boyutKontrol(Matris a, Matris b){
        if (a.getSatir() != b.getSatir() || a.getSutun() != b.getSutun()) {
            throw new Error("Matrisler farklı boyutta olamaz.");
        }
    }
    
    public double[][] bosData (int satir, int sutun){
        double[][] bosMatris = new double[satir][sutun];
        for (int i = 0; i < satir; i++) {
            for (int j = 0; j < sutun; j++) {
                bosMatris[i][j] = 0;
            }
        }
        return bosMatris;
    }

    public int getSatir() {
        return satir;
    }

    public void setSatir(int satir) {
        this.satir = satir;
    }

    public int getSutun() {
        return sutun;
    }

    public void setSutun(int sutun) {
        this.sutun = sutun;
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
    }
    
    
}
