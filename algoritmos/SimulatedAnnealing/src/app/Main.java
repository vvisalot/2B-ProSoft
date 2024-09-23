package app;

import model.Bloqueo;
import model.Tramo;
import model.Venta;
import utils.LeerDatos;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Leer todas las ventas en la carpeta "ventas.historico.proyectado"
        String carpetaVentas = "resources/ventas.historico.proyectado";

        // Leer todos los bloqueos en la carpeta "bloqueos"
        String carpetaBloqueos = "resources/bloqueos";
        Map<String, Tramo> tramosExistentes = new HashMap<>();

        try {
            List<Venta> ventas = LeerDatos.leerVentasEnCarpeta(carpetaVentas);
            List<Bloqueo> bloqueos = LeerDatos.leerBloqueosEnCarpeta(carpetaBloqueos, tramosExistentes);
            System.out.println("Se leyeron todos los bloqueos");
            //Imprimir bloqueos
            for (Bloqueo bloqueo : bloqueos) {
                System.out.println("Tramo: " + bloqueo.getTramo().getUbigeoOrigen() + "=>" + bloqueo.getTramo().getUbigeoDestino());
                System.out.println("Inicio y fin del bloqueo" + bloqueo.getFechaHoraInicio() + "=>" + bloqueo.getFechaHoraFin());
                System.out.println("======");
            }
            //Imprimir las ventas
//            System.out.println("Ventas cargadas: " + ventas.size());
//            for (Venta venta : ventas) {
//                System.out.println(venta);
//            }
        } catch (IOException e) {
            System.out.println("Error al leer los archivos: " + e.getMessage());
        }
    }
}