package recorte;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *   1) Líneas rectas con recorte EXPLÍCITO: se calculan con la ecuación
 *      de la recta las intersecciones con los bordes del área y solo se
 *      dibuja el tramo visible.
 *
 *   2) Circunferencias con recorte POR PUNTOS: cada píxel generado por el
 *      punto medio se compara con los límites del área antes de dibujarlo.
 */
public class Recorte extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final int ANCHO = 500;
    private static final int ALTO = 500;

    // Área de recorte (rectángulo)
    private static final int X_MIN = 150;
    private static final int Y_MIN = 150;
    private static final int X_MAX = 350;
    private static final int Y_MAX = 350;

    // Tolerancia para comparar números reales
    private static final double EPS = 1e-9;

    // true = dibuja también las figuras completas en gris, solo para comparar
    private static final boolean MOSTRAR_COMPLETAS = true;

    private final BufferedImage lienzo;

    public Recorte(String titulo, boolean lineas) {
        super(titulo);

        lienzo = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB);

        limpiarLienzo();

        if (lineas) {
            escenaLineas();
        } else {
            escenaCircunferencias();
        }

        add(new PanelLienzo());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ==========================================
    // PANEL QUE MUESTRA EL LIENZO
    // ==========================================
    private class PanelLienzo extends JPanel {

        private static final long serialVersionUID = 1L;

        PanelLienzo() {
            setPreferredSize(new Dimension(ANCHO, ALTO));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(lienzo, 0, 0, this);
        }
    }

    // ==========================================
    // PIXEL
    // ==========================================
    private void limpiarLienzo() {
        int blanco = Color.WHITE.getRGB();
        for (int y = 0; y < ALTO; y++) {
            for (int x = 0; x < ANCHO; x++) {
                lienzo.setRGB(x, y, blanco);
            }
        }
    }

    public void putPixel(int x, int y, Color color) {
        if (x < 0 || y < 0 || x >= ANCHO || y >= ALTO) {
            return;
        }
        lienzo.setRGB(x, y, color.getRGB());
    }

    private boolean dentro(int x, int y) {
        return x >= X_MIN && x <= X_MAX && y >= Y_MIN && y <= Y_MAX;
    }

    // ==========================================
    // LÍNEA (punto medio)
    // ==========================================
    private void lineaPuntoMedio(int x0, int y0, int x1, int y1, Color color) {

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

            putPixel(x, y, color);
        }
    }

    private void dibujarRectangulo(int xMin, int yMin, int xMax, int yMax, Color color) {
        lineaPuntoMedio(xMin, yMin, xMax, yMin, color);
        lineaPuntoMedio(xMax, yMin, xMax, yMax, color);
        lineaPuntoMedio(xMax, yMax, xMin, yMax, color);
        lineaPuntoMedio(xMin, yMax, xMin, yMin, color);
    }

    // ==========================================
    // RECORTE EXPLÍCITO DE LÍNEAS
    // ==========================================
    /**
     * Segmento P(t) = P0 + t * (P1 - P0), con t entre 0 y 1.
     * Se reúnen los valores de t de todos los puntos "candidatos":
     *   - los extremos que ya están dentro del área (t = 0 y/o t = 1)
     *   - las intersecciones con los bordes del rectángulo
     * El tramo visible va del menor al mayor de esos valores de t.
     * Si no hay candidatos, el segmento no toca el área y no se dibuja.
     */
    private void lineaRecortadaExplicita(int x0, int y0, int x1, int y1, Color color) {

        double dx = x1 - x0;
        double dy = y1 - y0;

        List<Double> ts = new ArrayList<>();

        // 1) Extremos dentro del área
        if (dentro(x0, y0)) {
            ts.add(0.0);
        }
        if (dentro(x1, y1)) {
            ts.add(1.0);
        }

        // 2) Bordes verticales: x = X_MIN y x = X_MAX
        //    t = (xb - x0) / dx  y  y = y0 + t * dy  (entre Y_MIN y Y_MAX)
        if (dx != 0) {
            int[] bordesX = {X_MIN, X_MAX};

            for (int xb : bordesX) {
                double t = (xb - x0) / dx;

                if (t >= -EPS && t <= 1 + EPS) {
                    double y = y0 + t * dy;

                    if (y >= Y_MIN - EPS && y <= Y_MAX + EPS) {
                        ts.add(t);
                    }
                }
            }
        }

        // 3) Bordes horizontales: y = Y_MIN y y = Y_MAX
        //    t = (yb - y0) / dy  y  x = x0 + t * dx  (entre X_MIN y X_MAX)
        if (dy != 0) {
            int[] bordesY = {Y_MIN, Y_MAX};

            for (int yb : bordesY) {
                double t = (yb - y0) / dy;

                if (t >= -EPS && t <= 1 + EPS) {
                    double x = x0 + t * dx;

                    if (x >= X_MIN - EPS && x <= X_MAX + EPS) {
                        ts.add(t);
                    }
                }
            }
        }

        // Nada visible
        if (ts.isEmpty()) {
            return;
        }

        double tMin = Collections.min(ts);
        double tMax = Collections.max(ts);

        int ax = (int) Math.round(x0 + tMin * dx);
        int ay = (int) Math.round(y0 + tMin * dy);
        int bx = (int) Math.round(x0 + tMax * dx);
        int by = (int) Math.round(y0 + tMax * dy);

        lineaPuntoMedio(ax, ay, bx, by, color);
    }

    // ==========================================
    // RECORTE POR PUNTOS DE CIRCUNFERENCIAS
    // ==========================================
    // Si "recortar" es true, el píxel solo se dibuja cuando está dentro del área
    private void pintar(int x, int y, Color color, boolean recortar) {
        if (!recortar || dentro(x, y)) {
            putPixel(x, y, color);
        }
    }

    private void circunferenciaPuntoMedio(
            int xc, int yc, int r,
            Color color,
            boolean recortar) {

        int x = 0;
        int y = r;

        // Decisión inicial: d = 1 - r
        int d = 1 - r;

        puntosSimetricos(xc, yc, x, y, color, recortar);

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

            puntosSimetricos(xc, yc, x, y, color, recortar);
        }
    }

    private void puntosSimetricos(int xc, int yc, int x, int y, Color color, boolean recortar) {
        pintar(xc + x, yc + y, color, recortar);
        pintar(xc - x, yc + y, color, recortar);
        pintar(xc + x, yc - y, color, recortar);
        pintar(xc - x, yc - y, color, recortar);
        pintar(xc + y, yc + x, color, recortar);
        pintar(xc + y, yc - x, color, recortar);
        pintar(xc - y, yc + x, color, recortar);
        pintar(xc - y, yc - x, color, recortar);
    }

    // ==========================================
    // ESCENAS DE PRUEBA
    // ==========================================
    private void escenaLineas() {

        int[][] lineas = {
            {180, 200, 320, 280},   // totalmente dentro
            {100, 250, 250, 300},   // entra por el borde izquierdo
            {100, 200, 400, 200},   // horizontal: cruza izquierdo y derecho
            {250, 100, 250, 400},   // vertical: cruza superior e inferior
            {100, 330, 330, 100},   // diagonal: cruza izquierdo y superior
            {300, 250, 330, 450},   // sale por el borde inferior
            {100, 180, 180, 100},   // no es descartada a simple vista, pero no toca el área
            {50, 60, 120, 100},     // totalmente fuera
            {100, 100, 400, 400}    // pasa exactamente por dos esquinas
        };

        Color[] colores = {
            Color.RED,
            Color.BLUE,
            new Color(0, 150, 0),
            Color.MAGENTA,
            Color.ORANGE,
            Color.CYAN.darker(),
            Color.DARK_GRAY,
            Color.PINK,
            new Color(128, 0, 128)
        };

        // Líneas completas en gris (solo para comparar)
        if (MOSTRAR_COMPLETAS) {
            for (int[] l : lineas) {
                lineaPuntoMedio(l[0], l[1], l[2], l[3], Color.LIGHT_GRAY);
            }
        }

        // Área de recorte
        dibujarRectangulo(X_MIN, Y_MIN, X_MAX, Y_MAX, Color.BLACK);

        // Líneas recortadas
        for (int i = 0; i < lineas.length; i++) {
            int[] l = lineas[i];
            lineaRecortadaExplicita(l[0], l[1], l[2], l[3], colores[i]);
        }
    }

    private void escenaCircunferencias() {

        int[][] circulos = {
            {250, 250, 50},    // totalmente dentro
            {150, 150, 80},    // centrada en una esquina
            {350, 200, 100},   // cruza el borde derecho
            {250, 400, 90},    // cruza el borde inferior
            {100, 300, 70},    // cruza el borde izquierdo
            {250, 250, 120},   // deja cuatro arcos, uno en cada esquina
            {440, 440, 40}     // totalmente fuera
        };

        Color[] colores = {
            Color.RED,
            Color.BLUE,
            new Color(0, 150, 0),
            Color.MAGENTA,
            Color.ORANGE,
            Color.CYAN.darker(),
            Color.PINK
        };

        // Circunferencias completas en gris (solo para comparar)
        if (MOSTRAR_COMPLETAS) {
            for (int[] c : circulos) {
                circunferenciaPuntoMedio(c[0], c[1], c[2], Color.LIGHT_GRAY, false);
            }
        }

        // Área de recorte
        dibujarRectangulo(X_MIN, Y_MIN, X_MAX, Y_MAX, Color.BLACK);

        // Circunferencias recortadas
        for (int i = 0; i < circulos.length; i++) {
            int[] c = circulos[i];
            circunferenciaPuntoMedio(c[0], c[1], c[2], colores[i], true);
        }
    }

    // ==========================================
    // PROGRAMA PRINCIPAL
    // ==========================================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                Recorte ventanaLineas = new Recorte("Recorte explícito de líneas", true);
                ventanaLineas.setLocation(20, 60);
                ventanaLineas.setVisible(true);

                Recorte ventanaCirculos = new Recorte("Recorte por puntos de circunferencias", false);
                ventanaCirculos.setLocation(540, 60);
                ventanaCirculos.setVisible(true);
            }
        });
    }
}