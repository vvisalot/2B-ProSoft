package algorithm;

import model.Camion;
import model.Oficina;
import model.Tramo;
import model.Venta;

import java.util.*;

public class PlanificadorRutas {

    public static List<Tramo> planificarRuta(Camion camion, GrafoTramos grafoTramos, List<Oficina> almacenesPrincipales) {
        List<Venta> ventas = camion.getVentas();  // Obtener las ventas asignadas al camión
        Set<Oficina> oficinasPorVisitar = new HashSet<>();  // Oficinas donde el camión debe entregar

        for (Venta venta : ventas) {
            oficinasPorVisitar.add(venta.getDestino());
        }

        Oficina oficinaActual = obtenerAlmacenPrincipalAleatorio(almacenesPrincipales);
        List<Tramo> rutaRecorrida = new ArrayList<>();

        //LOOP DE OFICINAS A VISITAR
        return rutaRecorrida;
    }

    // Método para obtener un almacén principal al azar
    private static Oficina obtenerAlmacenPrincipalAleatorio(List<Oficina> almacenesPrincipales) {
        Random random = new Random();
        return almacenesPrincipales.get(random.nextInt(almacenesPrincipales.size()));
    }
}

