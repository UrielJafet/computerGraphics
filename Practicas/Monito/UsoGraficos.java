package Monito;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;

public class UsoGraficos extends JPanel {
    
    public UsoGraficos() {
        JFrame frame = new JFrame("Uso de Gráficos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 300);
        frame.add(this);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Demo de gráficos", 50, 50);
        //Dibujar carita
        g.drawArc(50, 60, 50, 50, 0, 360);
        g.drawArc(60, 70, 30, 30, 180, 180);
        g.fillOval(65, 75, 5, 5);
        g.fillOval(80, 75, 5, 5);
        //Dibujar cuerpo
        g.drawLine(75, 110, 75, 200);
        //Brazos
        g.drawLine(75, 120, 45, 160);
        g.drawLine(75, 120, 105, 160);
        //Piernas
        g.drawLine(75, 200, 45, 240);
        g.drawLine(75, 200, 105, 240);
    }

    public static void main(String[] args) {
        new UsoGraficos();
    }
    
}
