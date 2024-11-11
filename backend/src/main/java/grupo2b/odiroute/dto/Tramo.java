package grupo2b.odiroute.dto;

import java.time.LocalDateTime;

public record Tramo(Oficina origen,
                    Oficina destino,
                    double distancia,
                    double velocidad,
                    LocalDateTime tiempoSalida,
                    LocalDateTime tiempoLlegada,
                    int tiempoEspera,
                    boolean seDejaraElPaquete) {

}
