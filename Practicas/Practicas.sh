#!/bin/bash

# ============================================
#       PRÁCTICAS - JAVA GRÁFICAS
# ============================================

PRACTICA="$1"

# Colores
VERDE='\033[0;32m'2
MAGENTA='\033[1;35m'
AMARILLO='\033[1;33m'
ROJO='\033[0;31m'
CYAN='\033[0;36m'
NC='\033[0m' # Sin color

# ============================================
# Validaciones
# ============================================

clear

if [ -z "$PRACTICA" ]; then
    echo -e "${ROJO}Error:${NC} Debe proporcionar el número de práctica."
    echo
    echo "Uso: ./Practicas.sh <num_practica>"
    echo "Ejemplo: ./Practicas.sh 1"
    exit 1
fi

echo
echo -e "${CYAN}============================================${NC}"
echo -e "${CYAN}          PRÁCTICAS DE JAVA${NC}"
echo -e "${CYAN}============================================${NC}"
echo

case "$PRACTICA" in

    1)
        echo -e "${MAGENTA}[Práctica 1]${NC} Hola Mundo"
        echo -e "${CYAN}--------------------------------------------${NC}"
        java -cp . Mundo.HolaMundo
        ;;

    2)
        echo -e "${MAGENTA}[Práctica 2]${NC} Números Aleatorios"
        echo -e "${CYAN}--------------------------------------------${NC}"
        java -cp . Aleatorios.NumeroAleatorio
        ;;

    3)
        echo -e "${MAGENTA}[Práctica 3]${NC} Ordenamiento de números"
        echo -e "${CYAN}--------------------------------------------${NC}"
        echo -e "${AMARILLO}Entrada:${NC}"
        echo "10 12 3 5 1 8 23"
        echo
        echo -e "${AMARILLO}Resultado:${NC}"
        java -cp . OrdenarListaNumeros.OrdenNumeros 10 12 3 5 1 8 23
        ;;

    4)
        echo -e "${MAGENTA}[Práctica 4]${NC} Subcadenas"
        echo -e "${CYAN}--------------------------------------------${NC}"
        echo -e "${AMARILLO}Cadena:${NC} \"Hola mundo\""
        echo
        echo -e "${AMARILLO}Subcadenas:${NC}"
        java -cp . Subcadena.Subcadenas "Hola mundo"
        ;;

    5)
        echo -e "${MAGENTA}[Práctica 5]${NC} Conversor Hexadecimal ↔ IP"
        echo -e "${CYAN}--------------------------------------------${NC}"

        echo -e "${AMARILLO}Hexadecimal → IP${NC}"
        echo "0A05FF01"
        java -cp . Hex2IP.Hex2IP -hex "0A05FF01"

        echo
        echo -e "${AMARILLO}IP → Hexadecimal${NC}"
        echo "10.5.255.1"
        java -cp . Hex2IP.Hex2IP -ip "10.5.255.1"

        echo
        echo -e "${AMARILLO}Prueba de entrada inválida${NC}"
        echo "GH34VTPP"
        java -cp . Hex2IP.Hex2IP -hex "GH34VTPP"
        ;;
    6)
        java -cp . Ventana.Ventana
        ;;
    *)
        echo -e "${ROJO}Error:${NC} Número de práctica no válido."
        echo "Las prácticas disponibles son: 1, 2, 3, 4, 5 y 6."
        exit 1
        ;;
esac

echo
echo -e "${VERDE}============================================${NC}"
echo -e "${VERDE}        Práctica finalizada${NC}"
echo -e "${VERDE}============================================${NC}"
echo

read -r -p "Presiona Enter para continuar..."
clear