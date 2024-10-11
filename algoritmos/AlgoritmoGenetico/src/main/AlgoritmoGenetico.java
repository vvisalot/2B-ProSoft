package main;

import Clases.*;
import Utils.LeerDatos;
import static Utils.LeerDatos.leerBloqueos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;

public class AlgoritmoGenetico {
    private int numGeneracion;
    private Double tasaMutacion;
    private Venta pedido;
    private List<Camion> camioniones;
    static Random random = new Random();
    //static final int NUM_GENERACIONES = 3; //antes era 100

    public AlgoritmoGenetico(int numGeneracion, Double tasaMutacion, Venta pedido, List<Camion> camioniones) {
        this.numGeneracion = numGeneracion;
        this.tasaMutacion = tasaMutacion;
        this.pedido = pedido;
        this.camioniones = camioniones;
    }


    public double generarAlgoritmoGenético(int numGeneracion, Double tasaMutacion, Venta pedido, List<Camion> camioniones) throws IOException{
        //Venta pedido = new Venta(fechaEnvio, "040101", "230101", 4, "000786");
        //Camion camion = new Camion("A001",90, "040101");
        List<Tramo> tramos = LeerDatos.leerTramos("archivos/tramos.txt");
        List<Bloqueo> bloqueos = LeerDatos.leerBloqueos("archivos/c.1inf54.24-2.bloqueo.10.txt");
        // Crear el mapa optimizado por clave origen: HashMap<String, List<Tramo>>
        HashMap<String, List<Tramo>> mapaTramos = new HashMap<>();

        for (Tramo tramo : tramos) {
            String origen = tramo.getUbigeoOrigen();
            // Usar computeIfAbsent para inicializar la lista si no existe
            mapaTramos.computeIfAbsent(origen, k -> new ArrayList<>()).add(tramo);
        }

        //Declarar mejor tiempo
        Double mejorTiempo=999.9;
     
        // Generar población inicial
        LocalDateTime fechaEnvio = pedido.getFechaHora();
        ArrayList<Cromosoma> poblacion = generarPoblacionInicial(pedido,mapaTramos,camioniones,bloqueos,fechaEnvio);

        // Evolucionar por generaciones
        for (int gen = 0; gen < numGeneracion; gen++) {
            poblacion = evolucionarPoblacion(poblacion);
            //Cromosoma mejorIndividuo = seleccionarMejor(poblacion);
            //System.out.println("Generación " + gen + ": Mejor tiempo = " + mejorIndividuo.getTiempoTotal());
            
            System.out.println("Generación " + gen + ":");
            for (Cromosoma cromosoma : poblacion) {
//                System.out.println("  Tiempo total: " + cromosoma.getTiempoTotal());
            }
            Cromosoma mejorIndividuo = seleccionarMejor(poblacion);
            System.out.println("  Mejor tiempo: " + mejorIndividuo.getTiempoTotal());
            imprimirRuta(mejorIndividuo,pedido);

            if(mejorTiempo>mejorIndividuo.getTiempoTotal()) mejorTiempo=mejorIndividuo.getTiempoTotal();
        }

        return mejorTiempo;
    }


    //entender a la poblacion
    public static ArrayList<Cromosoma> generarPoblacionInicial(Venta pedido,
            HashMap<String, List<Tramo>> mapaTramos, List<Camion> camioniones, List<Bloqueo> bloqueos,
            LocalDateTime fechaEnvio) {
        
        ArrayList<Cromosoma> poblacion = new ArrayList<>();

        //Verificar capacidad del camión para el pedido --> solo un camion
        Camion newCamion = new Camion(); 
        for(Camion camion: camioniones){
            if (pedido.getCantidad() > camion.getCapacidad()) {
//                System.out.println("El camión no tiene capacidad suficiente para este pedido.");
                ///return poblacion;
            }else{
                newCamion=new Camion(camion.getIdCamion(),camion.getCapacidad(),camion.getUbigeo());
            }
        }
        
        // Generar todas las rutas posibles entre el origen y el destino del pedido
        HashMap<String, List<Tramo>> rutasPosibles = generarRutas(pedido.getUbigeoOrigen(), pedido.getUbigeoDestino(), mapaTramos,null);

        // Crear cromosomas (individuos) a partir de las rutas
        for (Map.Entry<String, List<Tramo>> entry : rutasPosibles.entrySet()) {
            HashMap<String, Tramo> rutaMap = new HashMap<>();
            boolean tieneBloqueo = false;
            
            // Verificar cada tramo antes de agregarlo           
           for (Tramo tramo : entry.getValue()) {
                // Verificar si el tramo está bloqueado en la fecha de envío
                if (estaBloqueado(tramo, bloqueos, fechaEnvio)) {
                    tieneBloqueo = true;  // Si hay un tramo bloqueado, no incluir esta ruta
                    break;  // Salir del ciclo si se encuentra un tramo bloqueado
                }

                // Si no está bloqueado, agregarlo a la ruta                
                String clave = tramo.getUbigeoOrigen() + "-" + tramo.getUbigeoDestino();
                rutaMap.put(clave, tramo);
            }
           
            // Si no hay tramos bloqueados en esta ruta, crear el cromosoma
            if (!tieneBloqueo) {
                Cromosoma cromosoma = new Cromosoma(rutaMap, newCamion);
                cromosoma.setTiempoTotal();  // Calcular el tiempo total del cromosoma
                cromosoma.setFitness();
                poblacion.add(cromosoma);  // Añadir el cromosoma a la población
            }
        }

        return poblacion;
    }
    
