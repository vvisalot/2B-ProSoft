package algorithm;

import model.Bloqueo;
import model.Oficina;
import model.Tramo;
import utils.CalculaDistancia;

import java.util.*;


public class GrafoTramos {
    private final HashMap<Tramo, Set<Tramo>> grafo = new HashMap<Tramo, Set<Tramo>>();

    public void agregarArista(Tramo tramoOrigen, Set<Tramo> tramosSiguientes) {
        grafo.put(tramoOrigen, tramosSiguientes);
    }

    public void imprimirGrafo() {
        for (Tramo tramo : grafo.keySet()) {
            imprimirTramosVecinos(tramo);
        }
    }

    public void imprimirTramosVecinos(Tramo origen){
        var vecinos = obtenerVecinos(origen);
        if (vecinos != null && !vecinos.isEmpty()) {
            var tramoString = "(" + origen.getOrigen().getCodigo() + " - " + origen.getDestino().getCodigo() + ")";
            System.out.print("Vecinos de " + tramoString + ": ");
            for (Tramo vecino : vecinos) {
                tramoString = "(" + vecino.getOrigen().getCodigo() + " - " + vecino.getDestino().getCodigo() +")";
                System.out.print(tramoString + " | ");
            }
            System.out.println();
        } else {
            System.out.println("Sin vecinos");
        }
    }

    public Set<Tramo> obtenerVecinos(Tramo origen) {
        return grafo.get(origen);
    }

    public List<Tramo> obtenerRutaMasCorta(Oficina origen, Oficina destino) {
        PriorityQueue<Tramo> frontera = new PriorityQueue<>(Comparator.comparingDouble(tramo -> calcularHeuristica(tramo, destino)));

        Map<Tramo, Tramo> cameFrom = new HashMap<>();
        Map<Tramo, Double> costeActual = new HashMap<>();

        // Inicializar el primer tramo directo desde la oficina de origen
        List<Tramo> tramosIniciales = buscarTramosConOrigen(origen);

        // Si hay tramos que van directo al destino, devolverlos inmediatamente
        for (Tramo tramoInicial : tramosIniciales) {
            if (tramoInicial.getDestino().equals(destino)) {
                List<Tramo> rutaDirecta = new ArrayList<>();
                rutaDirecta.add(tramoInicial);
                return rutaDirecta;
            }
            frontera.add(tramoInicial);
            costeActual.put(tramoInicial, 0.0);
        }

        while (!frontera.isEmpty()) {
            Tramo actual = frontera.poll();

            // Condición de parada mejorada: si ya encontramos el destino
            if (actual.getDestino().equals(destino)) {
                return reconstruirRuta(cameFrom, actual);
            }

            // Revisar los tramos vecinos
            for (Tramo vecino : obtenerVecinos(actual)) {
                double nuevoCosto = costeActual.get(actual) + vecino.getDistancia();

                if (!costeActual.containsKey(vecino) || nuevoCosto < costeActual.get(vecino)) {
                    costeActual.put(vecino, nuevoCosto);
                    frontera.add(vecino);
                    cameFrom.put(vecino, actual);
                }
            }
        }

        return null;  // No se encontró ruta
    }

    private List<Tramo> buscarTramosConOrigen(Oficina origen) {
        // Busca los tramos que salen de la oficina de origen en el grafo
        List<Tramo> tramos = new ArrayList<>();
        for (Tramo tramo : grafo.keySet()) {
            if (tramo.getOrigen().equals(origen)) {
                tramos.add(tramo);
            }
        }
        return tramos;
    }



    private Tramo buscarTramoConOrigen(Oficina origen) {
        // Busca el tramo que sale de la oficina de origen en el grafo
        for (Tramo tramo : grafo.keySet()) {
            if (tramo.getOrigen().equals(origen)) {
                return tramo;
            }
        }
        return null;  // Si no encuentra tramo con la oficina origen
    }

    private double calcularHeuristica(Tramo tramoActual, Oficina destinoFinal) {
        // Calculamos la distancia desde el destino del tramo actual hasta el destino final
        return CalculaDistancia.calcular(
                tramoActual.getDestino().getLatitud(), tramoActual.getDestino().getLongitud(),
                destinoFinal.getLatitud(), destinoFinal.getLongitud());
    }

    private List<Tramo> reconstruirRuta(Map<Tramo, Tramo> cameFrom, Tramo actual) {
        List<Tramo> ruta = new ArrayList<>();
        while (actual != null) {
            ruta.add(actual);
            actual = cameFrom.get(actual);
        }
        Collections.reverse(ruta);  // Invertimos la ruta para que vaya de origen a destino
        return ruta;
    }

    public void agregarBloqueo(Bloqueo bloqueo, String codigoOrigen, String codigoDestino) {
        for(var tramo : grafo.keySet()){
            if(tramo.getOrigen().getCodigo().equals(codigoOrigen) && tramo.getDestino().getCodigo().equals(codigoDestino)){
                tramo.getBloqueos().add(bloqueo);
                break;
            }
        }
    }

    public void imprimirBloqueos(){
        for(var tramo : grafo.keySet()){
            var bloqueos = tramo.getBloqueos();
            if(!bloqueos.isEmpty()){
                var tramoString = "(" + tramo.getOrigen().getCodigo() + " - " + tramo.getDestino().getCodigo() + ")";
                System.out.print("Bloqueos de " + tramoString + ": ");
                for (Bloqueo bloqueo : tramo.getBloqueos()) {
                    var tiempoBloqueo = "(" + bloqueo.getFechaHoraInicio() + " - " + bloqueo.getFechaHoraFin() +")";
                    System.out.print(tiempoBloqueo + " | ");
                }
                System.out.println();
            }
        }
    }

    public void imprimirTodosLosTramos() {
        for (Tramo tramo : grafo.keySet()) {
            System.out.println("Tramo: " + tramo.getOrigen().getCodigo() + " => " + tramo.getDestino().getCodigo()
                    + " | Distancia: " + tramo.getDistancia());
        }
    }
}

