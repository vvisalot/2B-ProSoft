package app;

import algorithm.GrafoOficinas;
import model.Oficina;
import model.Tramo;
import utils.LeerDatos;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
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

        GrafoOficinas grafoOficinas = new GrafoOficinas();
        String filePathTramos = "resources/tramos.txt";  // Cambia esta ruta por la correcta
        List<Tramo> tramos = LeerDatos.leerTramosDesdeArchivo(filePathTramos, mapaOficinas);

//        System.out.println("\nTramos leídos:");
//        for (Tramo tramo : tramos) {
//            System.out.println(tramo);
//        }
        // Agregar los tramos al grafo
        for (Tramo tramo : tramos) {
            // Agregar la arista al grafo, suponiendo que quieres que sea bidireccional
            grafoOficinas.agregarArista(tramo, true);
        }

        for (Oficina oficina : mapaOficinas.values()) {
            grafoOficinas.imprimirVecinosPorCodigo(oficina);
        }
    }
}