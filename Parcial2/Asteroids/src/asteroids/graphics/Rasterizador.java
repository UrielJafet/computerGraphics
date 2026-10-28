package asteroids.graphics;

import asteroids.core.Canvas;

public class Rasterizador {
    
    private static final int BITS = 16;
    private static final int MASCARA_CONTINUA =0xFFFF;

    private final Canvas canvas;

    public Rasterizador(Canvas canvas){
        this.canvas = canvas;
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
