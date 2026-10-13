package reloj;

import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
//import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class PanelReloj extends JPanel{

    private BufferedImage buffer;
    private Graphics2D gBuffer;
    private BufferedImage bufferEstatico;   // clara + números + yema + anillo
    //private Path2D siluetaClara;            // para no recalcularla siempre

    // Ángulos actuales (los actualiza el hilo)
    private double anguloSegundos = 0;
    private double anguloMinutos = 0;
    private double anguloHoras = 0;

    HiloAnimacion hilo = new HiloAnimacion(this);

    public PanelReloj(){
        setPreferredSize(new Dimension(400, 400));
        setOpaque(false);
        setDoubleBuffered(true); 
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                crearBuffer();
                dibujarEnBuffer();
                repaint();
            }
        });
        hilo.start();
    }

    private void crearBuffer(){
        if (getWidth() <= 0 || getHeight() <= 0) return;

        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        gBuffer = buffer.createGraphics();
        gBuffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gBuffer.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // También creamos el buffer estático
        bufferEstatico = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gEst = bufferEstatico.createGraphics();
        gEst.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gEst.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Limpiar transparente
        gEst.setComposite(AlphaComposite.Clear);
        gEst.fillRect(0, 0, getWidth(), getHeight());
        gEst.setComposite(AlphaComposite.SrcOver);

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;
        int radioClara = Math.min(getWidth(), getHeight()) / 2 - 30;
        int radioYema  = radioClara / 3;
        int radioNumeros = (int) (radioClara * 0.65);

        // 1. Dibujar la clara (forma del reloj)
        dibujarClara(gEst, centroX, centroY, radioClara);

        // 2. Dibujar marcas
        dibujarMarcasPunto(gEst, centroX, centroY, radioNumeros - 20);
        // 2.1 Dibujar números
        dibujarNumeros(gEst, centroX, centroY, radioNumeros);

        // 3. Dibujar la yema
        dibujarYema(gEst, centroX, centroY, radioYema);

        gEst.dispose();
    }

    // Método que dibuja todo en el buffer
    private void dibujarEnBuffer() {
        if (buffer == null || bufferEstatico == null) return;

        // Limpiar el buffer (transparente)
        gBuffer.setComposite(AlphaComposite.Clear);
        gBuffer.fillRect(0, 0, getWidth(), getHeight());
        gBuffer.setComposite(AlphaComposite.SrcOver);
        gBuffer.drawImage(bufferEstatico, 0, 0, null);

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;
        int radioClara = Math.min(getWidth(), getHeight()) / 2 - 30;

        // 4. Dibujar las manecillas (encima de todo)
        dibujarManecillas(gBuffer, centroX, centroY, radioClara);

        // 5. Dibujar Anillo central
        dibujarAnillo(gBuffer,centroX, centroY);
    }


    private void dibujarClara(Graphics2D g2, int centroX, int centroY, int radio) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Path2D clara = crearSiluetaHuevo(centroX, centroY, radio, 6, 0.14, 0.9);

        // sombra suave debajo
        g2.setColor(new Color(0, 0, 0, 25));
        Path2D sombra = new Path2D.Double(clara);
        sombra.transform(java.awt.geom.AffineTransform.getTranslateInstance(3, 5));
        g2.fill(sombra);

        // Color de la clara (blanco sucio / crema)
        g2.setColor(new Color(255, 252, 245));
        g2.fill(clara);

        // Contorno suave
        g2.setColor(new Color(230, 220, 200));
        g2.setStroke(new BasicStroke(2.5f));
        g2.draw(clara);
    }

    /**
     * Genera una silueta tipo "huevo frito" (blob con lóbulos suaves)
     * usando puntos sobre un círculo con variación senoidal, unidos con
     * una spline Catmull-Rom convertida a curvas Bézier (sin picos).
     *
     * @param centroX     centro X
     * @param centroY     centro Y
     * @param radioBase   radio promedio de la forma
     * @param lobulos     cantidad de "bultos" grandes (5-7 se ve bien)
     * @param amplitud    qué tan pronunciados son los bultos (0.08 - 0.20)
     * @param escalaX     estira la forma horizontalmente (1.0 = circular)
     */
    private Path2D crearSiluetaHuevo(int centroX, int centroY, int radioBase,
                                    int lobulos, double amplitud, double escalaX) {
        int n = 60; // puntos base, luego se suavizan con la spline
        Point2D[] pts = new Point2D[n];

        for (int i = 0; i < n; i++) {
            double angulo = 2 * Math.PI * i / n;
            // variación principal (lóbulos) + una segunda armónica para romper simetría perfecta
            double r = radioBase * (1
                    + amplitud * Math.sin(lobulos * angulo)
                    + amplitud * 0.35 * Math.sin(2 * angulo + 1.3));
            double x = centroX + escalaX * r * Math.cos(angulo);
            double y = centroY + r * Math.sin(angulo);
            pts[i] = new Point2D.Double(x, y);
        }

        return catmullRomCerrado(pts);
    }

    // Convierte una lista de puntos (cerrada) en un Path2D suave usando Catmull-Rom -> Bézier
    private Path2D catmullRomCerrado(Point2D[] pts) {
        int n = pts.length;
        Path2D path = new Path2D.Double();
        path.moveTo(pts[0].getX(), pts[0].getY());

        for (int i = 0; i < n; i++) {
            Point2D p0 = pts[(i - 1 + n) % n];
            Point2D p1 = pts[i];
            Point2D p2 = pts[(i + 1) % n];
            Point2D p3 = pts[(i + 2) % n];

            double c1x = p1.getX() + (p2.getX() - p0.getX()) / 6.0;
            double c1y = p1.getY() + (p2.getY() - p0.getY()) / 6.0;
            double c2x = p2.getX() - (p3.getX() - p1.getX()) / 6.0;
            double c2y = p2.getY() - (p3.getY() - p1.getY()) / 6.0;

            path.curveTo(c1x, c1y, c2x, c2y, p2.getX(), p2.getY());
        }

        path.closePath();
        return path;
    }

    private void dibujarMarcasPunto(Graphics2D g2, int centroX, int centroY, int radio) {
        for (int i = 0; i < 60; i++) {
            double angulo = Math.toRadians(i * 6 - 90);

            boolean esHora = (i % 5 == 0);
            int tamano = esHora ? 6 : 3;

            int x = centroX + (int) (radio * Math.cos(angulo));
            int y = centroY + (int) (radio * Math.sin(angulo));

            g2.setColor(esHora ? Color.BLACK : new Color(150, 150, 150));
            g2.fillOval(x - tamano / 2, y - tamano / 2, tamano, tamano);
        }
    }

    private void dibujarNumeros(Graphics2D g2, int centroX, int centroY, int radio){
        g2.setColor(new Color(64, 64, 64));
        g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        FontMetrics fm = g2.getFontMetrics();

        for (int numero = 1; numero <= 12; numero++) {
            // el 12 está arriba (-90°), y cada número avanza 30°
            double angulo = Math.toRadians(numero * 30 - 90);

            int x = centroX + (int) (radio * Math.cos(angulo));
            int y = centroY + (int) (radio * Math.sin(angulo));

            String texto = String.valueOf(numero);
            int anchoTexto = fm.stringWidth(texto);
            int altoTexto = fm.getAscent();

            g2.drawString(texto, x - anchoTexto / 2, y + altoTexto / 2);
        }
    }

    private void dibujarYema(Graphics2D g2, int centroX, int centroY, int radioYema) {
        // Yema
        g2.setColor(new Color(255, 160, 0));          // naranja-amarillo
        g2.fillOval(centroX - radioYema, centroY - radioYema, 
                    radioYema * 2, radioYema * 2);

        // Brillo de la yema
        g2.setColor(new Color(255, 200, 80, 180));
        g2.fillOval(centroX - radioYema/2, centroY - radioYema/2 - 8, 
                    radioYema, radioYema);
    }

    private void dibujarManecillas(Graphics2D g2, int centroX, int centroY, int radioClara) {
        int largoHoras    = (int) (radioClara * 0.45);
        int largoMinutos  = (int) (radioClara * 0.55);
        int largoSegundos = (int) (radioClara * 0.65);

        dibujarManecilla(g2, centroX, centroY, anguloHoras, largoHoras, 7);
        dibujarManecilla(g2, centroX, centroY, anguloMinutos, largoMinutos, 5);
        dibujarManecilla(g2, centroX, centroY, anguloSegundos, largoSegundos, 2);
    }

    private void dibujarManecilla(Graphics2D g2, int centroX, int centroY,
                                double anguloGrados, int largo, float grosor) {
        // -90 para que el ángulo 0 apunte hacia ARRIBA, como en un reloj real
        double rad = Math.toRadians(anguloGrados - 90);
        int x = centroX + (int) (largo * Math.cos(rad));
        int y = centroY + (int) (largo * Math.sin(rad));

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(grosor, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        g2.drawLine(centroX, centroY, x, y);
    }

    private void dibujarAnillo(Graphics2D g2, int centroX, int centroY){
        // círculo relleno pequeño (tapa el nacimiento de las manecillas)
        g2.setColor(new Color(60, 60, 60));
        g2.fillOval(centroX - 6, centroY - 6, 12, 12);

        // aro dorado alrededor, como un tornillo
        g2.setColor(new Color(200, 160, 60));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(centroX - 6, centroY - 6, 12, 12);
    }

    public void actualizarAngulos(double segundos, double minutos, double horas) {
        this.anguloSegundos = segundos;
        this.anguloMinutos = minutos;
        this.anguloHoras = horas;
        
        dibujarEnBuffer();   // redibuja todo en el buffer
        repaint();           // pide que se muestre el buffer
    }

    // paintComponent SOLO copia el buffer a la pantalla
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Red de seguridad: si el panel se pinta antes de recibir su primer
        // resize (pasa a veces según el layout), creamos el buffer aquí también.
        if (buffer == null) {
            crearBuffer();
            dibujarEnBuffer();
        }

        if (buffer != null) {
            g.drawImage(buffer, 0, 0, null);
        }
    }
}