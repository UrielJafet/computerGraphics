package primitivas;

import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Color;

public class PanelDibujo extends JPanel {

    private static final long serialVersionUID = 1L;
    private BufferedImage pixelBuffer;
    private Graphics gPintura;

    public PanelDibujo() {
        pixelBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    }

    public void putPixel(int x, int y, Color color) {
        // Fuera de un repintado no hay dónde dibujar
        if (gPintura == null) {
            return;
        }

        Insets insets = getInsets();

        int pantallaX = x + insets.left;
        int pantallaY = y + insets.top;

        if (x < 0 || y < 0
                || pantallaX >= getWidth() - insets.right
                || pantallaY >= getHeight() - insets.bottom) {
            return;
        }

        pixelBuffer.setRGB(0, 0, color.getRGB());
        // Se dibuja en el Graphics del repintado, no en getGraphics():
        // JPanel usa doble búfer y lo que se pinta directo en pantalla
        // lo borra Swing al copiar el búfer cuando termina paint().
        gPintura.drawImage(pixelBuffer, pantallaX, pantallaY, this);
    }

    private void dibujarFiguras(){
        // Líneas
        // Diagonal hacia abajo a la derecha
        Dibujador.algoritmoPuntoMedio(this, 20, 30, 200, 180, Color.RED);
        // Horizontal izquierda a derecha
        Dibujador.algoritmoPuntoMedio(this, 220, 60, 380, 60, Color.BLACK);
        // Diagonal hacia arriba a la derecha
        Dibujador.algoritmoPuntoMedio(this, 560, 30, 380, 180, Color.BLUE);
        // Horizontal derecha a izquierda
        Dibujador.algoritmoPuntoMedio(this, 760, 70, 620, 70, Color.MAGENTA);

        // Circunferencias concéntricas
        int[] radios = {10, 35, 60, 90};
        for (int r : radios) {
            Dibujador.circunferenciaPuntoMedio(this, 110, 400, r, Color.BLACK);
        }

        // Rectángulos
        Dibujador.rectangulo(this, 270, 330, 520, 470, Color.BLACK);
        Dibujador.rectangulo(this, 480, 430, 310, 360, Color.RED);

        // Elipses
        int[][] radiosElipses = {
            {120, 80},   // la más grande
            {90, 60},
            {60, 40},
            {30, 20}     // la más pequeña
        };

        for (int[] r : radiosElipses) {
            Dibujador.elipsePuntoMedio(this, 660, 400, r[0], r[1], Color.BLUE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        gPintura = g;
        dibujarFiguras();
        gPintura = null;
    }
}