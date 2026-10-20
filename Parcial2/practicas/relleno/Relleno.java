package relleno;

import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ArrayDeque;

public class Relleno extends JFrame {

    private static final long serialVersionUID = 1L;

    private BufferedImage buffer;
    private boolean usarFloodFill;
    private boolean yaDibujado = false;   // para dibujar solo una vez

    public Relleno(boolean usarFloodFill) {
        this.usarFloodFill = usarFloodFill;
        setSize(500, 500);
    }

    // ==========================================
    // CREAR BUFFER 
    // ==========================================
    private void crearBuffer() {
        if (getWidth() <= 0 || getHeight() <= 0) return;

        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        // Llenar de blanco píxel por píxel
        int blanco = Color.WHITE.getRGB();
        for (int y = 0; y < buffer.getHeight(); y++) {
            for (int x = 0; x < buffer.getWidth(); x++) {
                buffer.setRGB(x, y, blanco);
            }
        }
    }

    // ==========================================
    // PUT PIXEL
    // ==========================================
    public void putPixel(int x, int y, Color color) {
        if (buffer == null) return;
        if (x < 0 || y < 0 || x >= buffer.getWidth() || y >= buffer.getHeight()) return;
        buffer.setRGB(x, y, color.getRGB());
    }

    // ==========================================
    // GET COLOR (devuelve int para mayor eficiencia)
    // ==========================================
    public int getRGB(int x, int y) {
        if (buffer == null) return Color.WHITE.getRGB();
        if (x < 0 || y < 0 || x >= buffer.getWidth() || y >= buffer.getHeight()) {
            return Color.WHITE.getRGB();
        }
        return buffer.getRGB(x, y);
    }

    // ==========================================
    // ALGORITMO DE LÍNEA (Punto Medio)
    // ==========================================
    private void algoritmoPuntoMedio(int x0, int y0, int x1, int y1, Color color) {
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

        putPixel(x, y, color);

        for (int i = 0; i < dx; i++) {
            if (d > 0) {
                d += incrNE;
                if (empinada) x += sx;
                else y += sy;
            } else {
                d += incrE;
            }

            if (empinada) y += sy;
            else x += sx;

            putPixel(x, y, color);
        }
    }

    private void dibujarLinea(int x0, int y0, int x1, int y1, Color color) {
        algoritmoPuntoMedio(x0, y0, x1, y1, color);
    }

    // ==========================================
    // DIBUJAR POLÍGONO
    // ==========================================
    private void dibujarPoligono(int[][] vertices, Color color) {
        int n = vertices.length;
        for (int i = 0; i < n; i++) {
            int x0 = vertices[i][0];
            int y0 = vertices[i][1];
            int x1 = vertices[(i + 1) % n][0];
            int y1 = vertices[(i + 1) % n][1];
            dibujarLinea(x0, y0, x1, y1, color);
        }
    }

    // ==========================================
    // FLOOD FILL 
    // ==========================================
    public void floodFill(int x, int y, Color colorRelleno, Color colorBorde) {
        int rgbRelleno = colorRelleno.getRGB();
        int rgbBorde = colorBorde.getRGB();

        ArrayDeque<int[]> cola = new ArrayDeque<>();
        cola.add(new int[]{x, y}); // 1. Punto semilla

        while (!cola.isEmpty()) {
            int[] p = cola.poll(); // 2. Sacar un píxel
            int px = p[0];
            int py = p[1];

            // 3. Si está fuera de la imagen, se ignora
            if (px < 0 || py < 0 || px >= buffer.getWidth() || py >= buffer.getHeight()) continue;

            int actual = getRGB(px, py); // 4. Leer color del píxel

            // 5. Si es borde o ya está rellenado → no hacer nada
            if (actual == rgbBorde || actual == rgbRelleno) continue;

            // 6. Pintar el píxel
            buffer.setRGB(px, py, rgbRelleno);

            // 7. Agregar los 4 vecinos (4-conectado)
            cola.add(new int[]{px + 1, py});  // derecha
            cola.add(new int[]{px - 1, py});  // izquierda
            cola.add(new int[]{px, py + 1});  // abajo
            cola.add(new int[]{px, py - 1});  // arriba
        }
    }

    // ==========================================
    // SCAN-LINE FILL 
    // ==========================================
    public void scanLineFillPoligono(int[][] vertices, Color color) {
        int n = vertices.length;
        int rgb = color.getRGB();

        // 1. Encontrar la fila más alta y más baja del polígono
        int ymin = vertices[0][1];
        int ymax = vertices[0][1];
        for (int i = 1; i < n; i++) {
            ymin = Math.min(ymin, vertices[i][1]);
            ymax = Math.max(ymax, vertices[i][1]);
        }

        // 2. Recorrer cada fila horizontal (scan-line)
        for (int y = ymin; y <= ymax; y++) {

            List<Integer> intersecciones = new ArrayList<>();

            // 3. Buscar dónde esta fila cruza las aristas del polígono
            for (int i = 0; i < n; i++) {
                int x0 = vertices[i][0];
                int y0 = vertices[i][1];
                int x1 = vertices[(i + 1) % n][0];
                int y1 = vertices[(i + 1) % n][1];

                // ¿La arista cruza esta y?
                if ((y0 <= y && y1 > y) || (y1 <= y && y0 > y)) {
                    // Calcular la X exacta de cruce
                    double xIntersect = x0 + (double)(y - y0) * (x1 - x0) / (y1 - y0);
                    intersecciones.add((int) Math.round(xIntersect));
                }
            }

            // 4. Ordenar las intersecciones de izquierda a derecha
            Collections.sort(intersecciones);

            // 5. Rellenar de dos en dos
            for (int i = 0; i < intersecciones.size() - 1; i += 2) {
                int xStart = intersecciones.get(i);
                int xEnd   = intersecciones.get(i + 1);

                for (int x = xStart; x <= xEnd; x++) {
                    buffer.setRGB(x, y, rgb);   // pintar el segmento
                }
            }
        }
    }

    // ==========================================
    // PAINT
    // ==========================================
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (buffer == null) {
            crearBuffer();
        }

        // Dibujar solo una vez
        if (!yaDibujado) {
            int[][] poligono = {
                {200, 80},
                {250, 160},
                {350, 160},
                {270, 230},
                {300, 330},
                {200, 270},
                {100, 330},
                {130, 230},
                {50, 160},
                {150, 160}
            };

            // Ajuste por insets (barra de título)
            int top = getInsets().top;
            int left = getInsets().left;

            // Desplazar el polígono para que no quede debajo de la barra de título
            for (int i = 0; i < poligono.length; i++) {
                poligono[i][0] += left;
                poligono[i][1] += top + 10;
            }

            if (usarFloodFill) {
                dibujarPoligono(poligono, Color.BLUE);
                floodFill(220, 200, Color.CYAN, Color.BLUE);
            } else {
                scanLineFillPoligono(poligono, Color.ORANGE);
                dibujarPoligono(poligono, Color.BLACK);
            }

            yaDibujado = true;
        }

        g.drawImage(buffer, 0, 0, this);
    }

    // ==========================================
    // MAIN
    // ==========================================
    public static void main(String[] args) {
        Relleno scan = new Relleno(false);
        scan.setTitle("Relleno Scan-Line");
        scan.setSize(500, 500);
        scan.setLocation(40, 60);
        scan.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scan.setVisible(true);

        Relleno flood = new Relleno(true);
        flood.setTitle("Relleno Flood-Fill");
        flood.setSize(500, 500);
        flood.setLocation(560, 60);
        flood.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        flood.setVisible(true);
    }
}