package algorithm;

import model.*;

import java.util.*;

public class PlanificadorRutas {

    public static List<Tramo> planificarRuta(Camion camion, GrafoTramos grafoTramos, List<Oficina> almacenesPrincipales) {
        List<Paquete> ventas = camion.getPaquetes();  // Obtener las ventas asignadas al camión
        Set<Oficina> oficinasPorVisitar = new HashSet<>();  // Oficinas donde el camión debe entregar

//        for (Venta venta : ventas) {
//            oficinasPorVisitar.add(venta.getDestino());
//        }

        Oficina oficinaInicial = obtenerAlmacenPrincipalAleatorio(almacenesPrincipales);
        List<Tramo> rutaRecorrida = new ArrayList<>();

        //LOOP DE OFICINAS A VISITAR
        for (Oficina oficinaPorVisitar : oficinasPorVisitar) {
            List<Tramo> ruta = grafoTramos.obtenerRutaMasCorta(oficinaInicial, oficinaPorVisitar);
            rutaRecorrida.addAll(ruta);
            oficinaInicial = oficinaPorVisitar;
        }
        return rutaRecorrida;
    }

    private static Map<Camion,List<Tramo>> planificarRutas(Map<Oficina, List<Camion>> mapaCamionesPorCentral){
        Map<Camion,List<Tramo>> mapaRutasPorCamion = new HashMap<>();
        for(var entry : mapaCamionesPorCentral.entrySet()){
            for(var camion: entry.getValue()){
                var paquetes = camion.getPaquetes();
            }
        }
        return mapaRutasPorCamion;
    }
    // Método para obtener un almacén principal al azar
    private static Oficina obtenerAlmacenPrincipalAleatorio(List<Oficina> almacenesPrincipales) {
        Random random = new Random();
        return almacenesPrincipales.get(random.nextInt(almacenesPrincipales.size()));
    }
}

