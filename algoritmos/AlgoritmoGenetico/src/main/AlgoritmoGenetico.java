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

    public Double getTasaMutacion() {
        return tasaMutacion;
    }

    public void setTasaMutacion(Double tasaMutacion) {
        this.tasaMutacion = tasaMutacion;
    }

    public double generarAlgoritmoGenético(int numGeneracion, Double tasaMutacion, Venta pedido, List<Camion> camioniones) throws IOException{
        //Venta pedido = new Venta(fechaEnvio, "040101", "230101", 4, "000786");
        //Camion camion = new Camion("A001",90, "040101");
        List<Tramo> tramos = LeerDatos.leerTramos("archivos/tramos.txt");
        List<Bloqueo> bloqueos = LeerDatos.leerBloqueos("archivos/bloqueos/c.1inf54.24-2.bloqueo.03.txt");
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
            poblacion = evolucionarPoblacion(poblacion,tasaMutacion,mapaTramos,bloqueos,fechaEnvio);
            //Cromosoma mejorIndividuo = seleccionarMejor(poblacion);
            //System.out.println("Generación " + gen + ": Mejor tiempo = " + mejorIndividuo.getTiempoTotal());
            
            System.out.println("Generación " + gen + ":");
            for (Cromosoma cromosoma : poblacion) {
//                System.out.println("  Tiempo total: " + cromosoma.getTiempoTotal());
            }
            
            if(!poblacion.isEmpty()){
                Cromosoma mejorIndividuo = seleccionarMejor(poblacion);
                System.out.println("  Mejor tiempo: " + mejorIndividuo.getTiempoTotal());
                imprimirRuta(mejorIndividuo);
                if(mejorTiempo>mejorIndividuo.getTiempoTotal()) mejorTiempo=mejorIndividuo.getTiempoTotal();
            }else{{
                System.out.println("No se encontró solución en la generación " + gen);
            }}

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
        // Agregar un mensaje para mostrar cuántas rutas se generaron
        System.out.println("Total de rutas generadas: " + rutasPosibles.size());
        //imprimirRutasPosibles(rutasPosibles, pedido.getUbigeoOrigen(), pedido.getUbigeoDestino());


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
                cromosoma.setOrigen(pedido.getUbigeoOrigen());
                cromosoma.setDestino(pedido.getUbigeoDestino());
                //System.out.println("Añadiendo hijo a la población inicial: " + cromosoma.getFitness());
                poblacion.add(cromosoma);  // Añadir el cromosoma a la población
            }
        }

        return poblacion;
    }

//     public static ArrayList<Cromosoma> generarPoblacionInicial(Venta pedido,
//             HashMap<String, List<Tramo>> mapaTramos, List<Camion> camioniones, List<Bloqueo> bloqueos,
//             LocalDateTime fechaEnvio) {
        
//         ArrayList<Cromosoma> poblacion = new ArrayList<>();

//         //Verificar capacidad del camión para el pedido --> solo un camion
//         Camion newCamion = new Camion(); 
//         for(Camion camion: camioniones){
//             if (pedido.getCantidad() > camion.getCapacidad()) {
// //                System.out.println("El camión no tiene capacidad suficiente para este pedido.");
//                 ///return poblacion;
//             }else{
//                 newCamion=new Camion(camion.getIdCamion(),camion.getCapacidad(),camion.getUbigeo());
//             }
//         }
        
//         // Generar todas las rutas posibles entre el origen y el destino del pedido
//         HashMap<String, List<Tramo>> rutasPosibles = generarRutas(pedido.getUbigeoOrigen(), pedido.getUbigeoDestino(), mapaTramos,null);

//         // Crear cromosomas (individuos) a partir de las rutas
//         for (Map.Entry<String, List<Tramo>> entry : rutasPosibles.entrySet()) {
//             HashMap<String, Tramo> rutaMap = new HashMap<>();
//             boolean tieneBloqueo = false;
            
//             // Verificar cada tramo antes de agregarlo           
//            for (Tramo tramo : entry.getValue()) {
//                 // Verificar si el tramo está bloqueado en la fecha de envío
//                 if (estaBloqueado(tramo, bloqueos, fechaEnvio)) {
//                     tieneBloqueo = true;  // Si hay un tramo bloqueado, no incluir esta ruta
//                     break;  // Salir del ciclo si se encuentra un tramo bloqueado
//                 }

//                 // Si no está bloqueado, agregarlo a la ruta                
//                 String clave = tramo.getUbigeoOrigen() + "-" + tramo.getUbigeoDestino();
//                 rutaMap.put(clave, tramo);
//             }
           
