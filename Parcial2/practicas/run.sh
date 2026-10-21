#!/bin/bash

# ============================================
#       PRÁCTICAS - JAVA GRÁFICAS
# ============================================

PRACTICA="$1"

if [ -z "$PRACTICA" ]; then
    echo -e "${ROJO}Error:${NC} Debe proporcionar el número de práctica."
    echo
    echo "Uso: ./Practicas.sh <num_practica>"
    echo "Ejemplo: ./Practicas.sh 1"
    exit 1
fi

case "$PRACTICA" in
    1) 
        java -cp . lineaRecta.LineaRecta
        ;;
    2)
        java -cp . algoritmos.Algoritmos
        ;;
    3) 
        java -cp . puntoMedio.PuntoMedio
        ;;
    4)
        java -cp . circunferencia.Circunferencia
        ;;
    5)
        java -cp primitivas/bin primitivas.Main
        ;;
    6)
        java -cp . tipoLineas.TipoLineas
        ;;
    7)
        java -cp . relleno.Relleno
        ;;
    *)
        echo -e "${ROJO}Error:${NC} Número de práctica no válido."
        echo "Las prácticas disponibles son: 1, 2, 3, 4, 5, 6 y 7."
        exit 1
        ;;
esac