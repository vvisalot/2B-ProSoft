package algorithm;
import model.Camion;
import model.Oficina;
import model.Tramo;
import model.Venta;

import java.util.*;

public class PlanificadorRutas {

    // Método principal para que el camión siga la mejor ruta para entregar todas sus ventas
    public static List<Tramo> planificarRuta(Camion camion, GrafoTramos grafoTramos) {
        List<Venta> ventas = camion.getVentas();  // Obtener las ventas asignadas al camión
        Oficina oficinaActual = ventas.get(0).getOrigen();  // La oficina inicial del camión
        Set<Oficina> oficinasPorVisitar = new HashSet<>();  // Oficinas donde el camión debe entregar

        // Obtener todas las oficinas de destino
        for (Venta venta : ventas) {
            oficinasPorVisitar.add(venta.getDestino());
        }

        List<Tramo> rutaRecorrida = new ArrayList<>();  // La ruta final que recorrerá el camión

        while (!oficinasPorVisitar.isEmpty()) {
            Tramo mejorTramo = null;
            Oficina siguienteDestino = null;
            double mejorDistancia = Double.MAX_VALUE;

            // Evaluar cada destino pendiente para encontrar la mejor ruta
            for (Oficina destino : oficinasPorVisitar) {
                // Obtener el tramo más conveniente para llegar al destino
                Tramo tramo = buscarMejorTramo(oficinaActual, destino, grafoTramos);
                if (tramo != null && tramo.getDistancia() < mejorDistancia) {
                    mejorDistancia = tramo.getDistancia();
                    mejorTramo = tramo;
                    siguienteDestino = destino;
                }
            }

            if (mejorTramo != null) {
                rutaRecorrida.add(mejorTramo);  // Añadir el tramo a la ruta final
                oficinaActual = siguienteDestino;  // Actualizar la oficina actual
                oficinasPorVisitar.remove(siguienteDestino);  // Marcar la oficina como visitada
                System.out.println("Camión " + camion.getCodigo() + " entrega en " + oficinaActual.getCodigo());
            }
        }
        return rutaRecorrida;
    }

    //Buscamos una conexion entre oficinas asegurando que no haya bloqueos
    private static Tramo buscarMejorTramo(Oficina origen, Oficina destino, GrafoTramos grafoTramos) {
        Set<Tramo> tramosVecinos = grafoTramos.obtenerVecinos(new Tramo(origen, null, 0));

        for (Tramo tramo : tramosVecinos) {
            if (tramo.getDestino().equals(destino) && tramo.getBloqueos().isEmpty()) {
                return tramo;  // Si no hay bloqueos y conecta con el destino, devolver el tramo
            }
        }
        return null;
    }

    // Encontrar la mejor ruta para entregar todas las ventas , utilizando el algoritmo A*
    public static List<Tramo> encontrarRutaOptima(Oficina origen, Oficina destino, GrafoTramos grafoTramos) {
        PriorityQueue<Tramo> colaPrioridad = new PriorityQueue<>(Comparator.comparingDouble(tramo -> tramo.getCostoTotal(0)));
        Map<Oficina, Double> costoActual = new HashMap<>();
        Map<Oficina, Tramo> previos = new HashMap<>();
        Set<Oficina> visitados = new HashSet<>();

        // Inicializar con los tramos que salen de la oficina de origen
        Set<Tramo> tramosIniciales = grafoTramos.obtenerVecinos(new Tramo(origen, destino, 0));



        for (Tramo tramoInicial : tramosIniciales) {
            tramoInicial.setCostoReal(0);  // El costo desde el inicio es 0
            double heuristica = calcularHeuristica(tramoInicial.getDestino(), destino);
            tramoInicial.setCostoTotal(tramoInicial.getDistancia() + heuristica);  // Costo total = distancia + heurística
            colaPrioridad.add(tramoInicial);
            costoActual.put(tramoInicial.getDestino(), tramoInicial.getDistancia());
        }

        // Búsqueda de la mejor ruta
        while (!colaPrioridad.isEmpty()) {
            Tramo tramoActual = colaPrioridad.poll();
            Oficina oficinaActual = tramoActual.getDestino();

            // Si llegamos al destino
            if (oficinaActual.equals(destino)) {
                return reconstruirRuta(previos, origen, destino);
            }

            if (visitados.contains(oficinaActual)) {
                continue;
            }
            visitados.add(oficinaActual);

            // Explorar los vecinos del tramo actual
            Set<Tramo> vecinos = grafoTramos.obtenerVecinos(tramoActual);
            for (Tramo tramoVecino : vecinos) {
                if (tramoVecino.getBloqueos().isEmpty()) {
                    Oficina oficinaVecina = tramoVecino.getDestino();
                    double nuevoCosto = costoActual.get(tramoActual.getOrigen()) + tramoVecino.getDistancia();

                    if (!costoActual.containsKey(oficinaVecina) || nuevoCosto < costoActual.get(oficinaVecina)) {
                        costoActual.put(oficinaVecina, nuevoCosto);
                        double heuristica = calcularHeuristica(oficinaVecina, destino);
                        double costoTotal = tramoVecino.getCostoTotal(heuristica);
                        tramoVecino.setCostoTotal(costoTotal);
                        colaPrioridad.add(tramoVecino);
                        previos.put(oficinaVecina, tramoVecino);  // Guardar el tramo previo
                    }
                }
            }
        }

        // Si no se encuentra una ruta
        return null;
    }

    // Heurística: calcular la distancia directa entre dos oficinas (latitud y longitud)
    private static double calcularHeuristica(Oficina origen, Oficina destino) {
        double lat1 = origen.getLatitud();
        double lon1 = origen.getLongitud();
        double lat2 = destino.getLatitud();
        double lon2 = destino.getLongitud();

        // Calculamos la distancia euclidiana como una heurística simple
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
    }

    // Reconstruir la ruta óptima desde el destino hasta el origen
    private static List<Tramo> reconstruirRuta(Map<Oficina, Tramo> previos, Oficina origen, Oficina destino) {
        List<Tramo> ruta = new ArrayList<>();
        Oficina paso = destino;

        while (paso != null && !paso.equals(origen)) {
            Tramo tramo = previos.get(paso);
            if (tramo != null) {
                ruta.add(0, tramo);  // Insertamos al principio para obtener la ruta en orden
                paso = tramo.getOrigen();
            } else {
                break;
            }
        }
        return ruta;
    }
}

