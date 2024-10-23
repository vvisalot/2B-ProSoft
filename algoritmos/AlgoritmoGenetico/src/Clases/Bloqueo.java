package Clases;
import java.time.LocalDateTime;

public class Bloqueo {
    private Tramo tramo;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;

    public Bloqueo(Tramo tramo, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin) {
        this.tramo = tramo;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
    }

    public Tramo getTramo() {
        return tramo;
    }

    public void setTramo(Tramo tramo) {
        this.tramo = tramo;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    
    @Override
    public String toString() {
        return "Bloqueo{" +
                "ubigeoOrigen='" + tramo.getUbigeoOrigen()+ '\'' +
                ", ubigeoDestino='" + tramo.getUbigeoDestino() + '\'' +
                ", fechaHoraInicio=" + fechaHoraInicio +
                ", fechaHoraFin=" + fechaHoraFin +
                '}';
    }
}

