package main;

import Clases.*;
import Utils.LeerDatos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

public class AlgoritmoGenetico {
    static final int TAMANO_POBLACION = 10;
    static final int NUM_GENERACIONES = 100; //antes era 100
    static Random random = new Random();

    public static void main(String[] args) throws IOException {

        // Generar población inicial
            //probar con solamente un pedido                     //arequipa
        Venta pedido = new Venta(LocalDateTime.now(), "040101", "230101", 4, "000786");
        
        List<Tramo> tramos = LeerDatos.leerTramos("archivos/tramos.txt");
        HashMap<String, Tramo> mapaTramos = new HashMap<>();
        for (Tramo tram : tramos) {
            String clave = tram.getUbigeoOrigen() + "-" + tram.getUbigeoDestino(); 
            mapaTramos.put(clave, tram);
        }
        Camion camion = new Camion("A001",90, "040101");
        ArrayList<Cromosoma> poblacion = generarPoblacionInicial(pedido,mapaTramos,camion);

        // Evolucionar por generaciones
        for (int gen = 0; gen < NUM_GENERACIONES; gen++) {
            poblacion = evolucionarPoblacion(poblacion);
            Cromosoma mejorIndividuo = seleccionarMejor(poblacion);
            System.out.println("Generación " + gen + ": Mejor tiempo = " + mejorIndividuo.getTiempoTotal());
        }
    }

    public static ArrayList<Cromosoma> generarPoblacionInicial(Venta pedido, HashMap<String, Tramo> mapaTramos, Camion camion) {
        ArrayList<Cromosoma> poblacion = new ArrayList<>();

        //Verificar capacidad del camión para el pedido --> solo un camion
        if (pedido.getCantidad() > camion.getCapacidad()) {
            System.out.println("El camión no tiene capacidad suficiente para este pedido.");
            return poblacion;
        }
        
        // Generar todas las rutas posibles entre el origen y el destino del pedido
        HashMap<String, List<Tramo>> rutasPosibles = generarRutas(pedido.getUbigeoOrigen(), pedido.getUbigeoDestino(), mapaTramos,null);

        // Crear cromosomas (individuos) a partir de las rutas
        for (Map.Entry<String, List<Tramo>> entry : rutasPosibles.entrySet()) {
            HashMap<String, Tramo> rutaMap = new HashMap<>();
            for (Tramo tramo : entry.getValue()) {
                String clave = tramo.getUbigeoOrigen() + "-" + tramo.getUbigeoDestino();
                rutaMap.put(clave, tramo);
            }
            Cromosoma cromosoma = new Cromosoma(rutaMap, camion);
            cromosoma.setTiempoTotal(); // para que ya tenga de una vez el tiempo en cromosoma
            poblacion.add(cromosoma);
        }

        return poblacion;
    }


    // Función recursiva para generar todas las rutas posibles desde el origen al destino
    public static HashMap<String, List<Tramo>> generarRutas(String origen, String destino, HashMap<String, Tramo> mapaTramos, List<String> visitados) {
        if (visitados == null) {
            visitados = new ArrayList<>();
        }
        visitados.add(origen);

        HashMap<String, List<Tramo>> rutasMap = new HashMap<>();

        for (String clave : mapaTramos.keySet()) {
            // Verificar si la clave empieza con el origen
            if (clave.startsWith(origen)) {
                Tramo tramo = mapaTramos.get(clave);
                if (tramo.getUbigeoDestino().equals(destino)) {
                    // Ruta directa encontrada
                    List<Tramo> rutaDirecta = new ArrayList<>();
                    rutaDirecta.add(tramo);
                    rutasMap.put(origen + "-" + destino, rutaDirecta);
                } else if (!visitados.contains(tramo.getUbigeoDestino())) {
                    // Buscar rutas a través de destinos intermedios
                    HashMap<String, List<Tramo>> rutasIntermedias = generarRutas(tramo.getUbigeoDestino(), destino, mapaTramos, visitados);
                    for (String claveIntermedia : rutasIntermedias.keySet()) {
                        List<Tramo> nuevaRuta = new ArrayList<>();
                        nuevaRuta.add(tramo);  // Añadir tramo inicial
                        nuevaRuta.addAll(rutasIntermedias.get(claveIntermedia));  // Añadir la ruta intermedia
                        rutasMap.put(origen + "-" + destino, nuevaRuta); // Guardar la nueva ruta en el mapa
                    }
                }
            }
        }
        return rutasMap;
    }




    public static ArrayList<Cromosoma> evolucionarPoblacion(ArrayList<Cromosoma> poblacion) {
        ArrayList<Cromosoma> nuevaPoblacion = new ArrayList<>();
        for (int i = 0; i < poblacion.size(); i++) {
            Cromosoma padre1 = seleccionarMejor(poblacion);
            Cromosoma padre2 = seleccionarMejor(poblacion);
            Cromosoma hijo = cruzar(padre1, padre2);
            mutar(hijo);
            nuevaPoblacion.add(hijo);
        }
        return nuevaPoblacion;
    }

    public static Cromosoma cruzar(Cromosoma padre1, Cromosoma padre2) {
        HashMap<String, Tramo> nuevaRutaMap = new HashMap<>(padre1.getRutaMap());  // Copiar la ruta del padre1
        
        for (Tramo tramo : padre2.getRutaMap().values()) {
            String clave = tramo.getUbigeoOrigen() + "-" + tramo.getUbigeoDestino();
            if (!nuevaRutaMap.containsKey(clave)) {
                nuevaRutaMap.put(clave, tramo);  // Añadir los tramos únicos del padre2
            }
        }
    
        return new Cromosoma(nuevaRutaMap, new Camion(padre1.getIdCamion()));
    }

    public static void mutar(Cromosoma individuo) {
        List<String> claves = new ArrayList<>(individuo.getRutaMap().keySet());
        int i = random.nextInt(claves.size());
        int j = random.nextInt(claves.size());
        
        // Intercambiar tramos
        Tramo tramo1 = individuo.getRutaMap().get(claves.get(i));
        Tramo tramo2 = individuo.getRutaMap().get(claves.get(j));
    
        // Hacer el swap
        individuo.getRutaMap().put(claves.get(i), tramo2);
        individuo.getRutaMap().put(claves.get(j), tramo1);
    }
    
    public static Cromosoma seleccionarMejor(ArrayList<Cromosoma> poblacion) {
        Cromosoma mejorIndividuo = poblacion.get(0);
        for (Cromosoma individuo : poblacion) {
            if (individuo.getTiempoTotal() < mejorIndividuo.getTiempoTotal()) {
                mejorIndividuo = individuo;
            }
        }
        return mejorIndividuo;
    }

}
