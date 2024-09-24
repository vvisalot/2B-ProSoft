package app;

import model.Bloqueo;
import model.Oficina;
import model.Tramo;
import model.Venta;
import utils.LeerDatos;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        //Lectura de datos
        String archivoOficinas = "resources/oficinas.txt";
        List<Oficina> oficinas = LeerDatos.leerOficinasDesdeArchivo(archivoOficinas);

        // Imprimir las oficinas cargadas
        for (Oficina oficina : oficinas) {
            System.out.println(oficina);
        }
    }
}