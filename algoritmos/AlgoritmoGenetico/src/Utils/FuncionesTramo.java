package Utils;
import Clases.Velocidad;
import java.util.ArrayList;
import java.util.List;


public class FuncionesTramo {
    private static final int RADIO_TIERRA_KM = 6371; // Radio de la Tierra en kilómetros

    // Para calcular la distancia entre dos puntos dados por latitud y longitud
    public double calcularDistancia(double latitud1, double longitud1, double latitud2, double longitud2) {
        double dLat = Math.toRadians(latitud2 - latitud1);
        double dLon = Math.toRadians(longitud2 - longitud1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latitud1)) * Math.cos(Math.toRadians(latitud2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA_KM * c; // Distancia en kilómetros
    }
    
    public static double calcularVelocidad(String regionOrigen, String regionDestino, List<Velocidad> velocidades) {
            // Busca la velocidad de la región origen-destino
            for (Velocidad vel : velocidades) {
                if (vel.getRegionOrigen().equals(regionOrigen) && vel.getRegionDestino().equals(regionDestino)) {
                    return vel.getVelocidadKmh();
                }
                // Si no se encuentra, intercambia el origen y el destino
                if (vel.getRegionOrigen().equals(regionDestino) && vel.getRegionDestino().equals(regionOrigen)) {
                    return vel.getVelocidadKmh();
                }
            }
            // Si no encuentra ningún dato, retorna -1 como señal de error
            return -1;
        }

    public static double calcularHoras(double distanciaTramo, double velocidadTramo) {
        return distanciaTramo/velocidadTramo; 
    }
}