    // Método auxiliar para verificar si un tramo está bloqueado
    public static boolean estaBloqueado(Tramo tramo, List<Bloqueo> bloqueos, LocalDateTime fechaEnvio) {
        for (Bloqueo bloqueo : bloqueos) {
            if (bloqueo.getTramo().equals(tramo) &&
                (fechaEnvio.isAfter(bloqueo.getFechaHoraInicio()) && fechaEnvio.isBefore(bloqueo.getFechaHoraFin()))) {
                return true;  // El tramo está bloqueado
            }
        }
        return false;  // El tramo no está bloqueado
    }


    // Función recursiva para generar todas las rutas posibles desde el origen al destino
    public static HashMap<String, List<Tramo>> generarRutas(String origen, String destino, HashMap<String, List<Tramo>> mapaTramos, List<String> visitados) {
        if (visitados == null) {
            visitados = new ArrayList<>();
        }
        
        visitados.add(origen);

        HashMap<String, List<Tramo>> rutasMap = new HashMap<>();
        
        // Obtener los tramos que comienzan desde el origen directamente desde el mapa
        List<Tramo> tramosDesdeOrigen = mapaTramos.get(origen);
        
        if (tramosDesdeOrigen != null) {
            for (Tramo tramo : tramosDesdeOrigen) {
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
                        rutasMap.put(origen + "-" + claveIntermedia, nuevaRuta); // Guardar la nueva ruta en el mapa
                    }
                }
            }
        }
        
        //visitados.remove(origen);  // Eliminar origen de los visitados para futuras recursiones       
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
            cromosoma.setFitness();
        }
        // Elitismo: Mantén los mejores cromosomas (por ejemplo, el 10% mejor) sin alterarlos
        int elitismoSize = (int) (0.1 * poblacion.size());
        Collections.sort(poblacion, Comparator.comparingDouble(Cromosoma::getFitness));
        //ordenar por fitness
        
        // Añadir los mejores cromosomas directamente a la nueva población
        for (int i = 0; i < elitismoSize; i++) {
            nuevaPoblacion.add(poblacion.get(i));
        }
        
        // Generar el resto de la nueva población
        for (int i = elitismoSize; i < poblacion.size(); i++) {
            Cromosoma padre1 = seleccionarPorRuleta(poblacion);
            Cromosoma padre2 = seleccionarPorRuleta(poblacion);
            Cromosoma hijo = cruzar(padre1, padre2);
            mutar(hijo);
            
            //Recalular fitness hijo
            hijo.setTiempoTotal(); //actualizar tiempo total de cromosoma
            hijo.setFitness();
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
            if (individuo.getFitness()> mejorIndividuo.getFitness()) {
                mejorIndividuo = individuo;
            }
        }
        return mejorIndividuo;
    }

    public static Cromosoma seleccionarAleatorio(ArrayList<Cromosoma> poblacion) {
        int indice = random.nextInt(poblacion.size());
        return poblacion.get(indice);
    }


    //ruleto bota la funcion cromosoma?
    public static Cromosoma seleccionarPorRuleta(ArrayList<Cromosoma> poblacion) {
        // Paso 1: Calcular la suma total de fitness
        double sumaTotalFitness = 0.0;
        for (Cromosoma cromosoma : poblacion) {
            sumaTotalFitness += cromosoma.getFitness();
        }

        // Paso 2: Generar un número aleatorio entre 0 y la suma total de fitness
        double randomValue = random.nextDouble() * sumaTotalFitness;

        // Paso 3: Seleccionar el cromosoma correspondiente
        double sumaParcial = 0.0;
        for (Cromosoma cromosoma : poblacion) {
            sumaParcial += cromosoma.getFitness();
            if (sumaParcial >= randomValue) {
                return cromosoma;  // Seleccionar este cromosoma
            }
        }

        // En caso de error numérico, retornar el último cromosoma
        return poblacion.get(poblacion.size() - 1);
    }

    private static void imprimirRuta(Cromosoma mejorIndividuo, Venta pedido) {
        System.out.println("    Ruta Optima: ");
        HashMap<String, Tramo> rutaMap = mejorIndividuo.getRutaMap();
        String ubigeoActual = pedido.getUbigeoOrigen();

        List<Tramo> rutaOrdenada = new ArrayList<>();
//        System.out.println("    Sin ordenar: ");
        // Imprimir la ruta en orden
        for (Tramo tramo : rutaMap.values()) {
//            System.out.println("    Desde: " + tramo.getUbigeoOrigen() + " Hasta: " + tramo.getUbigeoDestino());
        } 
        
         System.out.println("Ordenado: ");
        // Continuar hasta llegar al destino o si no se encuentra una ruta válida
        while (!ubigeoActual.equals(pedido.getUbigeoDestino())) {
            boolean tramoEncontrado = false;

            for (Tramo tramo : rutaMap.values()) {
                if (tramo.getUbigeoOrigen().equals(ubigeoActual)) {
                    rutaOrdenada.add(tramo);
                    ubigeoActual = tramo.getUbigeoDestino();
                    tramoEncontrado = true;
                    break;
                }
            }

            if (!tramoEncontrado) {
                System.out.println("    Ruta incompleta. No se pudo llegar al destino.");
                return;  // Salir si no se encuentra el siguiente tramo
            }
        }

        // Imprimir la ruta en orden
        for (Tramo tramo : rutaOrdenada) {
            System.out.println("    Desde: " + tramo.getUbigeoOrigen() + " Hasta: " + tramo.getUbigeoDestino());
        }
    }
}
