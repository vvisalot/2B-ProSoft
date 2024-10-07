package app;

import algorithm.AsignadorVentas;
import algorithm.GrafoTramos;
import algorithm.PlanificadorRutas;
import algorithm.SimulatedAnnealing;
import model.*;
import utils.LeerDatos;
import utils.Pair;
import utils.RelojSimulado;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
    public static final int DIAS_MANTENIMIENTO = 3;
    public static void main(String[] args) throws IOException {
        //Lectura de datos
        String archivoOficinas = "resources/oficinas.txt";
        Map<String, Oficina> mapaOficinas = LeerDatos.leerOficinasDesdeArchivo(archivoOficinas);

        // Filtrar almacenes principales
        List<Oficina> almacenesPrincipales = mapaOficinas.values().stream()
                .filter(oficina -> oficina.getCodigo().equals("150101")  // Lima
                        || oficina.getCodigo().equals("130101")  // Trujillo
                        || oficina.getCodigo().equals("040101")) // Arequipa
                .toList();

        // Imprimir almacenes principales
//        System.out.println("Almacenes principales:");
//        for (Oficina almacen : almacenesPrincipales) {
//            System.out.println("Ubigeo: " + almacen.getCodigo() + " -> " + almacen.getDepartamento());
//        }

        // Imprimir las oficinas leídas
//        System.out.println("Oficinas leídas:");
//        for (Map.Entry<String, Oficina> entry : mapaOficinas.entrySet()) {
//            String ubigeo = entry.getKey();
//            Oficina oficina = entry.getValue();
//            System.out.println("Ubigeo: " + ubigeo + " -> " + oficina);
//        }
        //Leer bloqueos
        var mapaBloqueos = new HashMap<Tramo,List<Bloqueo>>();
        for (int i = 1; i <= 12; i++) {
            String filePathBloqueos = String.format("resources/bloqueos/bloqueo%02d.txt", i);
            LeerDatos.leerBloqueos(filePathBloqueos,mapaBloqueos);
        }

        GrafoTramos grafoTramos = GrafoTramos.getInstance();
        String filePathTramos = "resources/tramos.txt";  // Cambia esta ruta por la correcta
        var datosTramos = LeerDatos.leerTramosDesdeArchivo(filePathTramos, mapaOficinas,mapaBloqueos);
        var listaTramos = datosTramos.first();
        var mapaTramos = datosTramos.second();

//        System.out.println("\nTramos leídos:");
//        for (Tramo tramo : listaTramos) {
//            System.out.println(tramo.getDistancia());
//        }

        // Agregar los tramos al grafo
        for (Tramo tramo : listaTramos) {
            grafoTramos.agregarArista(tramo, mapaTramos.get(tramo.getDestino().getCodigo()));
        }
        //grafoTramos.imprimirTodosLosTramos();

//        Oficina origen = mapaOficinas.get("010201");
//        Oficina destino = mapaOficinas.get("010601");
//
//        List<Tramo> rutaMasCorta = grafoTramos.obtenerRutaMasCorta(origen, destino);
//
//        if (rutaMasCorta != null) {
//            System.out.println("Ruta más corta encontrada:");
//            for (Tramo tramo : rutaMasCorta) {
//                System.out.println(tramo);
//            }
//        } else {
//            System.out.println("No se encontró ruta.");
//        }

        //grafoTramos.imprimirGrafo();

//        for (int i = 1; i <= 12; i++) {
//            String filePathBloqueos = String.format("resources/bloqueos/bloqueo%02d.txt", i);
//            LeerDatos.leerBloqueos(filePathBloqueos, grafoTramos);
//        }
//        grafoTramos.imprimirBloqueos();

        //Lectura de ventas
        String archivoVentas = "resources/ventas.historico.proyectado/ventas200001.txt";
        List<Venta> ventas = LeerDatos.leerVentasDesdeArchivo(archivoVentas, mapaOficinas);

        String archivoMantenimientos = "resources/mantenimientos.txt";
        var mapaMantenimientos = new HashMap<Camion,List<LocalDateTime>>();
        LeerDatos.leerMantenimientos(archivoMantenimientos, mapaMantenimientos);
        //Inicialización de camiones
        List<Camion> camiones = Camion.inicializarCamiones(almacenesPrincipales.get(2), almacenesPrincipales.get(0), almacenesPrincipales.get(1),mapaMantenimientos);

        var reloj = RelojSimulado.getInstance();
        for(int i = 0; i < 4; i++){
            // Empezamos a tomar el tiempo desde la asignacion de ventas a camiones
            long tiempoInicio = System.currentTimeMillis();
            //Loop en todos los camiones para revisar si alguno le toca mantenimiento o si tiene que salir de mantenmiento
            for(Camion c :camiones){
                //Revisar si esta en mantenimiento, si ya paso los días de mantenimiento vuelve a estar disponible
                if(c.getEnMantenimiento() && reloj.getTiempo().equals(c.getFechaUltimoMantenimiento().plusDays(DIAS_MANTENIMIENTO))){
                    c.setEnMantenimiento(false);
                }
                //Revisar si el camión se tiene que quedar por mantenimiento
                for(LocalDateTime fechaMantenimieto : c.getMantenimientosProgrmados()){
                    if(fechaMantenimieto.equals(reloj.getTiempo())){
                        c.setFechaUltimoMantenimiento(reloj.getTiempo());
                        c.setEnMantenimiento(true);
                    }
                }
            }
            //Actualizar Bloques en los tramos
//            grafoTramos.actualizarBloqueos();
//            var tramo = grafoTramos.buscarTramoConOrigenDestino(new Oficina("130901"),new Oficina("100101"));
//            var vecinos = grafoTramos.obtenerVecinos(tramo);
            // Asignamos las ventas a los camiones
            var mapaCamionesPorCentral = AsignadorVentas.asignarVentasGreedy(camiones, ventas,almacenesPrincipales, grafoTramos); //ALEATORIO CON CONDICIONES
//            for(var entry : mapaCamionesPorCentral.entrySet()){
//                System.out.println("Central: " + entry.getKey());
//                for(var camion: entry.getValue()){
//                    System.out.println("Camion: " + camion.getCodigo());
//                    camion.imprimirPaquetes();
//                    System.out.println("\n");
//                }
//            }

            // Planificamos una ruta para cada camión
            for(var entry : mapaCamionesPorCentral.entrySet()){
                for(var camion: entry.getValue()){
                    if(camion.getPaquetes().isEmpty()){
                        continue;
                    }
                    SimulatedAnnealing.calcular(camion.getPaquetes(),camion, reloj, almacenesPrincipales);
                }
            }
            //Actualizar posicion de los camiones para siguiente ejecucion
//            for(Camion c :camiones){
//
//            }


            // Obtenemos el tiempo de ejecucion del programa
            long tiempoFin = System.currentTimeMillis();
            System.out.println("Tiempo de ejecución: " + (tiempoFin - tiempoInicio) + " ms");
            for(var entry : mapaCamionesPorCentral.entrySet()){
                for(var camion: entry.getValue()){
                    if(camion.getPaquetes().isEmpty()){
                        continue;
                    }
                    System.out.println("Camion: " + camion);
                }
            }
            reloj.pasarCicloDeEntregas();
        }


    }
}