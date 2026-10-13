package Ventana;

import javax.swing.JFrame;

public class Ventana extends JFrame{

    public Ventana(){
        setTitle("Ejemplo");
        setSize(200,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args){
        new Ventana();
    }
    
}
