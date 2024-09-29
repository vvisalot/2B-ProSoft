package algorithm;

import model.*;

import java.util.*;
import java.util.stream.Collectors;

public class AsignadorVentas {

    //Se asignaran las ventas segun la cercania a los almacenes principales en Lima, Arequipa y Trujillo
    public static void asignarVentasAzar(List<Camion> camiones, List<Venta> ventas) {
        Random random = new Random();

        for (Venta venta : ventas) {
            int cantidadRestante = venta.getCantidad();  // Cantidad restante por asignar
            List<Camion> camionesDisponibles = new ArrayList<>(camiones);  // Copia de la lista de camiones

            while (cantidadRestante > 0 && !camionesDisponibles.isEmpty()) {
                Camion camionSeleccionado = camionesDisponibles.get(random.nextInt(camionesDisponibles.size()));

                int capacidadDisponible = camionSeleccionado.getCapacidad() - camionSeleccionado.getCargaActual();

                if (capacidadDisponible > 0) {
                    int cantidadAsignada = Math.min(capacidadDisponible, cantidadRestante);  // Asignar la mayor cantidad posible

                    camionSeleccionado.setCargaActual(camionSeleccionado.getCargaActual() + cantidadAsignada);
                    cantidadRestante -= cantidadAsignada;

                    var paquete = new Paquete(venta,cantidadAsignada);
                    camionSeleccionado.agregarPaquete(paquete);  // Agregar la venta al camión
//                    System.out.println("Asignado " + cantidadAsignada + " unidades de la venta al camión " + camionSeleccionado.getCodigo() +
//                            " (Carga actual: " + camionSeleccionado.getCargaActual() + "/" + camionSeleccionado.getCapacidad() + ")");

                    if (capacidadDisponible < cantidadRestante) {
                        camionesDisponibles.remove(camionSeleccionado);  // Quitar el camión si ya no tiene capacidad disponible
                    }
                } else {
                    camionesDisponibles.remove(camionSeleccionado);  // Remover si no tiene capacidad
                }
            }

            if (cantidadRestante > 0) {
                continue;
                //System.out.println("No se pudo asignar completamente la venta. Cantidad restante: " + cantidadRestante + " unidades");
            }
        }
        // Calcular la capacidad total restante en los camiones
        int capacidadRestanteTotal = 0;
        for (Camion camion : camiones) {
            capacidadRestanteTotal += (camion.getCapacidad() - camion.getCargaActual());
        }

        // Imprimir la cantidad total de capacidad restante
        System.out.println("\nCapacidad total restante en los camiones: " + capacidadRestanteTotal + " unidades.");
    }


    public static Map<Oficina,List<Camion>> asignarVentasGreedy(List<Camion> camiones, List<Venta> ventas, List<Oficina> almacenesPrincipales, GrafoTramos grafoTramos) {
//        List<Venta> ventasAsignadas = new ArrayList<>();
        Map<Oficina,List<Camion>> mapaCamionesPorCentral = new HashMap<>();

        for (Camion camion : camiones) {
            // Solo corre una vez para inicializar cada Central
            mapaCamionesPorCentral.computeIfAbsent(camion.getPosicionActual(), k -> new ArrayList<>());
            // Agregar camion a la lista de camiones de la central
            mapaCamionesPorCentral.get(camion.getPosicionActual()).add(camion);
        }

        // Calculamos el tiempo para cada venta desde cada almacen principal
        Map<Oficina,Map<Venta,Double>> mapaTiempoPorVentaPorOficina = new HashMap<>();
        for (var almacen : almacenesPrincipales){
            Map<Venta,Double> mapaTiempoPorVenta = new HashMap<>();
            for (var venta : ventas){
                var mejorRuta = new Ruta(grafoTramos.obtenerRutaMasCorta(almacen, venta.getDestino()));
                var tiempo = mejorRuta.calcularTiempoRuta();
                mapaTiempoPorVenta.put(venta, tiempo);
            }
            mapaTiempoPorVentaPorOficina.put(almacen, mapaTiempoPorVenta);
        }

        // Asignamos las ventas a los camiones
        // Comparar el tiempo para cada venta y nos quedamos con el mejor de los n almacenes
        for (Venta venta : ventas) {
            int cantidadRestante = venta.getCantidad();  // Cantidad restante por asignar
            while (cantidadRestante > 0) {
                Camion camionSeleccionado = seleccionarCamion(venta,mapaCamionesPorCentral,mapaTiempoPorVentaPorOficina);
                if(camionSeleccionado == null){
                    System.out.println("No se pudo asignar completamente la venta. Cantidad restante: " + cantidadRestante + " unidades");
                    break;
                }
                int capacidadDisponible = camionSeleccionado.getCapacidad() - camionSeleccionado.getCargaActual();

                if (capacidadDisponible > 0) {
                    int cantidadAsignada = Math.min(capacidadDisponible, cantidadRestante);  // Asignar la mayor cantidad posible

                    camionSeleccionado.setCargaActual(camionSeleccionado.getCargaActual() + cantidadAsignada);
                    cantidadRestante -= cantidadAsignada;

                    // Se pasa una parte de una venta a un paquete
                    // Para saber que parte de la venta tiene un camion (Venta parcial)
                    var paquete = new Paquete(venta,cantidadAsignada);
                    camionSeleccionado.agregarPaquete(paquete);  // Agregar la venta al camión
                }
            }
        }
        return mapaCamionesPorCentral;
    }

    private static Camion seleccionarCamion(Venta venta, Map<Oficina, List<Camion>> mapaCamionesPorCentral, Map<Oficina, Map<Venta, Double>> mapaTiempoPorVentaPorOficina) {
        Camion camionSeleccionado = null;
        double menorTiempo = Double.MAX_VALUE;
        for (var almacen : mapaCamionesPorCentral.keySet()){
            for (var camion : mapaCamionesPorCentral.get(almacen)){
                var tiempo = mapaTiempoPorVentaPorOficina.get(almacen).get(venta);
                if (tiempo < menorTiempo && camion.getCapacidad() - camion.getCargaActual() > 0){
                    menorTiempo = tiempo;
                    camionSeleccionado = camion;
                }
            }
        }
        return camionSeleccionado;
    }
}
