package grupo2b.odiroute.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RelojSimulado {
    private LocalDateTime tiempo;
    private LocalDateTime tiempoSiguienteBatch;
    private static final RelojSimulado instance = new RelojSimulado();;
    private final int INTERVALO = 6;
    private RelojSimulado() {
        try {
            this.tiempo = LocalDateTime.parse("2000-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            this.tiempo = LocalDateTime.now();
        }        
        this.tiempoSiguienteBatch = this.tiempo.plusHours(INTERVALO);
    }

    public static RelojSimulado getInstance() {
        return instance;
    }

    public LocalDateTime getTiempo() {
        return this.tiempo;
    }

    public LocalDateTime getTiempoSiguienteBatch() {
        return this.tiempoSiguienteBatch;
    }

    public void pasarCicloDeEntregas() {
        this.tiempo = this.tiempo.plusHours(INTERVALO);
        this.tiempoSiguienteBatch = this.tiempo.plusHours(6);
    }
}
