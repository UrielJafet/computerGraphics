package puntoMedio;

import javax.swing.JFrame;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Color;

public class PuntoMedio extends JFrame{
    
    private static final long serialVersionUID = 1L;

    private BufferedImage buffer;
    
    boolean rectangle = true;
    public PuntoMedio(boolean rectangle) {
        this.rectangle = rectangle;
        // Buffer de 1x1 píxel para dibujar píxeles individuales
        buffer = new BufferedImage(
                1,
                1,
                BufferedImage.TYPE_INT_RGB
        );
    }

    // ==========================================
    // DIBUJAR UN PIXEL
    // ==========================================

    public void putPixel(int x, int y, Color color) {

        Insets insets = getInsets();

        int pantallaX = x + insets.left;
        int pantallaY = y + insets.top;

        // No dibujar fuera del área útil de la ventana
        if (x < 0 || y < 0
                || pantallaX >= getWidth() - insets.right
                || pantallaY >= getHeight() - insets.bottom) {
            return;
        }

        buffer.setRGB(0, 0, color.getRGB());

        Graphics g = getGraphics();

        if (g != null) {
            g.drawImage(buffer, pantallaX, pantallaY, this);
            g.dispose();
        }
    }

    private void algoritmoPuntoMedio(
        int x0,
        int y0,
        int x1,
        int y1,
        Color color) {

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        // Dirección de avance en cada eje (+1 o -1)
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;

        // Si |m| > 1 el eje principal es Y: se intercambian los
        // papeles de dx y dy para reutilizar el mismo caso base.
        boolean empinada = dy > dx;

        if (empinada) {
            int temp = dx;
            dx = dy;
            dy = temp;
        }

        // d = 2 * F(M), así todo es entero:
        // d inicial = 2dy - dx
        int d = 2 * dy - dx;
        int incrE = 2 * dy;
        int incrNE = 2 * (dy - dx);

        int x = x0;
        int y = y0;

        putPixel(x, y, color);

        // dx es ahora la longitud del eje principal
        for (int i = 0; i < dx; i++) {

            if (d > 0) {
                // NE: avanza también en el eje secundario
                d += incrNE;

                if (empinada) {
                    x += sx;
                } else {
                    y += sy;
                }
            } else {
                // E: solo avanza en el eje principal
                d += incrE;
            }

            // Avance en el eje principal
            if (empinada) {
                y += sy;
            } else {
                x += sx;
            }

            putPixel(x, y, color);
        }
    }

    private void dibujarLinea(int x0, int y0, int x1, int y1, Color color) {
        algoritmoPuntoMedio(x0, y0, x1, y1, color);
    }

    private void drawRectangle(int x0, int y0, int x1, int y1, Color color) {
        algoritmoPuntoMedio(x0, y0, x1, y0, color);
        algoritmoPuntoMedio(x1, y0, x1, y1, color);
        algoritmoPuntoMedio(x1, y1, x0, y1, color);
        algoritmoPuntoMedio(x0, y1, x0, y0, color);
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        if (rectangle) {
            drawRectangle(50, 50, 300, 200, Color.BLUE);
        } else {
            // 1) Pendiente suave positiva
            dibujarLinea(50, 50, 300, 200, Color.GREEN);

            // 2) Pendiente suave negativa 
            dibujarLinea(50, 300, 300, 220, Color.BLUE);

            // 3) Horizontal 
            dibujarLinea(50, 30, 300, 30, Color.BLACK);

            // 4) Vertical 
            dibujarLinea(30, 50, 30, 300, Color.ORANGE);

            // 5) Derecha a izquierda 
            dibujarLinea(300, 340, 60, 320, Color.MAGENTA);

            // 6) Empinada |m| > 1 
            dibujarLinea(320, 60, 360, 300, Color.RED);
        }
    }

    // ==========================================
    // PROGRAMA PRINCIPAL
    // ==========================================

    public static void main(String[] args) {

        PuntoMedio medio = new PuntoMedio(false);

        medio.setTitle("Punto medio");
        medio.setSize(400, 400);
        medio.setLocation(50,100);
        medio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        medio.setVisible(true);

        PuntoMedio rectangleo = new PuntoMedio(true);
        rectangleo.setTitle("Rectangulo");
        rectangleo.setSize(400, 400);
        rectangleo.setLocation(470,100);
        rectangleo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rectangleo.setVisible(true);
    }
}
