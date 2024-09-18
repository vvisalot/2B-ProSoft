package app;

import model.Bloqueo;
import model.Venta;
import utils.LectorArchivos;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
          // Leer todas las ventas en la carpeta "ventas.historico.proyectado"
            String carpetaVentas = "resources/ventas.historico.proyectado";
            List<Venta> ventas = LectorArchivos.leerVentasEnCarpeta(carpetaVentas);

            // Leer todos los bloqueos en la carpeta "bloqueos"
            String carpetaBloqueos = "resources/bloqueos";
            List<Bloqueo> bloqueos = LectorArchivos.leerBloqueosEnCarpeta(carpetaBloqueos);

            // Imprimir las ventas y bloqueos cargados
//            System.out.println("Ventas cargadas: " + ventas.size());
//            for (Venta venta : ventas) {
//                System.out.println(venta);
//            }

            System.out.println("Bloqueos cargados: " + bloqueos.size());
            for (Bloqueo bloqueo : bloqueos) {
                System.out.println(bloqueo);
            }

        } catch (IOException e) {
            System.out.println("Error al leer los archivos: " + e.getMessage());
        }
    }
}