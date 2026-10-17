package circunferencia;

import javax.swing.JFrame;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Color;

public class Circunferencia extends JFrame{

    private static final long serialVersionUID = 1L;

    private BufferedImage buffer;
    boolean polar = true;
    boolean puntoMedio = false;
    boolean bresenham = false;

    public Circunferencia(boolean polar, boolean puntoMedio, boolean bresenham) {
        this.polar = polar;
        this.puntoMedio = puntoMedio;
        this.bresenham = bresenham;
        // Buffer de 1x1 píxel para dibujar píxeles individuales
        buffer = new BufferedImage(
                1,
                1,
                BufferedImage.TYPE_INT_RGB
        );
    }

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

    // ==========================================
    // Algoritmo Circunferencia
    // ==========================================
    private void circunferencia(int xc, int yc, int r, Color color){

        for (int x = -r; x <= r; x++) {
            int y = (int) Math.round(Math.sqrt(r * r - x * x));
            putPixel(xc + x, yc + y, color);
            putPixel(xc + x, yc - y, color);
        }

    }

    // ==========================================
    // Coordenadas Polares
    // ==========================================
    private void circunferenciaPolar(int xc, int yc, int r, Color color) {
        double step = 1.0 / r; // Paso en radianes
        for (double theta = 0; theta <= 2 * Math.PI; theta += step) {
            int x = (int) Math.round(r * Math.cos(theta));
            int y = (int) Math.round(r * Math.sin(theta));
            putPixel(xc + x, yc + y, color);
        }
    }

    // ==========================================
    // Algoritmo Punto Medio + Simetría de 8 lados
    // ==========================================
    private void circunferenciaPuntoMedio(int xc, int yc, int r, Color color) {
        int x = 0;
        int y = r;
        // Decisión inicial: d = 1 - r
        int d = 1 - r;

        // Dibujar los puntos iniciales (simetría)
        dibujarPuntosSimetricos(xc, yc, x, y, color);

        while (x < y) {
            if (d < 0) {
                // Elegir el píxel Este (E)
                d += 2 * x + 3;
            } else {
                // Elegir el píxel Sureste (SE)
                d += 2 * (x - y) + 5;
                y--;
            }
            x++;
            // Dibujar los 8 puntos simétricos
            dibujarPuntosSimetricos(xc, yc, x, y, color);
        }
    }

    // ==========================================
    // Algoritmo Bresenham + Simetría de 8 lados
    // ==========================================
    private void circunferenciaBresenham(int xc, int yc, int r, Color color) {
        int x = 0;
        int y = r;
        int d = 3 - 2 * r;          // decisión inicial

        dibujarPuntosSimetricos(xc, yc, x, y, color);

        while (x < y) {
            if (d < 0) {
                // Solo avanza en x (píxel Este)
                d = d + 4 * x + 6;
            } else {
                // Avanza en x y baja en y (píxel Sureste)
                d = d + 4 * (x - y) + 10;
                y--;
            }
            x++;
            dibujarPuntosSimetricos(xc, yc, x, y, color);
        }
    }

    private void dibujarPuntosSimetricos(int xc, int yc, int x, int y, Color color){
        putPixel(xc + x, yc + y, color);
        putPixel(xc - x, yc + y, color);
        putPixel(xc + x, yc - y, color);
        putPixel(xc - x, yc - y, color);
        putPixel(xc + y, yc + x, color);
        putPixel(xc + y, yc - x, color);
        putPixel(xc - y, yc + x, color);
        putPixel(xc - y, yc - x, color);
    }

    private void dibujarCirculo(int xc, int yc, int r, Color color) {
        if (polar) {
            circunferenciaPolar(xc, yc, r, color);
        } else if (puntoMedio) {
            circunferenciaPuntoMedio(xc, yc, r, color);
        } else if (bresenham) {
            circunferenciaBresenham(xc, yc, r, color);
        } else {
            circunferencia(xc, yc, r, color);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);

        dibujarCirculo(200, 150, 100, Color.BLUE);
        dibujarCirculo(200, 150, 80, Color.BLACK);
        dibujarCirculo(200, 150, 60, Color.RED);
    }

    public static void main(String[] args) {
        Circunferencia frame = new Circunferencia(false, false, false);
        frame.setTitle("Circunferencia");
        frame.setSize(400, 400);
        frame.setLocation(50,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Circunferencia frame2 = new Circunferencia(true, false, false);
        frame2.setTitle("Circunferencia - Coordenadas polares");
        frame2.setSize(400, 400);
        frame2.setLocation(470,100);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setVisible(true);

        Circunferencia frame3 = new Circunferencia(false, true, false);
        frame3.setTitle("Circunferencia - Algoritmo Punto Medio");
        frame3.setSize(400, 400);
        frame3.setLocation(890,100);
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame3.setVisible(true);

        Circunferencia frame4 = new Circunferencia(false, false, true);
        frame4.setTitle("Circunferencia - Algoritmo Bresenham");
        frame4.setSize(400, 400);
        frame4.setLocation(1310,100);
        frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame4.setVisible(true);
    }
}
