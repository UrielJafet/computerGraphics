package asteroids.core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.util.Arrays;

/**
 * Doble buffer.
 *
 * Se mantienen dos imágenes:
 *   - "atras":  donde el hilo del juego dibuja el siguiente cuadro.
 *   - "frente": la que se muestra en pantalla.
 * Cuando el cuadro está terminado se intercambian las referencias, así que
 * nunca se muestra un cuadro a medio dibujar (sin parpadeo).
 *
 * Los colores son enteros 0xRRGGBB.
 */
public class Canvas {
    
    private final int ancho;
    private final int alto;

    // Protege el intercambio y lectura de "frente"
    private final Object mutex = new Object();
    
    private BufferedImage frente;
    private BufferedImage atras;
    private int[] pixelesAtras;

    public Canvas(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        frente = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        atras = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        pixelesAtras = pixeles(atras);
    }

    // Arreglo de pixeles
    private static int[] pixeles(BufferedImage img) {
        return ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    }

    public int getAncho() {
        return ancho;
    }
    public int getAlto() {
        return alto;
    }
    // Pinta todo el buffer trasero
    public void limpiar(int color) {
        Arrays.fill(pixelesAtras, color);
    }

    // Único punto de dibujo: un pixel en el buffer trasero
    public void putPixel(int x, int y, int color){
        if(x < 0 || y < 0 || x >= ancho || y >= alto) return;
        pixelesAtras[y * ancho + x] = color;
    }

    // Cuadro terminado: se hace visible l buffer trasero
    public void intercambiar(){
        synchronized(mutex){
            BufferedImage temp = frente;
            frente = atras;
            atras = temp;
            pixelesAtras = pixeles(atras);
        }
    }
    // Se llama desde el hilo de la GUI para mostrar el buffer delantero en pantalla
    public void mostrar(Graphics g, ImageObserver obs){
        synchronized(mutex){
            g.drawImage(frente, 0, 0, obs);
        }
    }
}
