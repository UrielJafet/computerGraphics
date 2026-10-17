package primitivas;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Color;

public class PanelDibujo extends JPanel {

    private static final long serialVersionUID = 1L;
    private BufferedImage pixelBuffer;

    public PanelDibujo() {
        setPreferredSize(new Dimension(800, 600));
        pixelBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    }

    public void putPixel(int x, int y, Color color) {
        Insets insets = getInsets();

        int pantallaX = x + insets.left;
        int pantallaY = y + insets.top;

        if (x < 0 || y < 0
                || pantallaX >= getWidth() - insets.right
                || pantallaY >= getHeight() - insets.bottom) {
            return;
        }

        pixelBuffer.setRGB(0, 0, color.getRGB());

        Graphics g = getGraphics();
        if (g != null) {
            g.drawImage(pixelBuffer, pantallaX, pantallaY, this);
            g.dispose();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Llamamos al algoritmo pasando ESTE panel
        Dibujador.algoritmoPuntoMedio(this, 50, 50, 300, 200, Color.RED);
    }
}