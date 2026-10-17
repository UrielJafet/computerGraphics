package primitivas;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                JFrame frame = new JFrame("Primitivas");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                PanelDibujo panel = new PanelDibujo();
                frame.add(panel);
                
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
