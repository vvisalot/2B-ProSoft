package main;

import Clases.Bloqueo;
import Clases.Mantenimiento;
import Clases.Oficina;
import Clases.Tramo;
import Clases.Velocidad;
import Clases.Venta;
import Utils.LeerDatos;
import java.io.IOException;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try {
            // Leer todas las ventas en la carpeta "ventas.historico.proyectado"
            String carpetaVentas = "archivos/ventas.historico.proyectado";
            List<Venta> ventas = LeerDatos.leerVentasEnCarpeta(carpetaVentas);

            // Leer todos los bloqueos en la carpeta "bloqueos"
            String carpetaBloqueos = "archivos/bloqueos";
            List<Bloqueo> bloqueos = LeerDatos.leerBloqueosEnCarpeta(carpetaBloqueos);
            
            //Leer oficinas
            List<Oficina> oficinas = LeerDatos.leerOficinas("archivos/oficinas.txt");
            
            //Leer Tramos
            List<Tramo> tramos = LeerDatos.leerTramos("archivos/tramos.txt");
            
            //Leer Velocidades
            List<Velocidad> velocidades = LeerDatos.leerVelocidades("archivos/velocidades.txt");
            
            //Leer Mantenimientos
            List<Mantenimiento> mantenimientos = LeerDatos.leerMantenimientos("archivos/mantenimiento_trim.txt");
            

            
        } catch (IOException e) {
            System.out.println("Error al leer los archivos: " + e.getMessage());
        }
    }
}


/*
           
            Imprimir datos
            
            System.out.println("Ventas cargadas: " + ventas.size());
            for (Venta venta : ventas) {
                System.out.println(venta);
            }

            System.out.println("Bloqueos cargados: " + bloqueos.size());
            for (Bloqueo bloqueo : bloqueos) {
                System.out.println(bloqueo);
            }
            
            
            System.out.println("Oficinas cargados: " + oficinas.size());
            for (Oficina oficina : oficinas) {
                System.out.println(oficina);
            }
            
            System.out.println("Tramos cargados: " + tramos.size());
            for (Tramo tramo : tramos) {
                System.out.println(tramo);
            }
            */
            /*
            System.out.println("Velocidades cargadas: " + velocidades.size());
            for (Velocidad velocidad : velocidades) {
                System.out.println(velocidad);
            }
            
            System.out.println("Mantenimientos cargados: " + mantenimientos.size());
            for (Mantenimiento mantenimiento : mantenimientos) {
                System.out.println(mantenimiento);
            }
 */