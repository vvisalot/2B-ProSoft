package algorithm;

import model.Oficina;
import model.Tramo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
}

