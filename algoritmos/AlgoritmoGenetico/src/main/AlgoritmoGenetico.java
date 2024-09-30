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
import java.util.Comparator;
import java.util.Random;

public class AlgoritmoGenetico {
    //static final int TAMANO_POBLACION = 10;
    static final int NUM_GENERACIONES = 100; //antes era 100
    static Random random = new Random();

    public static void main(String[] args) throws IOException {

        // Generar población inicial 
        //probar con solamente un pedido                     //arequipa               tacna
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
            //Cromosoma mejorIndividuo = seleccionarMejor(poblacion);
            //System.out.println("Generación " + gen + ": Mejor tiempo = " + mejorIndividuo.getTiempoTotal());
            
            System.out.println("Generación " + gen + ":");
            for (Cromosoma cromosoma : poblacion) {
                System.out.println("  Tiempo total: " + cromosoma.getTiempoTotal());
            }
            Cromosoma mejorIndividuo = seleccionarMejor(poblacion);
            System.out.println("  Mejor tiempo: " + mejorIndividuo.getTiempoTotal());
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


    // public static ArrayList<Cromosoma> evolucionarPoblacion(ArrayList<Cromosoma> poblacion) {
    //     ArrayList<Cromosoma> nuevaPoblacion = new ArrayList<>();
    //     for (Cromosoma cromosoma : poblacion) {
    //         cromosoma.setTiempoTotal(); // Llamar al método setTiempoTotal() para cada cromosoma
    //     }
    //     for (int i = 0; i < poblacion.size(); i++) {
    //         Cromosoma padre1 = seleccionarMejor(poblacion);
    //         Cromosoma padre2 = seleccionarMejor(poblacion);
    //         Cromosoma hijo = cruzar(padre1, padre2);
    //         mutar(hijo);
    //         hijo.setTiempoTotal(); //actualizar tiempo total de cromosoma
    //         nuevaPoblacion.add(hijo);
    //     }
    //     return nuevaPoblacion;
    // }
    
    public static ArrayList<Cromosoma> evolucionarPoblacion(ArrayList<Cromosoma> poblacion) {
        ArrayList<Cromosoma> nuevaPoblacion = new ArrayList<>();
        for (Cromosoma cromosoma : poblacion) {
            cromosoma.setTiempoTotal(); // Llamar al método setTiempoTotal() para cada cromosoma
        }
        // Elitismo: Mantén los mejores cromosomas (por ejemplo, el 10% mejor) sin alterarlos
        int elitismoSize = (int) (0.1 * poblacion.size());
        Collections.sort(poblacion, Comparator.comparingDouble(Cromosoma::getTiempoTotal));
        
        // Añadir los mejores cromosomas directamente a la nueva población
        for (int i = 0; i < elitismoSize; i++) {
        nuevaPoblacion.add(poblacion.get(i));
        }
        
        // Generar el resto de la nueva población
        for (int i = elitismoSize; i < poblacion.size(); i++) {
            Cromosoma padre1 = seleccionarPorRuleta(poblacion);
            Cromosoma padre2 = null;

            // Intentar encontrar un padre2 diferente de padre1 (máximo 10 intentos)
            int intentos = 0;
            do {
                padre2 = seleccionarPorRuleta(poblacion);
                intentos++;
            } while (padre1.equals(padre2) && intentos < 10);

            // Si después de 10 intentos no se encuentra uno diferente, selecciona aleatoriamente
            if (padre1.equals(padre2)) {
                padre2 = seleccionarAleatorio(poblacion);
            }
            Cromosoma hijo = cruzar(padre1, padre2);
            mutar(hijo);
            hijo.setTiempoTotal(); //actualizar tiempo total de cromosoma
            nuevaPoblacion.add(hijo);
        }
        return nuevaPoblacion;
    }

    /*
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
    */
    
    public static Cromosoma cruzar(Cromosoma padre1, Cromosoma padre2) {
        HashMap<String, Tramo> nuevaRutaMap = new HashMap<>();
        List<Tramo> tramosPadre1 = new ArrayList<>(padre1.getRutaMap().values());
        List<Tramo> tramosPadre2 = new ArrayList<>(padre2.getRutaMap().values());

        // Mezclar tramos de ambos padres de manera más equitativa, evitando duplicados
        for (int i = 0; i < tramosPadre1.size(); i++) {
            // Alternar entre agregar tramos del padre1 y padre2
            if (random.nextBoolean()) {
                Tramo tramo = tramosPadre1.get(i);
                String clave = tramo.getUbigeoOrigen() + "-" + tramo.getUbigeoDestino();
                if (!nuevaRutaMap.containsKey(clave)) {
                    nuevaRutaMap.put(clave, tramo);
                }
            } else if (i < tramosPadre2.size()) {
                Tramo tramo = tramosPadre2.get(i);
                String clave = tramo.getUbigeoOrigen() + "-" + tramo.getUbigeoDestino();
                if (!nuevaRutaMap.containsKey(clave)) {
                    nuevaRutaMap.put(clave, tramo);
                }
            }
        }

        return new Cromosoma(nuevaRutaMap, new Camion(padre1.getIdCamion()));
    }


    public static void mutar(Cromosoma individuo) {
        
        // Posibilidad de realizar más de una mutación (por ejemplo, entre 1 y 3 mutaciones)
        int numMutaciones = 1 + random.nextInt(3);
        
        for (int m = 0; m < numMutaciones; m++) {
            List<String> claves = new ArrayList<>(individuo.getRutaMap().keySet());
            int i = random.nextInt(claves.size());
            int j = random.nextInt(claves.size());

            // Intercambiar tramos
            Tramo tramo1 = individuo.getRutaMap().get(claves.get(i));
            Tramo tramo2 = individuo.getRutaMap().get(claves.get(j));

            // Realizar el swap
            individuo.getRutaMap().put(claves.get(i), tramo2);
            individuo.getRutaMap().put(claves.get(j), tramo1);
        }
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

    public static Cromosoma seleccionarAleatorio(ArrayList<Cromosoma> poblacion) {
        int indice = random.nextInt(poblacion.size());
        return poblacion.get(indice);
    }


    public static Cromosoma seleccionarPorRuleta(ArrayList<Cromosoma> poblacion) {

        // Paso 1: Encontrar el tiempo máximo en la población
        double maxTiempo = Double.MIN_VALUE;
        for (Cromosoma cromosoma : poblacion) {
            maxTiempo = Math.max(maxTiempo, cromosoma.getTiempoTotal());
        }

        // Paso 2: Calcular la suma total de aptitudes ajustadas (inversa basada en el tiempo máximo)
        double sumaTotalFitness = 0.0;
        for (Cromosoma cromosoma : poblacion) {
            // Aptitud ajustada: maxTiempo - tiempoTotal (de modo que los tiempos menores tengan más probabilidad)
            sumaTotalFitness += (maxTiempo - cromosoma.getTiempoTotal());
        }

        // Paso 3: Generar un número aleatorio entre 0 y la suma total de aptitudes
        double randomValue = random.nextDouble() * sumaTotalFitness;

        // Paso 4: Seleccionar el cromosoma correspondiente
        double sumaParcial = 0.0;
        for (Cromosoma cromosoma : poblacion) {
            sumaParcial += (maxTiempo - cromosoma.getTiempoTotal());
            if (sumaParcial >= randomValue) {
                return cromosoma;  // Seleccionar este cromosoma
            }
        }

        // En caso de error numérico, retorna el último cromosoma
        return poblacion.get(poblacion.size() - 1);
    }
}
