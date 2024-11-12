package grupo2b.odiroute.controller;

import grupo2b.odiroute.dto.Solucion;
import grupo2b.odiroute.service.SimulatedAnnealingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/simulated-annealing")
public class SimulatedAnnealingController {

    private final SimulatedAnnealingService simulatedAnnealingService;

    public SimulatedAnnealingController(SimulatedAnnealingService simulatedAnnealingService) {
        this.simulatedAnnealingService = simulatedAnnealingService;
    }

    @PostMapping("/soluciones")
    public ResponseEntity<?> getSoluciones(@RequestParam("archivo") MultipartFile archivo) {
        try {
            // Ejecuta `runSimulatedAnnealing` con el archivo de ventas
            simulatedAnnealingService.runSimulatedAnnealing(archivo);
            // Obtiene las soluciones generadas por el algoritmo
            List<Solucion> soluciones = simulatedAnnealingService.getSoluciones();

            // Si las soluciones están vacías, retornamos una respuesta con NO CONTENT
            if (soluciones.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se generaron soluciones.");
            }

            // Si hay soluciones, las devolvemos en la respuesta
            return ResponseEntity.ok(soluciones);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el archivo de ventas y ejecutar el algoritmo.");
        }
    }

}
