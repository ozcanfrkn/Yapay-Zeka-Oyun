package astroidsgameai;

import astroidsgameai.gorunum.AnaPencere;
import javax.swing.SwingUtilities;

public class AsteroidsGameAi {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AnaPencere ana = new AnaPencere();//Ana pencereyi olustur 
                ana.setLocationRelativeTo(null);//Ekranda ortala
            }
        });
    }
    
}
