package algorithm;

import model.Bloqueo;
import model.Oficina;
import model.Tramo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


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
}

