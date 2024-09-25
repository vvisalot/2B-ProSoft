package app;

import algorithm.GrafoTramos;
import model.Oficina;
import model.Tramo;
import utils.LeerDatos;

import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        //Lectura de datos
        String archivoOficinas = "resources/oficinas.txt";
        Map<String, Oficina> mapaOficinas = LeerDatos.leerOficinasDesdeArchivo(archivoOficinas);

//        // Imprimir las oficinas leídas
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

        System.out.println("\nTramos leídos:");
        for (Tramo tramo : listaTramos) {
            System.out.println(tramo.getDistancia());
        }
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

    }
}