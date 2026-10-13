package visor;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.Image;
import javax.swing.ImageIcon; // Importación de ImageIcon para cargar la imagen

public class VisorImagen extends JFrame {
    private JScrollPane panel;
    private Pantalla pantalla;

    public VisorImagen() {
        setTitle("Visor de Imagen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // ImageIcon carga la imagen de forma síncrona garantizando que no devuelva -1 en sus dimensiones
        Image img = new ImageIcon("./profile.jpeg").getImage(); 
        
        pantalla = new Pantalla(img);
        
        // Creamos el JScrollPane con barras de desplazamiento automáticas
        panel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // Agregamos la pantalla al scrollpane
        panel.setViewportView(pantalla);
        
        // Agregamos el scrollpane al JFrame
        getContentPane().add(panel);
        
        setSize(800, 600);
        setLocationRelativeTo(null); // Centra la ventana en pantalla
        setVisible(true);
    }

    public static void main(String[] args) {
        new VisorImagen();
    }
}
