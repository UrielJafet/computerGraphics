package reloj;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Reloj Analógico");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Necesario para que la transparencia funcione
                frame.setUndecorated(true);
                // Fondo del FRAME totalmente transparente (alpha = 0)
                frame.setBackground(new Color(0, 0, 0, 0));
                //frame.setOpacity(0.95f);
                PanelReloj panel = new PanelReloj();
                panel.setOpaque(false); // el panel no debe pintar un fondo sólido

                
                frame.add(panel);
                frame.setSize(400, 400);
                frame.setLocationRelativeTo(null); 
                frame.setVisible(true);
            }
        });
    }
}
