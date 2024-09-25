package algorithm;

import model.Oficina;
import model.Tramo;
import utils.CalculaDistancia;

import java.util.*;

public class GrafoOficinas {
    private final HashMap<Oficina, Set<Oficina>> grafo = new HashMap<Oficina, Set<Oficina>>();

    public void agregarArista(Tramo tramo, boolean biDirectional) {
        if (obtenerVecinos(tramo.getOrigen()) == null) {
            agregarVertice(tramo.getOrigen());
        }

        if (obtenerVecinos(tramo.getDestino()) == null) {
            agregarVertice(tramo.getDestino());
        }
        obtenerVecinos(tramo.getOrigen()).add(tramo.getDestino());

        if (biDirectional)
            obtenerVecinos(tramo.getDestino()).add(tramo.getOrigen());

    }
    public void agregarVertice(Oficina origen) {
        grafo.put(origen, new HashSet<Oficina>());
    }

    public Set<Oficina> obtenerVecinos(Oficina origen) {
        return grafo.get(origen);
    }

    public void imprimirVecinosPorCodigo(Oficina oficina) {
        Set<Oficina> vecinos = obtenerVecinos(oficina);

        if (vecinos != null && !vecinos.isEmpty()) {
            System.out.print("Vecinos de " + oficina.getCodigo() + ": ");
            for (Oficina vecino : vecinos) {
                System.out.print(vecino.getCodigo() + ",");
            }
            System.out.println();
        } else {
            System.out.println("Sin vecinos");
        }
    }



    public List<Oficina> obtenerRutaMasCorta(Oficina origen, Oficina destino) {
        //Cola con prioridad (función de prioridad es la heurística de cuánto le falta por llegat)
        PriorityQueue<Oficina> frontera = new PriorityQueue<>(Comparator.comparingDouble(oficina -> calcularHeuristica(oficina, destino)));

        Map<Oficina, Oficina> cameFrom = new HashMap<>();
        Map<Oficina, Double> costeActual = new HashMap<>();

        frontera.add(origen);
        costeActual.put(origen, 0.0);

        while (!frontera.isEmpty()) {
            Oficina actual = frontera.poll();

            if (actual.equals(destino)) { //retornar si ya llegó
                return reconstruirRuta(cameFrom, actual);
            }

            for (Oficina vecino : obtenerVecinos(actual)) {
                double nuevoCosto = costeActual.get(actual) + CalculaDistancia.calcular(
                        actual.getLatitud(), actual.getLongitud(),
                        vecino.getLatitud(), vecino.getLongitud());

                if (!costeActual.containsKey(vecino) || nuevoCosto < costeActual.get(vecino)) {
                    costeActual.put(vecino, nuevoCosto);
                    frontera.add(vecino);
                    cameFrom.put(vecino, actual);
                }
            }
        }
        return null; //No hubo ruta (poco probable)
    }

    private double calcularHeuristica(Oficina origen, Oficina destino) {
        return CalculaDistancia.calcular(origen.getLatitud(), origen.getLongitud(),
                destino.getLatitud(), destino.getLongitud());
    }

    private List<Oficina> reconstruirRuta(Map<Oficina, Oficina> cameFrom, Oficina actual) {
        List<Oficina> ruta = new ArrayList<>();
        while (actual != null) {
            ruta.add(actual);
            actual = cameFrom.get(actual);
        }
        Collections.reverse(ruta); // Invertir la ruta de fin a origen
        return ruta;
    }
}

