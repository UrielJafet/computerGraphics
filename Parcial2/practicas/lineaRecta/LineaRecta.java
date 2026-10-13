package lineaRecta;

import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Color;

/**
 * Dibujo de líneas rectas pixel por pixel utilizando únicamente
 * la ecuación de la recta: y = mx + b
 *
 * Se abren dos ventanas con las mismas líneas de prueba:
 *   1) Versión BÁSICA: fórmula pura, sin correcciones.
 *   2) Versión MEJORADA: corrige las deficiencias encontradas.
 */
public class LineaRecta extends JFrame {

    private static final long serialVersionUID = 1L;

    private BufferedImage buffer;
    private boolean mejorada = true;

    public LineaRecta(boolean mejorada) {

        this.mejorada = mejorada;
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
    // Las coordenadas (x, y) son relativas al área
    // útil de la ventana (debajo de la barra de título
    // y dentro de los bordes), no a la esquina del JFrame.

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
    // VERSIÓN BÁSICA: y = mx + b
    // ==========================================

    public void dibujarLineaBasica(
            int x0,
            int y0,
            int x1,
            int y1,
            Color color) {

        /*Calcular la pendiente:
        *
        *       y1 - y0
        *   m = ---------
        *       x1 - x0
        */
        double m = (double) (y1 - y0) / (x1 - x0);

        /* Calcular la intersección con el eje Y:
         *
         *  b = y0 - mx0
         */
        double b = y0 - (m * x0);

        /* Recorrer cada posición X y calcular Y
        * utilizando la ecuación:
        *
        * y = mx + b
        */
        for (int x = x0; x <= x1; x++) {

            double y = (m * x) + b;

            // Como las coordenadas de pantalla
            // deben ser enteras, redondeamos Y.
            int yPixel = (int) Math.round(y);

            putPixel(x, yPixel, color);
        }
    }

    // ==========================================
    // VERSIÓN MEJORADA: y = mx + b
    // ==========================================

    public void dibujarLineaMejorada(
            int x0,
            int y0,
            int x1,
            int y1,
            Color color) {
        
        // Línea vertical (pendiente no existe)

        if (x0 == x1) {

            int inicioY = Math.min(y0, y1);
            int finY = Math.max(y0, y1);

            for (int y = inicioY; y <= finY; y++) {
                putPixel(x0, y, color);
            }
            return;
        }

        // Si la línea va de derecha a izquierda,
        // intercambiamos los puntos para recorrer x de menor a mayor.
        if (x0 > x1) {

            int tempX = x0;
            x0 = x1;
            x1 = tempX;

            int tempY = y0;
            y0 = y1;
            y1 = tempY;
        }

        double m = (double) (y1 - y0) / (x1 - x0);
        double b = y0 - (m * x0);

        if (Math.abs(m) <= 1) {
            // Línea "suave" (|m| <= 1):
            // se recorre X y se calcula Y = mx + b
            for (int x = x0; x <= x1; x++) {
                double y = (m * x) + b;
                putPixel(x, (int) Math.round(y), color);
            }
        } else {
            // Línea "empinada" (|m| > 1).
            // Se recorre Y y se despeja X de la misma ecuación:
            //
            //       y - b
            // x = ---------
            //         m
            //
            int inicioY = Math.min(y0, y1);
            int finY = Math.max(y0, y1);

            for (int y = inicioY; y <= finY; y++) {
                double x = (y - b) / m;
                putPixel((int) Math.round(x), y, color);
            }
        }
    }


    private void dibujarLinea(int x0, int y0, int x1, int y1, Color color) {
        if (mejorada) {
            dibujarLineaMejorada(x0, y0, x1, y1, color);
        } else {
            dibujarLineaBasica(x0, y0, x1, y1, color);
        }
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        // 1) Pendiente suave positiva (funciona en ambas)
        dibujarLinea(50, 50, 300, 200, Color.GREEN);

        // 2) Pendiente suave negativa (funciona en ambas)
        dibujarLinea(50, 300, 300, 220, Color.BLUE);

        // 3) Horizontal (funciona en ambas, m = 0)
        dibujarLinea(50, 30, 300, 30, Color.BLACK);

        // 4) Vertical (la básica falla)
        dibujarLinea(30, 50, 30, 300, Color.ORANGE);

        // 5) Derecha a izquierda (la básica no dibuja nada)
        dibujarLinea(300, 340, 60, 320, Color.MAGENTA);

        // 6) Empinada |m| > 1 (la básica deja huecos)
        dibujarLinea(320, 60, 360, 300, Color.RED);
    }

    // ==========================================
    // PROGRAMA PRINCIPAL
    // ==========================================

    public static void main(String[] args) {

        LineaRecta basica = new LineaRecta(false);

        basica.setTitle("Básica - y = mx + b");
        basica.setSize(400, 400);
        basica.setLocation(50,100);
        basica.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        basica.setVisible(true);

        LineaRecta mejorada = new LineaRecta(true);
        mejorada.setTitle("Mejorada - y = mx + b");
        mejorada.setSize(400, 400);
        mejorada.setLocation(470,100);
        mejorada.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mejorada.setVisible(true);
    }
}