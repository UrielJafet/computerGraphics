package asteroids.graphics;

public class Fonts {
    
    private static final int ANCHO = 5;
    private static final int ALTO = 7;
    private static final int ESPACIO = 1;

    private static final int [][] DIGITOS = {
        {0b01110,0b10001,0b10011,0b10101,0b11001,0b10001,0b01110},  // 0
        {0b00100,0b01100,0b00100,0b00100,0b00100,0b00100,0b01110},  // 1
        {0b01110,0b10001,0b00001,0b00010,0b00100,0b01000,0b11111},  // 2
        {0b11110,0b00001,0b00001,0b01110,0b00001,0b00001,0b11110},  // 3
        {0b00010,0b00110,0b01010,0b10010,0b11111,0b00010,0b00010},  // 4
        {0b11111,0b10000,0b11110,0b00001,0b00001,0b10001,0b01110},  // 5
        {0b00110,0b01000,0b10000,0b11110,0b10001,0b10001,0b01110},  // 6
        {0b11111,0b00001,0b00010,0b00100,0b01000,0b01000,0b01000},  // 7
        {0b01110,0b10001,0b10001,0b01110,0b10001,0b10001,0b01110},  // 8
        {0b01110,0b10001,0b10001,0b01111,0b00001,0b00010,0b01100}   // 9
    };

    private static final int [][] LETRAS = {
        // A
        {0b01110, 0b10001, 0b10001, 0b11111, 0b10001, 0b10001, 0b10001},
        // C
        {0b01110, 0b10001, 0b10000, 0b10000, 0b10000, 0b10001, 0b01110},
        // E
        {0b11111, 0b10000, 0b10000, 0b11110, 0b10000, 0b10000, 0b11111},
        // G
        {0b01110, 0b10001, 0b10000, 0b10111, 0b10001, 0b10001, 0b01110},
        // L
        {0b10000, 0b10000, 0b10000, 0b10000, 0b10000, 0b10000, 0b11111},
        // M
        {0b10001, 0b11011, 0b10101, 0b10001, 0b10001, 0b10001, 0b10001},
        // O
        {0b01110, 0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b01110},
        // P
        {0b11110, 0b10001, 0b10001, 0b11110, 0b10000, 0b10000, 0b10000},
        // R
        {0b11110, 0b10001, 0b10001, 0b11110, 0b10100, 0b10010, 0b10001},
        // S
        {0b01111, 0b10000, 0b10000, 0b01110, 0b00001, 0b00001, 0b11110},
        // V
        {0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b01010, 0b00100}
    };

    private static int[] glifo(char c) {
        // Dígitos
        if (c >= '0' && c <= '9') {
            return DIGITOS[c - '0'];
        }

        // Letras
        switch (c) {
            case 'A': return LETRAS[0];
            case 'C': return LETRAS[1];
            case 'E': return LETRAS[2];
            case 'G': return LETRAS[3];
            case 'L': return LETRAS[4];
            case 'M': return LETRAS[5];
            case 'O': return LETRAS[6];
            case 'P': return LETRAS[7];
            case 'R': return LETRAS[8];
            case 'S': return LETRAS[9];
            case 'V': return LETRAS[10];

            // Espacio u otros caracteres
            case ' ':
            default:
                return null;
        }
    }

    public static void dibujarTexto(Rasterizador r, String texto, int x, int y, int escala, int color) {
        int cursorX = x;  // posición horizontal actual

        for (char ch : texto.toUpperCase().toCharArray()) {
            int[] letra = glifo(ch);

            // Si es espacio o carácter no soportado
            if (letra == null) {
                cursorX += (ANCHO + ESPACIO) * escala;
                continue;
            }
            // Dibujar la letra (7 filas)
            for (int fila = 0; fila < ALTO; fila++) {
                int bits = letra[fila];

                // Revisar cada uno de los 5 bits (de izquierda a derecha)
                for (int col = 0; col < ANCHO; col++) {
                    // El bit más significativo está a la izquierda (bit 4)
                    if ((bits & (1 << (ANCHO - 1 - col))) != 0) {
                        // Dibujar el píxel (respetando la escala)
                        for (int ey = 0; ey < escala; ey++) {
                            for (int ex = 0; ex < escala; ex++) {
                                r.pixel(
                                    cursorX + col * escala + ex,
                                    y + fila * escala + ey,
                                    color
                                );
                            }
                        }
                    }
                }
            }
            // Avanzar el cursor para la siguiente letra
            cursorX += (ANCHO + ESPACIO) * escala;
        }
    }

    public static int anchoTexto(String texto, int escala) {
        if (texto == null || texto.isEmpty()) return 0;

        int n = texto.length();
        return n * (ANCHO + ESPACIO) * escala - ESPACIO * escala;
    }
}
