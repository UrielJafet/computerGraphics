package espiralArquimides;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;

public class Espiral extends JPanel implements Runnable {
    
    private Thread thr;
    private Image buffer;
    private Graphics bufferGraphics;
    private double theta = 0.0;
    int ultimoX = -1;
    int ultimoY = -1;
    int ultimoX2 = -1;
    int ultimoY2 = -1;

    public Espiral() {
        JFrame frame = new JFrame("Espiral de Arquímedes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(this);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        thr = new Thread(this);
        thr.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (buffer == null) {
            // Crear un buffer en memoria para dibujar la espiral
            buffer = createImage(getWidth(), getHeight());
            bufferGraphics = buffer.getGraphics();

            // Inicializar el buffer con un fondo blanco
            bufferGraphics.setColor(getBackground());
            bufferGraphics.fillRect(0, 0, getWidth(), getHeight()); 

            // Punto inicial de la espiral
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            ultimoX = centerX;
            ultimoY = centerY;
            ultimoX2 = centerX;
            ultimoY2 = centerY;
        }
        g.drawImage(buffer, 0, 0, this);
    }

    public void run(){
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        double a = 0; // inicia en el centro
        double b = 2.5; // factor de crecimiento

        while (true) {
            try {
                double r =a + b * theta; // Radio de la espiral

                // Calcular las coordenadas del punto en la espiral (polares a cartesianas)
                // Espiral 1
                int x = (int) (centerX + r * Math.cos(theta));
                int y = (int) (centerY - r * Math.sin(theta));
                // Espiral 2
                int x2 = (int) (centerX - r * Math.cos(theta));
                int y2 = (int) (centerY + r * Math.sin(theta));

                // Dibujar en el buffer el segmento de línea desde el último punto hasta el nuevo punto
                if (bufferGraphics != null) {
                    // Dibujar espiral 1
                    bufferGraphics.setColor(Color.BLUE);
                    bufferGraphics.drawLine(ultimoX, ultimoY, x, y);
                    // Dibujar espiral 2
                    bufferGraphics.setColor(Color.RED);
                    bufferGraphics.drawLine(ultimoX2, ultimoY2, x2, y2);

                }

                // Actualizar los puntos para el próximo segmento
                theta += 0.05; // Incrementar el ángulo para el siguiente punto
                ultimoX = x;
                ultimoY = y;
                ultimoX2 = x2;
                ultimoY2 = y2;

                repaint(); // Solicitar repintado del panel
                Thread.sleep(10); // Pausa para animación
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Espiral();
    }
}
