package Clases;
import java.time.LocalDateTime;

public class Bloqueo {
    private String ubigeoOrigen;
    private String ubigeoDestino;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    public Bloqueo(String ubigeoOrigen, String ubigeoDestino, LocalDateTime fechaHoraInicio, 
            LocalDateTime fechaHoraFin) {
        this.ubigeoOrigen = ubigeoOrigen;
        this.ubigeoDestino = ubigeoDestino;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
    }

    @Override
    public String toString() {
        return "Bloqueo{" +
                "ubigeoOrigen='" + ubigeoOrigen + '\'' +
                ", ubigeoDestino='" + ubigeoDestino + '\'' +
                ", fechaHoraInicio=" + fechaHoraInicio +
                ", fechaHoraFin=" + fechaHoraFin +
                '}';
    }
}