//             // Si no hay tramos bloqueados en esta ruta, crear el cromosoma
//             if (!tieneBloqueo) {
//                 Cromosoma cromosoma = new Cromosoma(rutaMap, newCamion);
//                 cromosoma.setTiempoTotal();  // Calcular el tiempo total del cromosoma
//                 cromosoma.setFitness();
//                 poblacion.add(cromosoma);  // Añadir el cromosoma a la población
//             }
//         }

//         return poblacion;
//     }


    
    // Método auxiliar para verificar si un tramo está bloqueado
    public static boolean estaBloqueado(Tramo tramo, List<Bloqueo> bloqueos, LocalDateTime fechaEnvio) {
        for (Bloqueo bloqueo : bloqueos) {
            if (bloqueo.getTramo().equals(tramo)) {
                // Verificar las fechas y hacer un print para depuración
                System.out.println("Verificando bloqueo: " + bloqueo.getFechaHoraInicio() + " a " + bloqueo.getFechaHoraFin());
                if (fechaEnvio.isAfter(bloqueo.getFechaHoraInicio()) && fechaEnvio.isBefore(bloqueo.getFechaHoraFin())) {
                    System.out.println("Tramo bloqueado: " + tramo.getUbigeoOrigen() + " -> " + tramo.getUbigeoDestino());
                    return true;  // El tramo está bloqueado
                }
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



    public static ArrayList<Cromosoma> evolucionarPoblacion(ArrayList<Cromosoma> poblacion,double tasaMutacion,HashMap<String, List<Tramo>> mapaTramos, 
            List<Bloqueo> bloqueos,LocalDateTime fechaEnvio) {
        ArrayList<Cromosoma> nuevaPoblacion = new ArrayList<>();

        // Actualizar el tiempo y fitness de cada cromosoma
        for (Cromosoma cromosoma : poblacion) {
            cromosoma.setTiempoTotal(); 
            cromosoma.setFitness();
        }

        // Elitismo: Mantén los mejores cromosomas (por ejemplo, el 10% mejor) sin alterarlos
        int elitismoSize = (int) (0.05 * poblacion.size());
        Collections.sort(poblacion, Comparator.comparingDouble(Cromosoma::getFitness).reversed()); 

        // Añadir los mejores cromosomas directamente a la nueva población
        for (int i = 0; i < elitismoSize; i++) {
            nuevaPoblacion.add(poblacion.get(i));
        }

        // Generar el resto de la nueva población
        while (nuevaPoblacion.size() < poblacion.size()) {
            Cromosoma padre1 = seleccionarPorRuleta(poblacion);
            Cromosoma padre2 = seleccionarPorRuleta(poblacion);

            // Intentar cruzar los padres
            Cromosoma[] hijos = cruzar(padre1, padre2);

            if (hijos != null) {
                // Si los hijos son válidos, añadirlos a la nueva población
                for (Cromosoma hijo : hijos) {
                    if (hijo != null) {
                        mutar(hijo,tasaMutacion,mapaTramos,bloqueos,fechaEnvio);  // Mutar los hijos para añadir diversidad
                        hijo.setTiempoTotal(); 
                        hijo.setFitness();      
                        nuevaPoblacion.add(hijo);
                    }
                }
            } else {
                // Si no se generan hijos válidos, mutar y devolver a los padres

                mutar(padre1,tasaMutacion,mapaTramos,bloqueos,fechaEnvio);  
                mutar(padre2, tasaMutacion,mapaTramos,bloqueos,fechaEnvio);  
                nuevaPoblacion.add(padre1);
                nuevaPoblacion.add(padre2);
            }

            // Si se ha alcanzado el tamaño de la nueva población, salir del bucle
            if (nuevaPoblacion.size() >= poblacion.size()) {
                break;
            }
        }

        return nuevaPoblacion;
    }


    

    public static Cromosoma[] cruzar(Cromosoma padre1, Cromosoma padre2) {
        // Ordenar tramos de los padres
        List<Tramo> tramosOrdenadosPadre1 = ordenarTramos(padre1.getRutaMap(), padre1.getOrigen(), padre2.getDestino());
        List<Tramo> tramosOrdenadosPadre2 = ordenarTramos(padre2.getRutaMap(), padre2.getOrigen(), padre2.getDestino());

        // Inicializar rutas de los hijos
        List<Tramo> hijo1Rutas = new ArrayList<>();
        List<Tramo> hijo2Rutas = new ArrayList<>();
  
        List<String> puntosComunes = new ArrayList<>();
        
        // Buscar puntos comunes en los tramos de ambos padres
        for (Tramo tramo1 : tramosOrdenadosPadre1) {
            for (Tramo tramo2 : tramosOrdenadosPadre2) {
                // Si encontramos una ciudad intermedia en común, la agregamos a la lista
                String puntoComun = tramo1.getUbigeoDestino();
                if (tramo1.getUbigeoDestino().equals(tramo2.getUbigeoDestino()) && !puntoComun.equals(padre1.getDestino())) {
                    //System.out.println("Mi punto Comun es: " + puntoComun);
                    puntosComunes.add(puntoComun);
                }
            }
        }
        
        // Si no hay puntos comunes, retornar hijos nulos
        if (puntosComunes.isEmpty()) {
            //System.out.println("No hay puntos comunes, salir");
            return null;  // No se puede realizar el cruce
        }

        // Elegir un punto común al azar si hay más de uno
        String puntoCruce = puntosComunes.get(random.nextInt(puntosComunes.size()));
        //System.out.println("Mi punto de cruce es: " + puntoCruce);
        // Crear hijo1: agregar tramos de padre1 hasta el punto común, y de padre2 desde el punto de cruce hasta el destino
        for (Tramo tramo : tramosOrdenadosPadre1) {
            hijo1Rutas.add(tramo);
            if (tramo.getUbigeoDestino().equals(puntoCruce)) break;
        }
        
        boolean puntoEncontrado = false;
        for (Tramo tramo : tramosOrdenadosPadre2) {
            if (puntoEncontrado || tramo.getUbigeoOrigen().equals(puntoCruce)) {
                // Verificar si el tramo ya está en hijo1Rutas
                if (!hijo1Rutas.contains(tramo)) {
                    hijo1Rutas.add(tramo);
                }
                puntoEncontrado = true;
            }
        }

        // Crear hijo2: agregar tramos de padre2 hasta el punto común, y de padre1 desde el punto de cruce hasta el destino
        for (Tramo tramo : tramosOrdenadosPadre2) {
            hijo2Rutas.add(tramo);
            if (tramo.getUbigeoDestino().equals(puntoCruce)) break;
        }
        puntoEncontrado = false;
        for (Tramo tramo : tramosOrdenadosPadre1) {
            if (puntoEncontrado || tramo.getUbigeoOrigen().equals(puntoCruce)) {
                // Verificar si el tramo ya está en hijo2Rutas
                if (!hijo2Rutas.contains(tramo)) {
                    hijo2Rutas.add(tramo);
                }
                puntoEncontrado = true;
            }
        }

        // Verificar si los hijos generados tienen rutas válidas
        if (!validarContinuidad(hijo1Rutas, padre1.getOrigen(), padre1.getDestino()) ||
            !validarContinuidad(hijo2Rutas, padre2.getOrigen(), padre2.getDestino())) {
            // Si alguna ruta no es válida, devolver hijos nulos
            return null;
        }        
        
        // Crear los cromosomas para los hijos y devolverlos
        HashMap<String, Tramo> hijo1RutaMap = convertirListaAMap(hijo1Rutas);
        HashMap<String, Tramo> hijo2RutaMap = convertirListaAMap(hijo2Rutas);

        Cromosoma hijo1 = new Cromosoma(hijo1RutaMap, new Camion(padre1.getIdCamion()));
        Cromosoma hijo2 = new Cromosoma(hijo2RutaMap, new Camion(padre2.getIdCamion()));
        
        hijo1.setOrigen(padre1.getOrigen());
        hijo1.setDestino(padre1.getDestino());
        hijo2.setOrigen(padre2.getOrigen());
        hijo2.setDestino(padre2.getDestino());
        
        return new Cromosoma[]{hijo1, hijo2};
    }


    // Validar si la ruta es continua desde el origen hasta el destino
    private static boolean validarContinuidad(List<Tramo> ruta, String origen, String destino) {
        String ubigeoActual = origen;

        for (Tramo tramo : ruta) {
            if (!tramo.getUbigeoOrigen().equals(ubigeoActual)) {
                return false;  // La ruta no es continua
            }
            ubigeoActual = tramo.getUbigeoDestino();
        }

        // Verificar que el último tramo llegue al destino
        return ubigeoActual.equals(destino);
    }
    
    
    // Función auxiliar para convertir una lista de tramos en un mapa de rutas
    public static HashMap<String, Tramo> convertirListaAMap(List<Tramo> listaTramos) {
        HashMap<String, Tramo> mapaTramos = new HashMap<>();
        for (Tramo tramo : listaTramos) {
            String clave = tramo.getUbigeoOrigen() + "-" + tramo.getUbigeoDestino();
            mapaTramos.put(clave, tramo);
        }
        return mapaTramos;
    }


    // public static void mutar(Cromosoma individuo) {
        
    //     // Posibilidad de realizar más de una mutación (por ejemplo, entre 1 y 3 mutaciones)
    //     int numMutaciones = 1 + random.nextInt(3);
        
    //     for (int m = 0; m < numMutaciones; m++) {
    //         List<String> claves = new ArrayList<>(individuo.getRutaMap().keySet());
    //         int i = random.nextInt(claves.size());
    //         int j = random.nextInt(claves.size());

    //         // Intercambiar tramos
    //         Tramo tramo1 = individuo.getRutaMap().get(claves.get(i));
    //         Tramo tramo2 = individuo.getRutaMap().get(claves.get(j));

    //         // Realizar el swap
    //         individuo.getRutaMap().put(claves.get(i), tramo2);
    //         individuo.getRutaMap().put(claves.get(j), tramo1);
    //     }
    // }

    public static void mutar(Cromosoma individuo,double tasaMutacion,HashMap<String, List<Tramo>> mapaTramos,
            List<Bloqueo> bloqueos,LocalDateTime fechaEnvio) {
        
        // Verificar si la mutación ocurre en función de la tasa de mutación
        if (random.nextDouble() < tasaMutacion) { // Cambié la comparación
            // Obtener los tramos del cromosoma y ordenarlos
            List<Tramo> tramosOrdenados = ordenarTramos(individuo.getRutaMap(), individuo.getOrigen(), individuo.getDestino());

            // Solo mutar si hay más de 2 tramos (es decir, si hay al menos un tramo intermedio entre origen y destino)
            if (tramosOrdenados.size() > 2) {
                // Seleccionar un tramo aleatorio, excluyendo el origen (primer índice) y destino (último índice)
                int indice = 1 + random.nextInt(tramosOrdenados.size() - 2); // Evitar el primer y último tramo
                Tramo tramoSeleccionado = tramosOrdenados.get(indice);
                Tramo tramoSiguiente = tramosOrdenados.get(indice + 1); // Tramo siguiente

                // Verificar si se puede eliminar el tramo seleccionado
                if (tramoSeleccionado.getUbigeoDestino().equals(tramoSiguiente.getUbigeoOrigen())) {
                    // Crear un nuevo tramo que conecte el tramo anterior con el siguiente
                    if(verificarConexion(tramoSeleccionado.getUbigeoOrigen(),tramoSiguiente.getUbigeoDestino(),mapaTramos,bloqueos,fechaEnvio)){
                        Tramo nuevoTramo = buscarTramo(tramoSeleccionado.getUbigeoOrigen(), tramoSiguiente.getUbigeoDestino(),mapaTramos);
                        // Actualizar el mapa de rutas
                        individuo.getRutaMap().put(nuevoTramo.getUbigeoOrigen() + "-" + nuevoTramo.getUbigeoDestino(), nuevoTramo);
                        // Eliminar el tramo seleccionado del mapa de rutas
                        individuo.getRutaMap().remove(tramoSeleccionado.getUbigeoOrigen() + "-" + tramoSeleccionado.getUbigeoDestino());
                        //System.out.println("Muto");
                    }
                } else {
                    // Opción: podrías manejar el caso en el que no se puede conectar, si lo deseas
                    //System.out.println("No se puede mutar");
                }
            }
        }
    }
    

        // Función para verificar si existe una conexión válida entre dos ubicaciones
     private static boolean verificarConexion(String origen, String destino, HashMap<String, List<Tramo>> mapaTramos, List<Bloqueo> bloqueos, LocalDateTime fechaEnvio) {
        // Verificar si existe una lista de tramos que parten desde el origen
        if (mapaTramos.containsKey(origen)) {
            List<Tramo> tramosDesdeOrigen = mapaTramos.get(origen);

            // Buscar un tramo con el destino especificado
            for (Tramo tramo : tramosDesdeOrigen) {
                if (tramo.getUbigeoDestino().equals(destino)) {
                    // Verificar si el tramo está bloqueado
                    if (estaBloqueado(tramo, bloqueos, fechaEnvio)) {
                        System.out.println("El tramo " + origen + " -> " + destino + " está bloqueado.");
                        return false;
                    } else {
                        // Si no está bloqueado, la conexión es válida
                        return true;
                    }
                }
            }
        }

        // Si no se encuentra un tramo con ese origen y destino, retornar falso
        //System.out.println("No se encontró un tramo entre " + origen + " y " + destino);
        return false;
    }

    
    public static Tramo buscarTramo(String origen, String destino, HashMap<String, List<Tramo>> mapaTramos){
        
        List<Tramo> tramosDesdeOrigen = mapaTramos.get(origen);
        // Buscar un tramo con el destino especificado
       for (Tramo tramo : tramosDesdeOrigen) {
           if (tramo.getUbigeoDestino().equals(destino)) {
               return tramo;
           }
       }
        return null;
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

    private static void imprimirRuta(Cromosoma mejorIndividuo) {
        System.out.println("    Ruta: ");

        // Obtener el mapa de tramos del cromosoma
        HashMap<String, Tramo> rutaMap = mejorIndividuo.getRutaMap();

        // Usar la función ordenarTramos para obtener la ruta ordenada
        List<Tramo> rutaOrdenada = ordenarTramos(rutaMap, mejorIndividuo.getOrigen(), mejorIndividuo.getDestino());

        // Crear un StringBuilder para construir la ruta en un solo formato
        StringBuilder rutaString = new StringBuilder();

        // Construir la ruta en formato "A->B->C"
        for (int i = 0; i < rutaOrdenada.size(); i++) {
            Tramo tramo = rutaOrdenada.get(i);
            rutaString.append(tramo.getUbigeoOrigen());

            // Solo añadir "->" entre tramos, excepto al final
            if (i < rutaOrdenada.size() - 1) {
                rutaString.append("->");
            } else {
                rutaString.append("->").append(tramo.getUbigeoDestino()); // Agregar el destino final
            }
        }

        // Imprimir la ruta final en el formato "A->B->C"
        System.out.println("    " + rutaString.toString());

        // Validar si la ruta está completa
        if (!rutaOrdenada.isEmpty()) {
            Tramo ultimoTramo = rutaOrdenada.get(rutaOrdenada.size() - 1);
            if (!ultimoTramo.getUbigeoDestino().equals(mejorIndividuo.getDestino())) {
                System.out.println("    Ruta incompleta. No se pudo llegar al destino.");
            }
        } else {
            System.out.println("    No se encontró una ruta válida.");
        }
    }


    public static List<Tramo> ordenarTramos(HashMap<String, Tramo> rutaMap, String origen, String destino) {
        List<Tramo> tramosOrdenados = new ArrayList<>();
        String ubigeoActual = origen;

        // Mientras no lleguemos al destino, buscar el tramo que continúa
        while (!ubigeoActual.equals(destino)) {
            boolean tramoEncontrado = false; // Variable para comprobar si se encontró un tramo
            for (Tramo tramo : rutaMap.values()) {
                if (tramo.getUbigeoOrigen().equals(ubigeoActual)) {
                    tramosOrdenados.add(tramo);
                    ubigeoActual = tramo.getUbigeoDestino();  // Actualizar el siguiente origen
                    tramoEncontrado = true; // Marcamos que se encontró un tramo
                    break;  // Salir del ciclo para procesar el siguiente tramo
                }
            }

            // Si no se encontró un tramo, salimos del bucle para evitar el infinito
            if (!tramoEncontrado) {
                System.out.println("    No se encontró un tramo desde: " + ubigeoActual);
                break; // Salimos del bucle
            }
        }

        // Devolver la lista de tramos en el orden correcto
        return tramosOrdenados;
    }
    // Método para imprimir las rutas posibles
    private static void imprimirRutasPosibles(HashMap<String, List<Tramo>> rutasPosibles, String origen, String destino) {
        System.out.println("Rutas posibles generadas:");
        for (Map.Entry<String, List<Tramo>> entry : rutasPosibles.entrySet()) {
            String key = entry.getKey(); // Esta sería la clave de la ruta
            List<Tramo> tramos = entry.getValue();

            // Ordenar los tramos usando la función ordenarTramos
            List<Tramo> tramosOrdenados = ordenarTramos(convertirListaAMap(tramos), origen, destino);

            // Imprimir la ruta en formato A->B->C
            StringBuilder rutaString = new StringBuilder();
            for (Tramo tramo : tramosOrdenados) {
                rutaString.append(tramo.getUbigeoOrigen()).append("->");
            }
            // Añadir el destino final
            rutaString.append(tramosOrdenados.get(tramosOrdenados.size() - 1).getUbigeoDestino());

            System.out.println("Ruta: " + rutaString.toString());
        }
    }
}
