package algorithm;

import model.Camion;
import model.Venta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsignadorVentas {

    public static void asignarVentas(List<Camion> camiones, List<Venta> ventas) {
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

                    camionSeleccionado.agregarVenta(venta);  // Agregar la venta al camión
                    System.out.println("Asignado " + cantidadAsignada + " unidades de la venta al camión " + camionSeleccionado.getCodigo() +
                            " (Carga actual: " + camionSeleccionado.getCargaActual() + "/" + camionSeleccionado.getCapacidad() + ")");

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
}
