package mouseFrame;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseMonitor extends JPanel implements MouseListener , MouseMotionListener {

    private JLabel barraEstado;

    public MouseMonitor() {
        JFrame frame = new JFrame();
        frame.setTitle("Mouse Frame");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Agregamos barra de estado en la parte inferior del frame
        barraEstado = new JLabel("Mueve el ratón dentro de la ventana para iniciar.", JLabel.CENTER);
        frame.getContentPane().add(barraEstado, BorderLayout.SOUTH);

        // Agregamos el MouseMonitor como listener de eventos de mouse
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.setVisible(true);
    }

    // ==========================================
    // MÉTODOS DE MouseMotionListener
    // ==========================================
    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("Mouse dragged at: (" + e.getX() + ", " + e.getY() + ")");
        barraEstado.setText("Mouse dragged at: (" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println("Mouse moved at: (" + e.getX() + ", " + e.getY() + ")");
        barraEstado.setText("Mouse moved at: (" + e.getX() + ", " + e.getY() + ")");
    }

    // ==========================================
    // MÉTODOS DE MouseListener
    // ==========================================
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse clicked at: (" + e.getX() + ", " + e.getY() + ")");
        barraEstado.setText("Mouse clicked at: (" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(Color.LIGHT_GRAY);
        System.out.println("Mouse entered the component at: (" + e.getX() + ", " + e.getY() + ")");
        barraEstado.setText("Mouse entered the component at: (" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(Color.WHITE);
        System.out.println("Mouse exited the component at: (" + e.getX() + ", " + e.getY() + ")");
        barraEstado.setText("Mouse exited the component at: (" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse pressed at: (" + e.getX() + ", " + e.getY() + ")");
        barraEstado.setText("Mouse pressed at: (" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released at: (" + e.getX() + ", " + e.getY() + ")");
        barraEstado.setText("Mouse released at: (" + e.getX() + ", " + e.getY() + ")");
    }

    public static void main(String[] args) {
        new MouseMonitor();
    }
}
