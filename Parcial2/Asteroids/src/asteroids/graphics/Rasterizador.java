package asteroids.graphics;

import asteroids.core.Canvas;

public class Rasterizador {
    
    private static final int BITS = 16;
    private static final int MASCARA_CONTINUA =0xFFFF;
    private int[] cruces = new int[64];   // reutilizable: nunca más de n cruces por fila

    private final Canvas canvas;

    public Rasterizador(Canvas canvas){
        this.canvas = canvas;
    }

    public void rellenarPoligono(int[] xs, int[] ys, int color){
        int n = xs.length;
        if (cruces.length < n) cruces = new int[n];
        // 1. Encontrar ymin y ymax
        int ymin = ys[0];
        int ymax = ys[0];
        for (int i = 1; i < n; i++) {
            if (ys[i] < ymin) ymin = ys[i];
            if (ys[i] > ymax) ymax = ys[i];
        }

        // 2. Recortar al área del canvas
        int alto = canvas.getAlto();
        if (ymin < 0) ymin = 0;
        if (ymax >= alto) ymax = alto - 1;

        // 3. Recorrer cada scan-line
        for (int y = ymin; y <= ymax; y++) {
            int k = 0;

            // Buscar intersecciones con las aristas
            for (int i = 0; i < n; i++) {
                int j = (i + 1) % n;

                int y0 = ys[i];
                int y1 = ys[j];
                int x0 = xs[i];
                int x1 = xs[j];

                // ¿La arista cruza esta fila y? (regla [yMin, yMax))
                if ((y0 <= y && y1 > y) || (y1 <= y && y0 > y)) {
                    // Calcular X con double + redondeo
                    double xIntersect = x0 + (double)(y - y0) * (x1 - x0) / (y1 - y0);
                    cruces[k++] = (int) Math.round(xIntersect);
                }
            }

            // 4. Ordenar las intersecciones (inserción - son pocos elementos)
            for (int i = 1; i < k; i++) {
                int temp = cruces[i];
                int j = i - 1;
                while (j >= 0 && cruces[j] > temp) {
                    cruces[j + 1] = cruces[j];
                    j--;
                }
                cruces[j + 1] = temp;
            }

            // 5. Pintar entre pares de intersecciones
            for (int i = 0; i < k - 1; i += 2) {
                int xStart = cruces[i];
                int xEnd   = cruces[i + 1];

                // Opcional: recortar también en X
                if (xStart < 0) xStart = 0;
                if (xEnd >= canvas.getAncho()) xEnd = canvas.getAncho() - 1;

                for (int x = xStart; x <= xEnd; x++) {
                    canvas.putPixel(x, y, color);
                }
            }
        }
    }

    public void pixel(int x, int y, int color){
        canvas.putPixel(x, y, color);
    }

    // ==========================================
    // LÍNEA (punto medio)
    // ==========================================
    public void linea(int x0, int y0, int x1, int y1, int color) {
        linea(x0, y0, x1, y1, color, MASCARA_CONTINUA);
    }

    public void linea(int x0, int y0, int x1, int y1, int color, int mascara) {
        
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;
        
        // Si |m| > 1 el eje principal es Y
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
        
        if (bitActivo(mascara, contador)) {
            canvas.putPixel(x, y, color);
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
            
            if (bitActivo(mascara, contador)) {
                canvas.putPixel(x, y, color);
            }
            contador++;
        }
    }

    private boolean bitActivo(int mascara, int contador) {
        return ((mascara >> (BITS - 1 - (contador % BITS))) & 1) == 1;
    }
    
    // ==========================================
    // CIRCUNFERENCIA (punto medio)
    // ==========================================
    public void circunferencia(int xc, int yc, int r, int color) {
        
        int x = 0;
        int y = r;
        int d = 1 - r;
        
        puntosSimetricos(xc, yc, x, y, color);
        
        while (x < y) {
            if (d < 0) {
                d += 2 * x + 3;
            } else {
                d += 2 * (x - y) + 5;
                y--;
            }
            x++;
            
            puntosSimetricos(xc, yc, x, y, color);
        }
    }
    
    private void puntosSimetricos(int xc, int yc, int x, int y, int color) {
        canvas.putPixel(xc + x, yc + y, color);
        canvas.putPixel(xc - x, yc + y, color);
        canvas.putPixel(xc + x, yc - y, color);
        canvas.putPixel(xc - x, yc - y, color);
        canvas.putPixel(xc + y, yc + x, color);
        canvas.putPixel(xc + y, yc - x, color);
        canvas.putPixel(xc - y, yc + x, color);
        canvas.putPixel(xc - y, yc - x, color);
    }

    // ==========================================
    // POLÍGONO (contorno cerrado)
    // ==========================================
    public void poligono(int[] xs, int[] ys, int color){
        int n = xs.length;
        for(int i = 0; i < n; i++){
            int j = (i + 1) % n;
            linea(xs[i], ys[i], xs[j], ys[j], color);
        }
    }
}
