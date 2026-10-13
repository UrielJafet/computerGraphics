package Pastel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class GraficaPastel extends JPanel {

    private int[] values;
    private Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.YELLOW};

    public GraficaPastel(int[] values){
        JFrame frame = new JFrame("Gráfica de pastel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.add(this);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.values = values;
    }

    @Override 
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        int total = 0;
        for(int valor : values){
            total += valor;
        }
        int startAngle = 0;

        // Definir las dimensiones del gráfico de pastel
        int x = (getWidth() - 250) / 2; // Coordenada x del rectángulo que contiene el gráfico
        int y = (getHeight() - 250) / 2; // Coordenada y del rectángulo que contiene el gráfico
        int diameter = 250; // Diámetro del gráfico de pastel

        for(int i = 0; i < values.length; i++){
            int arcAngle = (int) Math.round((double) values[i] / total * 360);
            g.setColor(colors[i % colors.length]);
            g.fillArc(x, y, diameter, diameter, startAngle, arcAngle);
            startAngle += arcAngle;
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Por favor, ingrese los valores como argumentos.");
            return;
        }  
        try{
            int[] values = new int[args.length];
            for (int i = 0; i < args.length; i++) {
                values[i] = Integer.parseInt(args[i]);
            }
            new GraficaPastel(values);
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese solo números enteros como argumentos.");
        }
    }
}
