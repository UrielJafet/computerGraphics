package primitivas;

import java.awt.Color;

public class Dibujador {
    
    public static void algoritmoPuntoMedio(
        PanelDibujo panel,
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

        panel.putPixel(x, y, color);

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

            panel.putPixel(x, y, color);
        }
    }

    public static void circunferenciaPuntoMedio(PanelDibujo panel, int xc, int yc, int r, Color color) {
        int x = 0;
        int y = r;
        // Decisión inicial: d = 1 - r
        int d = 1 - r;

        // Dibujar los puntos iniciales (simetría)
        dibujarPuntosSimetricos(panel, xc, yc, x, y, color);

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
            dibujarPuntosSimetricos(panel, xc, yc, x, y, color);
        }
    }

    public static void dibujarPuntosSimetricos(PanelDibujo panel, int xc, int yc, int x, int y, Color color){
        panel.putPixel(xc + x, yc + y, color);
        panel.putPixel(xc - x, yc + y, color);
        panel.putPixel(xc + x, yc - y, color);
        panel.putPixel(xc - x, yc - y, color);
        panel.putPixel(xc + y, yc + x, color);
        panel.putPixel(xc + y, yc - x, color);
        panel.putPixel(xc - y, yc + x, color);
        panel.putPixel(xc - y, yc - x, color);
    }

    public static void rectangulo(PanelDibujo panel, int x0, int y0, int x1, int y1, Color color) {
        algoritmoPuntoMedio(panel, x0, y0, x1, y0, color); // lado superior
        algoritmoPuntoMedio(panel, x1, y0, x1, y1, color); // lado derecho
        algoritmoPuntoMedio(panel, x1, y1, x0, y1, color); // lado inferior
        algoritmoPuntoMedio(panel, x0, y1, x0, y0, color); // lado izquierdo
    }

    public static void elipsePuntoMedio(PanelDibujo panel, int xc, int yc, int a, int b, Color color) {
        int x = 0;
        int y = b;

        // ---------- REGIÓN 1 ----------
        // Decisión inicial región 1
        long d1 = b*b - a*a*b + (a*a)/4;

        dibujarPuntosSimetricosElipse(panel, xc, yc, x, y, color);

        // Mientras estemos en región 1 (pendiente >= -1)
        while (b*b*(x + 1) < a*a*(y - 0.5)) {
            if (d1 < 0) {
                // Solo avanza en x
                d1 += b*b*(2*x + 3);
            } else {
                // Avanza en x y baja en y
                d1 += b*b*(2*x + 3) + a*a*(-2*y + 2);
                y--;
            }
            x++;
            dibujarPuntosSimetricosElipse(panel, xc, yc, x, y, color);
        }

        // ---------- REGIÓN 2 ----------
        // Decisión inicial región 2
        double d2 = b*b*(x + 0.5)*(x + 0.5) + a*a*(y - 1)*(y - 1) - a*a*b*b;

        while (y > 0) {
            if (d2 < 0) {
                // Avanza en x y baja en y
                d2 += b*b*(2*x + 2) + a*a*(-2*y + 3);
                x++;
            } else {
                // Solo baja en y
                d2 += a*a*(-2*y + 3);
            }
            y--;
            dibujarPuntosSimetricosElipse(panel, xc, yc, x, y, color);
        }
    }

    private static void dibujarPuntosSimetricosElipse(PanelDibujo panel, int xc, int yc, int x, int y, Color color) {
        panel.putPixel(xc + x, yc + y, color);
        panel.putPixel(xc - x, yc + y, color);
        panel.putPixel(xc + x, yc - y, color);
        panel.putPixel(xc - x, yc - y, color);
    }
}
