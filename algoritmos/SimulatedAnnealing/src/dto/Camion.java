package dto;

import java.util.List;

public record Camion(
        String codigo,
        char tipo,
        int capacidad,
        int cargaActual,
        List<Paquete>paquetes) {
}
