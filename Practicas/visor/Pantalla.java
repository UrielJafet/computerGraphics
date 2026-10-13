package visor;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;

public class Pantalla extends JPanel {
    private Image imagen;
    private boolean tamañoDefinido = false;

    public Pantalla(Image imagen) {
        this.imagen = imagen;
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g); // Despliega componentes base y limpia el fondo
        
        if (imagen != null) {
            // Dibujamos la imagen en el panel
            g.drawImage(imagen, 0, 0, this);
            
            // Obtenemos las dimensiones reales de la imagen
            int ancho = imagen.getWidth(this);
            int alto = imagen.getHeight(this);
            
            // Una vez cargada la imagen (dimensiones mayores a 0), ajustamos el panel una sola vez
            if (ancho > 0 && alto > 0 && !tamañoDefinido) {
                Dimension size = new Dimension(ancho, alto);
                setPreferredSize(size);
                setMinimumSize(size);
                setMaximumSize(size);
                setSize(size);
                tamañoDefinido = true;
                
                // Forzamos al JScrollPane a recalcular las barras de desplazamiento
                revalidate();
            }
        }
    }
}
