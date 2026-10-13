package miniPaint;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MiniPaint extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

    private ButtonGroup modos;
    private JPanel area;
    private JLabel status;
    private Image buffer;
    private Image temporal;
    private boolean arrastrando = false;

    private static final int PUNTOS = 1;
    private static final int LINEAS = 2;
    private static final int RECTANGULOS = 3;
    private static final int CIRCULOS = 4;

    private int modo;
    private int x, y;

    public MiniPaint() {
        super("Mini Paint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem opcionNuevo = new JMenuItem("Nuevo", 'N');
        opcionNuevo.setActionCommand("nuevo");
        opcionNuevo.addActionListener(this);
        menuArchivo.add(opcionNuevo);

        menuArchivo.addSeparator();

        JMenuItem opcionSalir = new JMenuItem("Salir", 'S');
        opcionSalir.setActionCommand("salir");
        opcionSalir.addActionListener(this);
        menuArchivo.add(opcionSalir);
        menuBar.add(menuArchivo);

        modos = new ButtonGroup();
        JMenu menuModos = new JMenu("Modos");

        JRadioButtonMenuItem opcionPuntos = new JRadioButtonMenuItem("Puntos", true);
        opcionPuntos.setActionCommand("puntos");
        opcionPuntos.addActionListener(this);
        menuModos.add(opcionPuntos);
        modos.add(opcionPuntos);

        JRadioButtonMenuItem opcionLineas = new JRadioButtonMenuItem("Lineas");
        opcionLineas.setActionCommand("lineas");
        opcionLineas.addActionListener(this);
        menuModos.add(opcionLineas);
        modos.add(opcionLineas);

        JRadioButtonMenuItem opcionRectangulos = new JRadioButtonMenuItem("Rectangulos");
        opcionRectangulos.setActionCommand("rectangulos");
        opcionRectangulos.addActionListener(this);
        menuModos.add(opcionRectangulos);
        modos.add(opcionRectangulos);

        JRadioButtonMenuItem opcionCirculos = new JRadioButtonMenuItem("Circulos");
        opcionCirculos.setActionCommand("circulos");
        opcionCirculos.addActionListener(this);
        menuModos.add(opcionCirculos);
        modos.add(opcionCirculos);

        menuBar.add(menuModos);
        setJMenuBar(menuBar);

        // Mientras se arrastra el mouse, pinta "temporal" (el trazo en vivo);
        // el resto del tiempo pinta "buffer" (lo ya confirmado).
        area = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image aMostrar = (arrastrando && temporal != null) ? temporal : buffer;
                if (aMostrar != null) {
                    g.drawImage(aMostrar, 0, 0, this);
                }
            }
        };
        area.setBackground(Color.WHITE);
        area.addMouseListener(this);
        area.addMouseMotionListener(this);

        area.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (area.getWidth() > 0 && area.getHeight() > 0) {
                    Image nuevoBuffer = area.createImage(area.getWidth(), area.getHeight());
                    Graphics g = nuevoBuffer.getGraphics();
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, area.getWidth(), area.getHeight());
                    if (buffer != null) {
                        g.drawImage(buffer, 0, 0, area);
                    }
                    buffer = nuevoBuffer;
                }
            }
        });

        status = new JLabel("Status", JLabel.LEFT);

        getContentPane().add(area, BorderLayout.CENTER);
        getContentPane().add(status, BorderLayout.SOUTH);

        modo = PUNTOS;

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        switch (comando) {
            case "nuevo":
                if (buffer != null) {
                    Graphics g = buffer.getGraphics();
                    g.setColor(Color.WHITE);
                    g.fillRect(0, 0, area.getWidth(), area.getHeight());
                    area.repaint();
                }
                break;
            case "salir":
                if (JOptionPane.showConfirmDialog(this, "¿En verdad desea salir?",
                        "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    dispose();
                    System.exit(0);
                }
                break;
            case "puntos":
                modo = PUNTOS;
                break;
            case "lineas":
                modo = LINEAS;
                break;
            case "rectangulos":
                modo = RECTANGULOS;
                break;
            case "circulos":
                modo = CIRCULOS;
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        if (buffer == null) return;
        x = e.getX();
        y = e.getY();
        arrastrando = true;
        temporal = area.createImage(area.getWidth(), area.getHeight());
        temporal.getGraphics().drawImage(buffer, 0, 0, this);
        area.repaint(); // por si el modo es un punto y ya quieres verlo desde aquí
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (buffer != null && temporal != null) {
            buffer.getGraphics().drawImage(temporal, 0, 0, this);
        }
        arrastrando = false;
        area.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (temporal == null) return;
        Graphics g = temporal.getGraphics();
        g.setColor(Color.BLACK);
        switch (modo) {
            case PUNTOS:
                g.fillOval(e.getX(), e.getY(), 3, 3);
                break;
            case LINEAS:
                g.drawImage(buffer, 0, 0, area);
                g.setColor(Color.GREEN);
                g.drawLine(x, y, e.getX(), e.getY());
                break;
            case RECTANGULOS:
                g.drawImage(buffer, 0, 0, area);
                g.setColor(Color.BLUE);
                g.drawRect(Math.min(x, e.getX()), Math.min(y, e.getY()),
                        Math.abs(e.getX() - x), Math.abs(e.getY() - y));
                break;
            case CIRCULOS:
                g.drawImage(buffer, 0, 0, area);
                g.setColor(Color.RED);
                g.drawOval(Math.min(x, e.getX()), Math.min(y, e.getY()),
                        Math.abs(e.getX() - x), Math.abs(e.getY() - y));
                break;
        }
        area.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        status.setText("x=" + e.getX() + ", y=" + e.getY());
    }

    public static void main(String[] args) {
        new MiniPaint();
    }
}