package asteroids.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class KeyMap extends KeyAdapter{
    
    public volatile boolean left;
    public volatile boolean right;
    public volatile boolean push;
    public volatile boolean shot;

    @Override
    public void keyPressed(KeyEvent e){
        changeState(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e){
        changeState(e.getKeyCode(), false);
    }

    private void changeState(int cod, boolean pressed){
        switch(cod){
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                left = pressed;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                right = pressed;
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                push = pressed;
                break;
            case KeyEvent.VK_SPACE:
                shot = pressed;
                break;
            default:
                break;
        }
    }
}
