package main;

import Clases.Bloqueo;
import Clases.Camion;
import Clases.Mantenimiento;
import Clases.Oficina;
import Clases.Tramo;
import Clases.Velocidad;
import Clases.Venta;
import Utils.LeerDatos;
import java.io.IOException;
import java.util.List;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        try {
            // Leer todas las ventas en la carpeta "ventas.historico.proyectado"
            String carpetaVentas = "archivos/ventas.historico.proyectado";
            List<Venta> ventas = LeerDatos.leerVentasEnCarpeta(carpetaVentas);
            // int i=1;
            // System.out.println("VENTAS");
            // for(Venta venta: ventas){
            //     System.out.println(i+") Ubigeo Origen:"+venta.getUbigeoOrigen()+"  Ubigeo Destino: "+venta.getUbigeoDestino()
            //              +"   Cantidad: "+venta.getCantidad()+"   IdCliente: "+venta.getIdCliente()
            //              +"   Fecha y Hora: "+venta.getFechaHora());
            //     i++;
            //     if(i==20) break;
            // }

            // Leer todas los mantenimientos en la carpeta "mantenimiento"
            String carpetaMantenimientos = "archivos/Mantenimiento";
            List<Mantenimiento> mantenimientos  = LeerDatos.leerMantenimientos(carpetaMantenimientos);

            int i=0;
            for(Venta vv: ventas){
                //Camion camion = new Camion("A001",90, "040101");
                System.out.println("fecha de venta: "+vv.getFechaHora());
                System.out.println("Ubigeo origen: "+vv.getUbigeoOrigen());
                //1. Leer camiones con ubigeo
                List<Camion> camiones = LeerDatos.leerCamionesConUbigeo("archivos/camiones.txt",vv.getUbigeoOrigen());
                //2. Ver que camion no este en mantenimiento


                for(Camion cam: camiones){
                    System.out.println("Camion: "+cam.getIdCamion());

                    

                    AlgoritmoGenetico algGen = new AlgoritmoGenetico(5,0.5,vv,cam);
                    Double mejorTiempo = algGen.generarAlgoritmoGenético(5, 0.5, vv, cam);
                
                }
                
                i++;
                if(i==1) break;


                //System.out.println(vv.getUbigeoOrigen()+" "+vv.getUbigeoDestino()+" "+vv.getCantidad()+" "+vv.getIdCliente()+" "+vv.getFechaHora());
                System.out.println("El mejor tiempo para esta venta es: "+mejorTiempo);
            }








            // Leer todos los bloqueos en la carpeta "bloqueos"
            String carpetaBloqueos = "archivos/bloqueos";
            List<Bloqueo> bloqueos = LeerDatos.leerBloqueosEnCarpeta(carpetaBloqueos);
            
            //Leer oficinas
            List<Oficina> oficinas = LeerDatos.leerOficinas("archivos/oficinas.txt");
            //int i=1;
            // for(Oficina of: oficinas){
            //     System.out.println(i+") Ubigeo:"+of.getUbigeo()+"  Dep: "+of.getDepartamento()+"   Prov: "+of.getProvincia()+"   Capac:"+of.getAlmacen());
            //     i++;
            // }
            

            /*
            //Leer Tramos
            List<Tramo> tramos = LeerDatos.leerTramos("archivos/tramos.txt");
            //almacenar tramos en mapa hash --> clave es ubigeo origen
            HashMap<String, Tramo> mapaTramos = new HashMap<>();
            for (Tramo tram : tramos) {
                mapaTramos.put(tram.getUbigeoOrigen(), tram);
            }
            int i=1;
            System.out.println("TRAMOS");
            for(Tramo tram : mapaTramos.values()){
                System.out.println(i+") Ubigeo Origen:"+tram.getUbigeoOrigen()+"  Ubigeo Destino: "+tram.getUbigeoDestino()
                        +"   Distancia: "+tram.getDistanciaTramo()+"   Velocidad:"+tram.getVelocidadTramo()
                        +"   Hora: "+tram.getHorasTramo());
                i++;
                if(i==20) break;
            }
            
            //Leer Velocidades
            List<Velocidad> velocidades = LeerDatos.leerVelocidades("archivos/velocidades.txt");
            
            //Leer Mantenimientos
            List<Mantenimiento> mantenimientos = LeerDatos.leerMantenimientos("archivos/mantenimiento_trim.txt");
            */            

            
        } catch (IOException e) {
            System.out.println("Error al leer los archivos: " + e.getMessage());
        }
    }
}