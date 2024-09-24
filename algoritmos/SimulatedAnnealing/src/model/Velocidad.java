package model;

//OPTIMIZAR ESTA WBD, SON DATOS FIJOS.

import java.util.HashMap;
import java.util.Map;

public class Velocidad {
    public static final Map<String, Integer> velocidades;

    static {
        velocidades = new HashMap<>();
        // Guardar las velocidades en el formato "Region1-Region2"
        velocidades.put("Costa-Costa", 70);
        velocidades.put("Costa-Sierra", 50);
        velocidades.put("Sierra-Sierra", 60);
        velocidades.put("Sierra-Selva", 55);
        velocidades.put("Selva-Selva", 65);
    }


    // Función para obtener la velocidad promedio entre dos regiones
    public static int obtenerVelocidad(String region1, String region2) {
        String clave = region1 + "-" + region2;
        // Buscar la velocidad correspondiente
        if (velocidades.containsKey(clave)) {
            return velocidades.get(clave);
        }
        // Si no se encuentra la clave, buscar en el sentido contrario
        clave = region2 + "-" + region1;
        if (velocidades.containsKey(clave)) {
            return velocidades.get(clave);
        }
        // Si no se encuentra la velocidad entre esas dos regiones, retornar un valor predeterminado
        return -1; // o lanzar una excepción si es necesario
    }
}