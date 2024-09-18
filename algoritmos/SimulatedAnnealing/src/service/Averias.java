package service;

import java.util.Random;

public class Averias {

    // Tipos de averías
    public enum TipoAveria {
        AVERIA_1,  // Parada de camión por n horas
        AVERIA_2,  // Parada por un turno completo
        AVERIA_3   // Inoperativo durante 3 días
    }

    // Simular una avería
    public TipoAveria simularAveria() {
        Random random = new Random();
        int probabilidad = random.nextInt(100);  // Número entre 0 y 99

        if (probabilidad < 50) {
            return TipoAveria.AVERIA_1;  // Probabilidad del 50%
        } else if (probabilidad < 80) {
            return TipoAveria.AVERIA_2;  // Probabilidad del 30%
        } else {
            return TipoAveria.AVERIA_3;  // Probabilidad del 20%
        }
    }

    // Método para obtener el impacto en la ruta (por ejemplo, retraso en horas o días)
    public double calcularRetrasoPorAveria(TipoAveria tipoAveria) {
        return switch (tipoAveria) {
            case AVERIA_1 -> 2; // Por ejemplo, un retraso de 2 horas
            case AVERIA_2 -> 8; // Un turno completo, por ejemplo, 8 horas
            case AVERIA_3 -> 72; // 3 días, es decir, 72 horas
            default -> 0;
        };
    }
}