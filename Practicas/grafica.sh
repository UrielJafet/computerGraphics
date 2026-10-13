#!/bin/bash

# ============================================
#       PRÁCTICAS - JAVA GRÁFICAS
# ============================================

PRACTICA="$1"
# Colores
ROJO='\033[0;31m'
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

case "$PRACTICA" in
    1) 
        java -cp . Ventana.Ventana
        ;;
    2)
        java -cp . espiralArquimides.Espiral
        ;;
    3) 
        java -cp . Pastel.GraficaPastel 10 15 35 5 25 10
        ;;
    4)
        java -cp . Monito.UsoGraficos
        ;;
    5)
        java -cp . visor.VisorImagen
        ;;
    6)
        java -cp . mouseFrame.MouseMonitor
        ;;
    7)
        java -cp . calculadora.Calculadora
        ;;
    8)
        java -cp . miniPaint.MiniPaint
        ;;
    9)
        java -cp . keyFrame.KeyMonitor
        ;;
    *)
        echo -e "${ROJO}Error:${NC} Número de práctica no válido."
        echo "Las prácticas disponibles son: 1, 2, 3, 4, 5, 6 y 7."
        exit 1
        ;;
esac