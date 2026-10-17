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


}
