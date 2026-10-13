package keyFrame;

import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;

public class KeyMonitor extends JPanel implements KeyListener {

    public KeyMonitor() {
        // Configuración del panel
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);

        // Configuración del label para mostrar el estado de las teclas
        JLabel label = new JLabel("Presiona cualquier tecla...");
        add(label);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Key typed: " + e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key pressed: " + e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Key released: " + e.getKeyChar());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new javax.swing.JFrame("Key Monitor");
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.add(new KeyMonitor());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
