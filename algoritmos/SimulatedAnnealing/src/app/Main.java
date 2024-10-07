package app;

import algorithm.AsignadorVentas;
import algorithm.GrafoTramos;
import algorithm.SimulatedAnnealing;
import model.*;
import utils.LeerDatos;
import utils.RelojSimulado;

import java.io.IOException;
import java.time.LocalDateTime;
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

        //Leer bloqueos
        var mapaBloqueos = new HashMap<Tramo, List<Bloqueo>>();
        for (int i = 1; i <= 12; i++) {
            String filePathBloqueos = String.format("resources/bloqueos/bloqueo%02d.txt", i);
            LeerDatos.leerBloqueos(filePathBloqueos, mapaBloqueos);
        }

        GrafoTramos grafoTramos = GrafoTramos.getInstance();
        String filePathTramos = "resources/tramos.txt";  // Cambia esta ruta por la correcta
        var datosTramos = LeerDatos.leerTramosDesdeArchivo(filePathTramos, mapaOficinas, mapaBloqueos);
        var listaTramos = datosTramos.first();
        var mapaTramos = datosTramos.second();

        // Agregar los tramos al grafo
        for (Tramo tramo : listaTramos) {
            grafoTramos.agregarArista(tramo, mapaTramos.get(tramo.getDestino().getCodigo()));
        }

        //Lectura de ventas
        String archivoVentas = "resources/ventas.historico.proyectado/ventas200001.txt";
        List<Venta> ventas = LeerDatos.leerVentasDesdeArchivo(archivoVentas, mapaOficinas);

        String archivoMantenimientos = "resources/mantenimientos.txt";
        var mapaMantenimientos = new HashMap<Camion, List<LocalDateTime>>();
        LeerDatos.leerMantenimientos(archivoMantenimientos, mapaMantenimientos);
        //Inicialización de camiones
        List<Camion> camiones = Camion.inicializarCamiones(almacenesPrincipales.get(2), almacenesPrincipales.get(0), almacenesPrincipales.get(1), mapaMantenimientos);

        var reloj = RelojSimulado.getInstance();
        // Empezamos a tomar el tiempo desde la asignacion de ventas a camiones
        long tiempoInicio = System.currentTimeMillis();

        // Loop de un dia
        for (int i = 0; i < 4; i++) {

            // Mantenimiento de camiones
            for (Camion c : camiones) {
                //Revisar si esta en mantenimiento, si ya paso los días de mantenimiento vuelve a estar disponible
                if (c.getEnMantenimiento() && reloj.getTiempo().equals(c.getFechaUltimoMantenimiento().plusDays(DIAS_MANTENIMIENTO))) {
                    c.setEnMantenimiento(false);
                }
                //Revisar si el camión se tiene que quedar por mantenimiento
                for (LocalDateTime fechaMantenimieto : c.getMantenimientosProgrmados()) {
                    if (fechaMantenimieto.equals(reloj.getTiempo())) {
                        c.setFechaUltimoMantenimiento(reloj.getTiempo());
                        c.setEnMantenimiento(true);
                    }
                }
            }

            // Asignamos las ventas a los camiones
            var mapaCamionesPorCentral = AsignadorVentas.asignarVentasGreedy(camiones, ventas, almacenesPrincipales, grafoTramos);

            // Planificamos una ruta para cada camión
            var tiempoTotal = 0.0;
            var camionesConPaquetes = 0;
            for (var entry : mapaCamionesPorCentral.entrySet()) {
                for (var camion : entry.getValue()) {
                    if (camion.getPaquetes().isEmpty()) {
                        continue;
                    }
                    tiempoTotal += SimulatedAnnealing.calcular(camion.getPaquetes(), camion, reloj, almacenesPrincipales);
                    camionesConPaquetes++;
                }
            }

            //Actualizar posicion de los camiones para siguiente ejecucion
            // TODO: Revisar si la hora del siguiente batch es despues de regresoAlmacen del camion
//            for(Camion c :camiones){


            var tiempoPromedioRuta = tiempoTotal / camionesConPaquetes;

            System.out.println(String.format("Hora de salida de los camiones: %02d:%02d - %02d:%02d",
                    reloj.getTiempo().getHour(), reloj.getTiempo().getMinute(),
                    reloj.getTiempoSiguienteBatch().getHour(), reloj.getTiempoSiguienteBatch().getMinute()));
            for (var entry : mapaCamionesPorCentral.entrySet()) {
                System.out.println("\tDe la oficina central " + entry.getKey().getCodigo() + " ubicada en  "
                        + entry.getKey().getDepartamento() + " salen los siguientes camiones:");
                for (var camion : entry.getValue()) {
                    if (camion.getPaquetes().isEmpty()) {
                        continue;
                    }
                    System.out.println("\t\t" + camion);
                }
            }
            System.out.println(String.format("\nTiempo promedio de ruta: %.2f\n\n", tiempoPromedioRuta));
            reloj.pasarCicloDeEntregas();
        }
        // Obtenemos el tiempo de ejecucion del programa
            long tiempoFin = System.currentTimeMillis();
            System.out.println("Tiempo de ejecución: " + (tiempoFin - tiempoInicio) + " ms");
    }
}