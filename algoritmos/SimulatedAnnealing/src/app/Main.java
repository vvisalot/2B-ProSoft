package app;

import algorithm.AsignadorVentas;
import algorithm.GrafoTramos;
import algorithm.PlanificadorRutas;
import model.Camion;
import model.Oficina;
import model.Tramo;
import model.Venta;
import utils.LeerDatos;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class Main {
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

        GrafoTramos grafoTramos = new GrafoTramos();
        String filePathTramos = "resources/tramos.txt";  // Cambia esta ruta por la correcta
        var datosTramos = LeerDatos.leerTramosDesdeArchivo(filePathTramos, mapaOficinas);
        var listaTramos = datosTramos.first();
        var mapaTramos = datosTramos.second();

//        System.out.println("\nTramos leídos:");
//        for (Tramo tramo : listaTramos) {
//            System.out.println(tramo.getDistancia());
//        }
        // Agregar los tramos al grafo
        for (Tramo tramo : listaTramos) {
            // Agregar la arista al grafo, suponiendo que quieres que sea bidireccional
            grafoTramos.agregarArista(tramo, mapaTramos.get(tramo.getDestino().getCodigo()));
        }
        grafoTramos.imprimirTodosLosTramos();

        Oficina origen = mapaOficinas.get("010501");
        Oficina destino = mapaOficinas.get("010301");

        List<Tramo> rutaMasCorta = grafoTramos.obtenerRutaMasCorta(origen, destino);

        if (rutaMasCorta != null) {
            System.out.println("Ruta más corta encontrada:");
            for (Tramo tramo : rutaMasCorta) {
                System.out.println(tramo);
            }
        } else {
            System.out.println("No se encontró ruta.");
        }

        //grafoTramos.imprimirGrafo();

//        for (int i = 1; i <= 12; i++) {
//            String filePathBloqueos = String.format("resources/bloqueos/bloqueo%02d.txt", i);
//            LeerDatos.leerBloqueos(filePathBloqueos, grafoTramos);
//        }
//        grafoTramos.imprimirBloqueos();

        //Lectura de ventas
        String archivoVentas = "resources/ventas.historico.proyectado/ventas202403.txt";
        List<Venta> ventas = LeerDatos.leerVentasDesdeArchivo(archivoVentas, mapaOficinas);

        //Inicialización de camiones
        List<Camion> camiones = Camion.inicializarCamiones();
        AsignadorVentas.asignarVentas(camiones, ventas); //ALEATORIO CON CONDICIONES

//        //Imprimir carga actual de los camiones
//        for (Camion camion: camiones){
//            System.out.println("Carga actual del camión " + camion.getCodigo() + ": " + camion.getCargaActual());
//        }

//        // Planificamos una ruta para cada camión
        for (Camion camion : camiones) {
            List<Tramo> rutaRecorrida = null;
            if (!camiones.isEmpty()) {
                rutaRecorrida = PlanificadorRutas.planificarRuta(camion, grafoTramos, almacenesPrincipales);
            }
            System.out.println("Ruta recorrida por el camión " + camion.getCodigo() + ":");
            if (rutaRecorrida != null) {
                for (Tramo tramo : rutaRecorrida) {
                    System.out.println(tramo.getOrigen().getCodigo() + " -> " + tramo.getDestino().getCodigo() + " (" + tramo.getDistancia() + " km)");
                }
            }
        }
    }
}