package asteroids.core;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import asteroids.input.KeyMap;

/*
* Solo muestra el buffer visible. No dibuja nada por su cuenta
*/

public class GamePanel extends JPanel{
    
    private static final long serialVersionUID = 1L;
    
    private final Canvas canvas;

    public GamePanel(Canvas canvas, KeyMap key){
        this.canvas = canvas;

        setPreferredSize(new Dimension(canvas.getAncho(), canvas.getAlto()));
        setFocusable(true);
        addKeyListener(key);
    }

    @Override 
    protected void paintComponent(Graphics g){
        canvas.mostrar(g, this);
    }

}
