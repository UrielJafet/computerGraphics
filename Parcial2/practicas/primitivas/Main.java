package primitivas;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                JFrame frame = new JFrame("Primitivas");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                PanelDibujo panel = new PanelDibujo();
                panel.setPreferredSize(new Dimension(800, 600)); 
                frame.add(panel);
                
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
