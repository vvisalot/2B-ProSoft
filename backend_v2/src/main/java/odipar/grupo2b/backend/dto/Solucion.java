package odipar.grupo2b.backend.dto;

import java.util.List;

public record Solucion(Camion camion,
                       List<Tramo> tramos,
                       double tiempoTotal) {
}
