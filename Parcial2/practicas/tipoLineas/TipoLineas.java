package tipoLineas;

import javax.swing.JFrame;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Color;


public class TipoLineas extends JFrame{
        private static final long serialVersionUID = 1L;

    private BufferedImage buffer;
    private boolean grueso = true;

    public TipoLineas(boolean grueso) {
        this.grueso = grueso;
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
        int x0, int y0,
        int x1, int y1,
        Color color,
        int mascara) {

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;

        boolean empinada = dy > dx;

        if (empinada) {
            int temp = dx;
            dx = dy;
            dy = temp;
        }

        int d = 2 * dy - dx;
        int incrE = 2 * dy;
        int incrNE = 2 * (dy - dx);

        int x = x0;
        int y = y0;

        int contador = 0;
        final int BITS = 16;

        // Dibujar el primer píxel
        if (((mascara >> (BITS - 1 - (contador % BITS))) & 1) == 1) {
            putPixel(x, y, color);
        }
        contador++;

        for (int i = 0; i < dx; i++) {
            if (d > 0) {
                d += incrNE;
                if (empinada) {
                    x += sx;
                } else {
                    y += sy;
                }
            } else {
                d += incrE;
            }

            if (empinada) {
                y += sy;
            } else {
                x += sx;
            }

            // ===== Máscara =====
            int bit = (mascara >> (BITS - 1 - (contador % BITS))) & 1;
            if (bit == 1) {
                putPixel(x, y, color);
            }
            contador++;
            // ===================
        }
    }

    public void circunferenciaPuntoMedio(int xc, int yc, int r, Color color, int mascara) {
        int x = 0;
        int y = r;
        // Decisión inicial: d = 1 - r
        int d = 1 - r;

        int contador = 0;
        final int BITS = 16;

        // Dibujar el primer píxel
        if (((mascara >> (BITS - 1 - (contador % BITS))) & 1) == 1) {
            // Dibujar los puntos iniciales (simetría)
            dibujarPuntosSimetricos( xc, yc, x, y, color);
        }
        contador++;
        

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

            // ===== Máscara =====
            int bit = (mascara >> (BITS - 1 - (contador % BITS))) & 1;
            if (bit == 1) {
                // Dibujar los 8 puntos simétricos
                dibujarPuntosSimetricos( xc, yc, x, y, color);
            }
            contador++;
            // ===================
            
        }
    }

    // ==========================================
    // DIBUJAR GROSOR
    // ==========================================

    void putPixelGrueso(int x, int y, int grosor, Color color) {
        int inicio = -(grosor - 1) / 2;
        for (int i = 0; i < grosor; i++) {
            for (int j = 0; j < grosor; j++) {
                putPixel(x + inicio + i, y + inicio + j, color);
            }
        }
    }

    private void lineaGrosor(
        int x0,
        int y0,
        int x1,
        int y1,
        Color color,
        int grosor) {

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;
        boolean empinada = dy > dx;

        if (empinada) {
            int temp = dx;
            dx = dy;
            dy = temp;
        }
        int d = 2 * dy - dx;
        int incrE = 2 * dy;
        int incrNE = 2 * (dy - dx);

        int x = x0;
        int y = y0;

        putPixelGrueso(x, y, grosor, color);

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

            putPixelGrueso(x, y, grosor, color);
        }
    }

    private void circunferenciaGrosor(int xc, int yc, int r, Color color, int grosor) {
        int x = 0;
        int y = r;
        // Decisión inicial: d = 1 - r
        int d = 1 - r;

        // Dibujar los puntos iniciales (simetría)
        dibujarPuntosSimetricosGrueso(xc, yc, x, y, color, grosor);

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
            dibujarPuntosSimetricosGrueso(xc, yc, x, y, color, grosor);
        }
    }

    public void dibujarPuntosSimetricosGrueso(int xc, int yc, int x, int y, Color color, int grosor){
        putPixelGrueso(xc + x, yc + y, grosor, color);
        putPixelGrueso(xc - x, yc + y, grosor, color);
        putPixelGrueso(xc + x, yc - y, grosor, color);
        putPixelGrueso(xc - x, yc - y, grosor, color);
        putPixelGrueso(xc + y, yc + x, grosor, color);
        putPixelGrueso(xc + y, yc - x, grosor, color);
        putPixelGrueso(xc - y, yc + x, grosor, color);
        putPixelGrueso(xc - y, yc - x, grosor, color);
    }
    public void dibujarPuntosSimetricos(int xc, int yc, int x, int y, Color color){
        putPixel(xc + x, yc + y, color);
        putPixel(xc - x, yc + y, color);
        putPixel(xc + x, yc - y, color);
        putPixel(xc - x, yc - y, color);
        putPixel(xc + y, yc + x, color);
        putPixel(xc + y, yc - x, color);
        putPixel(xc - y, yc + x, color);
        putPixel(xc - y, yc - x, color);
    }

    private void dibujarLinea(int x0, int y0, int x1, int y1, Color color, int mascara,int grosor) {
        if (grueso) {
            lineaGrosor(x0, y0, x1, y1, color, grosor);
        } else {
            algoritmoPuntoMedio(x0, y0, x1, y1, color, mascara);
        }
    }

    private void dibujarCircunferecia(int xc, int yc, int r, Color color, int mascara, int grosor) {
        if (grueso) {
            circunferenciaGrosor(xc, yc, r, color, grosor);
        } else {
            circunferenciaPuntoMedio(xc, yc, r, color, mascara);
        }
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        int continua     = 0b1111111111111111;  // línea normal
        int punteada     = 0b1010101010101010;  // punteada
        int discontinua  = 0b1111000011110000;  // discontinua
        int puntoRaya    = 0b1111010111101010;  // punto y raya

        if (grueso) {
            dibujarLinea(50, 30, 300, 30, Color.BLACK, continua, 5);
            dibujarLinea(50, 50, 300, 50, Color.BLUE, punteada, 4);
            dibujarLinea(50, 60, 300, 60, Color.ORANGE, discontinua, 7);
            dibujarLinea(50, 70, 300, 70, Color.BLACK, puntoRaya, 8);
        } else {
            dibujarLinea(50, 30, 300, 30, Color.BLACK, continua, 1);
            dibujarLinea(50, 50, 300, 50, Color.BLUE, punteada, 1);
            dibujarLinea(50, 60, 300, 60, Color.ORANGE, discontinua, 1);
            dibujarLinea(50, 70, 300, 70, Color.BLACK, puntoRaya, 1);
        }
        
        // Circunferencias concéntricas
        int[] radios = {10, 35, 60, 90};
        int[] mascaras = {continua, punteada, discontinua, puntoRaya};
        for (int i = 0; i < radios.length; i++) {
            dibujarCircunferecia(150, 180, radios[i], Color.BLACK, mascaras[i], grueso ? 5 : 1);
        }
    }

    public static void main(String[] args) {

        TipoLineas frame = new TipoLineas(false);

        frame.setTitle("Máscara");
        frame.setSize(400, 400);
        frame.setLocation(50,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        TipoLineas frame2 = new TipoLineas(true);
        frame2.setTitle("Grosor");
        frame2.setSize(400, 400);
        frame2.setLocation(470,100);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setVisible(true);
    }
}
