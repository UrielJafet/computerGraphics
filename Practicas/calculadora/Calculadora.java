package calculadora;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Calculadora extends JPanel implements ActionListener {

    private JTextField display;

    Calculadora(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets.set(5, 5, 5, 5); // Espaciado entre botones
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los botones se expanden horizontalmente

        gbc.weightx = 1; // Cada botón ocupa el mismo espacio horizontal
        gbc.weighty = 1; // Cada botón ocupa el mismo espacio vertical

        // Display
        gbc.gridwidth = 4; // El display ocupa 4 columnas
        gbc.gridx = 0;
        gbc.gridy = 0;
        display = new JTextField(15);
        display.setEditable(false);
        display.setText("");
        display.setHorizontalAlignment(JTextField.RIGHT); 
        add(display, gbc);

        // Botones
        String[] botones = {
            "c","/","*","-",
            "7","8","9","+",
            "4","5","6",
            "1","2","3","=",
            "0","."
        };

        for (String boton : botones) {
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.fill = GridBagConstraints.BOTH; // Se expande en ambas direcciones
            
             // Asignación explícita de coordenadas según el botón
            switch (boton) {
                // Fila 1 de botones
                case "c": gbc.gridx = 0; gbc.gridy = 1; break;
                case "/": gbc.gridx = 1; gbc.gridy = 1; break;
                case "*": gbc.gridx = 2; gbc.gridy = 1; break;
                case "-": gbc.gridx = 3; gbc.gridy = 1; break;

                // Fila 2 de botones
                case "7": gbc.gridx = 0; gbc.gridy = 2; break;
                case "8": gbc.gridx = 1; gbc.gridy = 2; break;
                case "9": gbc.gridx = 2; gbc.gridy = 2; break;
                case "+": 
                    gbc.gridx = 3; gbc.gridy = 2; 
                    gbc.gridheight = 2; // Ocupa 2 filas
                    gbc.fill = GridBagConstraints.BOTH; // Se expande en ambas direcciones
                    break;

                // Fila 3 de botones (La col 3 está ocupada por el "+" de arriba)
                case "4": gbc.gridx = 0; gbc.gridy = 3; break;
                case "5": gbc.gridx = 1; gbc.gridy = 3; break;
                case "6": gbc.gridx = 2; gbc.gridy = 3; break;

                // Fila 4 de botones
                case "1": gbc.gridx = 0; gbc.gridy = 4; break;
                case "2": gbc.gridx = 1; gbc.gridy = 4; break;
                case "3": gbc.gridx = 2; gbc.gridy = 4; break;
                case "=": 
                    gbc.gridx = 3; gbc.gridy = 4; 
                    gbc.gridheight = 2; // Ocupa 2 filas
                    gbc.fill = GridBagConstraints.BOTH; // Se expande en ambas direcciones
                    break;

                // Fila 5 de botones (La col 3 está ocupada por el "=" de arriba)
                case "0": 
                    gbc.gridx = 0; gbc.gridy = 5; 
                    gbc.gridwidth = 2; // Ocupa 2 columnas
                    break;
                case ".": gbc.gridx = 2; gbc.gridy = 5; break;
            }

            JButton btn = new JButton(boton);

            // Asociamos la clase Calculadora como receptora del clic de este botón
            btn.addActionListener(this); 
            add(btn, gbc);
        }
    }

    @Override 
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("c")) {
            // Lógica para limpiar el display
            display.setText(""); // Actualizar el display
        } else if (command.equals("=")) {
            // Lógica para calcular el resultado
            calcularResultado();
        } else {
            // Lógica para manejar números y operadores
            String currentText = display.getText(); 
            display.setText(currentText + command); 
        }
    }

    private void calcularResultado() {
    String expresion = display.getText();
    
    // Identificar qué operador está presente en la cadena
    String operador = "";
    if (expresion.contains("+")) operador = "+";
    else if (expresion.contains("-")) operador = "-";
    else if (expresion.contains("*")) operador = "*";
    else if (expresion.contains("/")) operador = "/";
    
    // Si no hay ningún operador, no hay nada que calcular
    if (operador.isEmpty()) {
        return;
    }
    
    try {
        // Dividir la cadena en dos partes usando el operador como separador
        // Usamos "\\" antes del operador porque caracteres como '+' y '*' son especiales en expresiones regulares
        String[] partes = expresion.split("\\" + operador);
        
        // Validar que tengamos ambos números listos para la operación
        if (partes.length < 2) {
            return; 
        }
        
        double numero1 = Double.parseDouble(partes[0]);
        double numero2 = Double.parseDouble(partes[1]);
        double resultado = 0;
        
        // 3. Evaluar y realizar la operación correspondiente [7]
        switch (operador) {
            case "+":
                resultado = numero1 + numero2;
                break;
            case "-":
                resultado = numero1 - numero2;
                break;
            case "*":
                resultado = numero1 * numero2;
                break;
            case "/":
                if (numero2 == 0) {
                    display.setText("Error: Div/0");
                    return;
                }
                resultado = numero1 / numero2;
                break;
        }
        
        // Mostrar el resultado de forma estética en el display
        // Si el resultado es un número entero (ej: 5.0), le quitamos el decimal (.0)
        if (resultado % 1 == 0) {
            display.setText(String.valueOf((int) resultado));
        } else {
            display.setText(String.valueOf(resultado));
        }
        
    } catch (NumberFormatException e) {
        display.setText("Error de formato");
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Calculadora");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(300, 400);
                frame.add(new Calculadora());
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);   
                frame.setVisible(true);
            }
        });
    }
}
