package astroidsgameai.gorunum;

import astroidsgameai.yapayzeka.SinirAgi;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

public class OyunPencere extends JFrame {
    
    private final int PENCERE_GENEISLIK     = 700;   
    private final int PENCERE_YUKSEKLIK    = 700;
    AsteroidsOyunGorunum oyunGorunum;
    
    public OyunPencere() {
        //setDefaultLookAndFeelDecorated(true);       //Ekran genisliginin ayarlanan genislik olmasýný saglar. Kenarlýklarla ilgili duzeltme
        setTitle("Astroids Game");                  //Oyun basligini ayarla
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(0, 0, PENCERE_GENEISLIK, PENCERE_YUKSEKLIK); //Pencerenin boyutunu ayarla
        setLayout( new FlowLayout( FlowLayout.CENTER, 10, 10) ); //Oyunu ortala ve kenarlardan 10ar px bosluk olsun
        oyunGorunum = new AsteroidsOyunGorunum();
        add(oyunGorunum.oyunJPanel);
        
        pack();
        setVisible(true); //Pencereyi görünür yap
    }
    public OyunPencere(SinirAgi sinirAgi) {// sinir aðý ile oyunu olustur
        //setDefaultLookAndFeelDecorated(true);       //Ekran genisliginin ayarlanan genislik olmasýný saglar. Kenarlýklarla ilgili duzeltme
        setTitle("Astroids Game");                  //Oyun basligini ayarla
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(0, 0, PENCERE_GENEISLIK, PENCERE_YUKSEKLIK); //Pencerenin boyutunu ayarla
        setLayout( new FlowLayout( FlowLayout.CENTER, 10, 10) ); //Oyunu ortala ve kenarlardan 10ar px bosluk olsun
        oyunGorunum = new AsteroidsOyunGorunum(sinirAgi);
        add(oyunGorunum.oyunJPanel);
        
        pack();
        setVisible(true); //Pencereyi görünür yap
    }

 
}
