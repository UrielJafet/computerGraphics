package algoritmos;

import javax.swing.JFrame;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Color;

public class Algoritmos extends JFrame{
    
    private static final long serialVersionUID = 1L;

    private BufferedImage buffer;
    boolean bresenham = true;
    
    public Algoritmos(boolean bresenham) {

        this.bresenham = bresenham;
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
    // Algoritmo DDA
    // ==========================================
    public void algoritmoDDA(
            int x0,
            int y0,
            int x1,
            int y1,
            Color color) {
            
        int dX = x1 - x0;
        int dY = y1 - y0;


        int steps = Math.max(Math.abs(dX), Math.abs(dY));
        
        double xS = dX / (double) steps;
        double yS = dY / (double) steps;
        
        double x = x0;
        double y = y0;
        
        putPixel((int) Math.round(x), (int) Math.round(y), color);
        for (int k = 1; k <= steps; k++) {
            x += xS;
            y += yS;
            putPixel((int) Math.round(x), (int) Math.round(y), color);
        }
    }

    // ==========================================
    // Algoritmo de Bresenham
    // ==========================================
    private void algoritmoBresenham(
            int x0,
            int y0,
            int x1,
            int y1,
            Color color) {
        
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

        int dX = x1 - x0;
        int dY = y1 - y0;

        // Caso especial: línea vertical
        if (dX == 0) {
            int pasoY = (dY >= 0) ? 1 : -1;

            for (int y = y0; ; y += pasoY) {
                putPixel(x0, y, color);

                if (y == y1) {
                    break;
                }
            }

            return;
        }

        // Calcular la pendiente
        double m = (double) dY / dX;

        if( m >= 0 && m <= 1) {
            algoritmoBresenhamOctante1(x0, y0, x1, y1, color);
        } else if (m > 1) {
            algoritmoBresenhamOctante2(x0, y0, x1, y1, color);
        } else if (m < -1) {
            algoritmoBresenhamOctante7(x0, y0, x1, y1, color);
        } else{
            algoritmoBresenhamOctante8(x0, y0, x1, y1, color);
        }
    }

    private void algoritmoBresenhamOctante1(
            int x0,
            int y0,
            int x1,
            int y1,
            Color color) {

        int dX = x1 - x0;
        int dY = y1 - y0;

        int d = 2 * dY - dX;
        int incrE = 2 * dY;
        int incrNE = 2 * (dY - dX);

        putPixel(x0, y0, color);

        // Avanzamos siempre en x. Decidimos si mantenemos y o incrementamos y
        while (x0 < x1) {
            if (d <= 0) {
                d += incrE;
                x0++;
            } else {
                d += incrNE;
                x0++;
                y0++;
            }
            putPixel(x0, y0, color);
        }
    }

    private void algoritmoBresenhamOctante2(
            int x0,
            int y0,
            int x1,
            int y1,
            Color color) {

        int dX = x1 - x0;
        int dY = y1 - y0;

        int d = 2 * dX - dY;
        int incrS = 2 * dX;
        int incrSE = 2 * (dX - dY);

        putPixel(x0, y0, color);

        // Avanzamos siempre en y. Decidimos si mantenemos x o incrementamos también x
        while (y0 < y1) {
            if (d <= 0) {
                d += incrS;
                y0++;
            } else {
                d += incrSE;
                x0++;
                y0++;
            }
            putPixel(x0, y0, color);
        }
    }

    private void algoritmoBresenhamOctante7(
            int x0,
            int y0,
            int x1,
            int y1,
            Color color) {

        int dX = x1 - x0;
        int dY = y0 - y1; // Note: y0 - y1 because this octant is for negative slopes

        int d = 2 * dX - dY;
        int incrN = 2 * dX;
        int incrNE = 2 * (dX - dY);

        putPixel(x0, y0, color);
        
        // Avanzamos hacia arriba en y y decidimos si también incrementamos x

        while (y0 > y1) {
            if (d <= 0) {
                d += incrN;
                y0--;
            } else {
                d += incrNE;
                x0++;
                y0--;
            }
            putPixel(x0, y0, color);
        }
    }

    private void algoritmoBresenhamOctante8(
            int x0,
            int y0,
            int x1,
            int y1,
            Color color) {

        int dX = x1 - x0;
        int dY = y0 - y1; // Note: y0 - y1 because this octant is for negative slopes

        int d = 2 * dY - dX;
        int incrE = 2 * dY;
        int incrNE = 2 * (dY - dX);

        putPixel(x0, y0, color);

        // y1 es menor que y0, avanzamos hacia arriba en pantalla. El eje dominante sigue siendo x

        while (x0 < x1) {
            if (d <= 0) {
                d += incrE;
                x0++;
            } else {
                d += incrNE;
                x0++;
                y0--;
            }
            putPixel(x0, y0, color);
        }
    }

    private void dibujarLinea(int x0, int y0, int x1, int y1, Color color) {
        if (bresenham) {
            algoritmoBresenham(x0, y0, x1, y1, color);
        } else {
            algoritmoDDA(x0, y0, x1, y1, color);
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

        Algoritmos dda = new Algoritmos(false);

        dda.setTitle("Algoritmo DDA");
        dda.setSize(400, 400);
        dda.setLocation(50,100);
        dda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dda.setVisible(true);

        Algoritmos bresenham = new Algoritmos(true);
        bresenham.setTitle(" Algoritmo de Bresenham");
        bresenham.setSize(400, 400);
        bresenham.setLocation(470,100);
        bresenham.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bresenham.setVisible(true);
    }

}
