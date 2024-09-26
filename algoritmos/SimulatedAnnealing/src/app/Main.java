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

        // Imprimir las oficinas leídas
        System.out.println("Oficinas leídas:");
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
        AsignadorVentas.asignarVentas(camiones, ventas);

//        //Imprimir carga actual de los camiones
//        for (Camion camion: camiones){
//            System.out.println("Carga actual del camión " + camion.getCodigo() + ": " + camion.getCargaActual());
//        }
// Seleccionar un camión y planificar su ruta
//        if (!camiones.isEmpty()) {
//            Camion camion = camiones.getFirst();  // Seleccionamos el primer camión para el ejemplo
//
//            // Obtener la oficina de origen y destino de las ventas del camión
//            if (!camion.getVentas().isEmpty()) {
//                Oficina origen = camion.getVentas().getFirst().getOrigen();
//                Oficina destino = camion.getVentas().getLast().getDestino();
//
//                System.out.println(origen.getCodigo());
//                // Encontrar la mejor ruta usando A*
//                List<Tramo> rutaOptima = PlanificadorRutas.encontrarRutaOptima(destino, grafoTramos);
//
//                // Imprimir la ruta recorrida
//                if (rutaOptima != null) {
//                    System.out.println("Ruta óptima encontrada para el camión " + camion.getCodigo() + ":");
//                    for (Tramo tramo : rutaOptima) {
//                        System.out.println(tramo.getOrigen().getCodigo() + " -> " + tramo.getDestino().getCodigo() + " (Distancia: " + tramo.getDistancia() + ")");
//                    }
//                } else {
//                    System.out.println("No se encontró una ruta para el camión " + camion.getCodigo());
//                }
//            } else {
//                System.out.println("No hay ventas asignadas al camión.");
//            }
//        } else {
//            System.out.println("No hay camiones disponibles.");
//        }
    }
}