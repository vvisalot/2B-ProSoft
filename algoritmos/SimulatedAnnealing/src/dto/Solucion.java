package dto;

import java.util.List;

public record Solucion(Camion camion,
                       List<Tramo> tramos,
                       double tiempoTotal) {
}
