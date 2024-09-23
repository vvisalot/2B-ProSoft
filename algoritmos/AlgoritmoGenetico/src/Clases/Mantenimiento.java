package Clases;
import java.time.LocalDateTime;


public class Mantenimiento {
    String camion;
    private LocalDateTime fechaHoraInicio;

    public Mantenimiento(String camion, LocalDateTime fechaHoraInicio) {
        this.camion = camion;
        this.fechaHoraInicio = fechaHoraInicio;
    }
    

    public String getCamion() {
        return camion;
    }

    public void setCamion(String camion) {
        this.camion = camion;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }
    
}
