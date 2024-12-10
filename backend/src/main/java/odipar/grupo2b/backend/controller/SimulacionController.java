package odipar.grupo2b.backend.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import odipar.grupo2b.backend.algorithm.GrafoTramos;
import odipar.grupo2b.backend.dto.Solucion;
import odipar.grupo2b.backend.model.Bloqueo;
import odipar.grupo2b.backend.model.Camion;
import odipar.grupo2b.backend.model.Oficina;
import odipar.grupo2b.backend.model.Tramo;
import odipar.grupo2b.backend.model.Venta;
import odipar.grupo2b.backend.service.AlgoritmoService;
import odipar.grupo2b.backend.service.SimulacionDataService;
import odipar.grupo2b.backend.utils.LeerDatos;
import odipar.grupo2b.backend.utils.RelojSimulado;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/simulacion")
public class SimulacionController {
    private final SimulacionDataService simulacionDataService;
	private final AlgoritmoService algoritmoService;

	public SimulacionController(SimulacionDataService simulacionDataService, AlgoritmoService algoritmoService) {
		this.simulacionDataService = simulacionDataService;
        this.algoritmoService = algoritmoService;
	}

    @GetMapping
    public ResponseEntity<List<Solucion>> simular(){
        var camiones = simulacionDataService.getCamiones();
        var reloj = simulacionDataService.getReloj();
        var ventas = simulacionDataService.getVentas();
        var almacenesPrincipales = simulacionDataService.getAlmacenesPrincipales();
        var grafoTramos = simulacionDataService.getGrafoTramos();
		return new ResponseEntity<>(algoritmoService.simular(camiones, reloj, ventas, almacenesPrincipales, grafoTramos),HttpStatus.OK);
	} 

    @GetMapping("/reloj")
    public ResponseEntity<String> actualizarReloj(@RequestParam("fechaInicial") 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime){
                var reloj = simulacionDataService.getReloj();
                reloj.actualizarReloj(dateTime);
		return new ResponseEntity<>(reloj.getTiempo().toString(),HttpStatus.OK);
	}

    @GetMapping("/reset")
    public String resetSimulacionDataService() {
        String archivoOficinas = "oficinas.txt";
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
            String filePathBloqueos = String.format("bloqueos/bloqueo%02d.txt", i);
            LeerDatos.leerBloqueos(filePathBloqueos, mapaBloqueos);
        }

        GrafoTramos grafoTramos = GrafoTramos.getInstance();
        String filePathTramos = "tramos.txt";  // Cambia esta ruta por la correcta
        var datosTramos = LeerDatos.leerTramosDesdeArchivo(filePathTramos, mapaOficinas, mapaBloqueos);
        var listaTramos = datosTramos.first();
        var mapaTramos = datosTramos.second();

        // Agregar los tramos al grafo
        for (Tramo tramo : listaTramos) {
            grafoTramos.agregarArista(tramo, mapaTramos.get(tramo.getDestino().getCodigo()));
        }

        //Lectura de ventas
        var archivosVenta = new String[]{
            "ventas.historico.proyectado/ventas202403.txt",
            "ventas.historico.proyectado/ventas202404.txt",
            "ventas.historico.proyectado/ventas202405.txt",
            "ventas.historico.proyectado/ventas202406.txt",
            "ventas.historico.proyectado/ventas202407.txt",
            "ventas.historico.proyectado/ventas202408.txt",
            "ventas.historico.proyectado/ventas202409.txt",
            "ventas.historico.proyectado/ventas202410.txt",
            "ventas.historico.proyectado/ventas202411.txt",
            "ventas.historico.proyectado/ventas202412.txt",
            "ventas.historico.proyectado/ventas202501.txt",
            "ventas.historico.proyectado/ventas202502.txt",
            "ventas.historico.proyectado/ventas202503.txt",
            "ventas.historico.proyectado/ventas202504.txt",
            "ventas.historico.proyectado/ventas202505.txt"
        };
        var ventas = new ArrayList<Venta>();
        for(String archivoVentas : archivosVenta){
            List<Venta> ventasAux = LeerDatos.leerVentasDesdeArchivo(archivoVentas, mapaOficinas);
            ventas.addAll(ventasAux);
        }

        String archivoMantenimientos = "mantenimientos.txt";
        var mapaMantenimientos = new HashMap<Camion, List<LocalDateTime>>();
        LeerDatos.leerMantenimientos(archivoMantenimientos, mapaMantenimientos);
        //Inicialización de camiones
        List<Camion> camiones = Camion.inicializarCamiones(almacenesPrincipales.get(2), almacenesPrincipales.get(0), almacenesPrincipales.get(1), mapaMantenimientos);

        var reloj = RelojSimulado.getInstance();
        simulacionDataService.reset(camiones, reloj, ventas, almacenesPrincipales, grafoTramos);
        return "SimulacionDataService has been reset!";
    }
}
