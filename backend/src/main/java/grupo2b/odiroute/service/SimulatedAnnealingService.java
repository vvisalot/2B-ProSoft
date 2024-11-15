package grupo2b.odiroute.service;

import grupo2b.odiroute.algorithm.AsignadorVentas;
import grupo2b.odiroute.algorithm.GrafoTramos;
import grupo2b.odiroute.algorithm.SimulatedAnnealing;
import grupo2b.odiroute.dto.Solucion;
import grupo2b.odiroute.model.Bloqueo;
import grupo2b.odiroute.model.Camion;
import grupo2b.odiroute.model.Oficina;
import grupo2b.odiroute.model.Tramo;
import grupo2b.odiroute.model.Venta;
import grupo2b.odiroute.utils.LeerDatos;
import grupo2b.odiroute.utils.RelojSimulado;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimulatedAnnealingService {
    public static final int DIAS_MANTENIMIENTO = 3;
    private List<Solucion> soluciones;

    public SimulatedAnnealingService() {
        soluciones = new ArrayList<>();
    }

    // Lectura de datos
    public Map<String, Oficina> cargarOficinas() throws IOException {
        String archivoOficinas = "src/main/resources/data/oficinas.txt";
        return LeerDatos.leerOficinasDesdeArchivo(archivoOficinas);
    }

    public List<Oficina> obtenerAlmacenesPrincipales(Map<String, Oficina> mapaOficinas) {
        return mapaOficinas.values().stream()
                .filter(oficina -> oficina.getCodigo().equals("150101")  // Lima
                        || oficina.getCodigo().equals("130101")  // Trujillo
                        || oficina.getCodigo().equals("040101")) // Arequipa
                .toList();
    }

    public Map<Tramo, List<Bloqueo>> cargarBloqueos() throws IOException {
        var mapaBloqueos = new HashMap<Tramo, List<Bloqueo>>();
        for (int i = 1; i <= 12; i++) {
            String filePathBloqueos = String.format("src/main/resources/data/bloqueos/bloqueo%02d.txt", i);
            LeerDatos.leerBloqueos(filePathBloqueos, mapaBloqueos);
        }
        return mapaBloqueos;
    }

    public GrafoTramos cargarTramos(Map<String, Oficina> mapaOficinas, Map<Tramo, List<Bloqueo>> mapaBloqueos) throws IOException {
        GrafoTramos grafoTramos = GrafoTramos.getInstance();
        String filePathTramos = "src/main/resources/data/tramos.txt";
        var datosTramos = LeerDatos.leerTramosDesdeArchivo(filePathTramos, mapaOficinas, mapaBloqueos);
        List<Tramo> listaTramos = datosTramos.first();
        var mapaTramos = datosTramos.second();

        for (Tramo tramo : listaTramos) {
            grafoTramos.agregarArista(tramo, mapaTramos.get(tramo.getDestino().getCodigo()));
        }

        return grafoTramos;
    }

    public List<Venta> cargarVentas(MultipartFile archivoVentas, Map<String, Oficina> mapaOficinas) throws IOException {
        return LeerDatos.leerVentasDesdeArchivo(archivoVentas, mapaOficinas);
    }

    public Map<Camion, List<LocalDateTime>> cargarMantenimientos() throws IOException {
        String archivoMantenimientos = "src/main/resources/data/mantenimientos.txt";
        var mapaMantenimientos = new HashMap<Camion, List<LocalDateTime>>();
        LeerDatos.leerMantenimientos(archivoMantenimientos, mapaMantenimientos);
        return mapaMantenimientos;
    }

    public List<Camion> inicializarCamiones(List<Oficina> almacenesPrincipales, Map<Camion, List<LocalDateTime>> mapaMantenimientos) {
        return Camion.inicializarCamiones(almacenesPrincipales.get(2), almacenesPrincipales.get(0), almacenesPrincipales.get(1), mapaMantenimientos);
    }

    public void runSimulatedAnnealing(MultipartFile archivoVentas) throws IOException {

        Map<String, Oficina> mapaOficinas = cargarOficinas();
        List<Oficina> almacenesPrincipales = obtenerAlmacenesPrincipales(mapaOficinas);
        Map<Tramo, List<Bloqueo>> mapaBloqueos = cargarBloqueos();
        GrafoTramos grafoTramos = cargarTramos(mapaOficinas, mapaBloqueos);
        List<Venta> ventas = cargarVentas(archivoVentas, mapaOficinas);
        Map<Camion, List<LocalDateTime>> mapaMantenimientos = cargarMantenimientos();
        List<Camion> camiones = inicializarCamiones(almacenesPrincipales, mapaMantenimientos);


        var reloj = RelojSimulado.getInstance();
        // Empezamos a tomar el tiempo desde la asignación de ventas a camiones
        long tiempoInicio = System.currentTimeMillis();
        soluciones.clear();

        for (int i = 0; i < 4; i++) { //TODO: CAMBIAR EL ARCHIVO VENTAS DE PRUEBA PARA QUE SOPORTE UNA SEMANA
            // Mantenimiento de camiones
            for (Camion c : camiones) {
                //Revisar si esta en mantenimiento, si ya paso los días de mantenimiento vuelve a estar disponible
                if (c.getEnMantenimiento() && reloj.getTiempo().equals(c.getFechaUltimoMantenimiento().plusDays(DIAS_MANTENIMIENTO))) {
                    c.setEnMantenimiento(false);
                }
                //Revisar si el camión se tiene que quedar por mantenimiento
                for (LocalDateTime fechaMantenimieto : c.getMantenimientosProgramados()) {
                    if (fechaMantenimieto.equals(reloj.getTiempo())) {
                        c.setFechaUltimoMantenimiento(reloj.getTiempo());
                        c.setEnMantenimiento(true);
                    }
                }
            }

            // Asignamos las ventas a los camiones
            var mapaCamionesPorCentral = AsignadorVentas.asignarVentasGreedy(camiones, ventas, almacenesPrincipales, grafoTramos);

            // Planificamos una ruta para cada camión
            var tiempoTotal = 0.0; //innecesario
            var camionesConPaquetes = 0; //innecesario

            //Este loop corre cada vez que se tiene que procesar una asignación de ventas y rutas
            for (var entry : mapaCamionesPorCentral.entrySet()) {
                for (var camion : entry.getValue()) {
                    if (camion.getPaquetes().isEmpty()) {
                        continue;
                    }
                    var solucionCamion = SimulatedAnnealing.calcular(camion.getPaquetes(), camion, reloj, almacenesPrincipales);
                    soluciones.add(solucionCamion);
                    tiempoTotal += solucionCamion.tiempoTotal(); //innecesario
                    camionesConPaquetes++; //innecesario
                }
            }

            //Actualizar posicion de los camiones para siguiente ejecucion
            for(Camion c :camiones) {
                if(c.getEnRuta()){
                    if (c.getRegresoAlmacen().isBefore(reloj.getTiempoSiguienteBatch())){
                        c.setEnRuta(false);
                        c.setCargaActual(0);
                        c.setFechaUltimoMantenimiento(reloj.getTiempoSiguienteBatch()); //editar si es necesario
                    }
                }
            }

            var tiempoPromedioRuta = tiempoTotal / camionesConPaquetes;

            System.out.printf("Hora de salida de los camiones: %02d:%02d - %02d:%02d%n",
                    reloj.getTiempo().getHour(), reloj.getTiempo().getMinute(),
                    reloj.getTiempoSiguienteBatch().getHour(), reloj.getTiempoSiguienteBatch().getMinute());
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
            System.out.printf("\nTiempo promedio de ruta: %.2f\n\n%n", tiempoPromedioRuta);
            reloj.pasarCicloDeEntregas();
        }
        // Obtenemos el tiempo de ejecución del programa
        long tiempoFin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución: " + (tiempoFin - tiempoInicio) + " ms");
    }

    public List<Solucion> getSoluciones() {
        return soluciones;
    }
}