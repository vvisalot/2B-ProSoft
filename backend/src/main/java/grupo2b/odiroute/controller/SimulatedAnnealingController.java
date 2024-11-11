package grupo2b.odiroute.controller;

import grupo2b.odiroute.dto.Solucion;
import grupo2b.odiroute.service.SimulatedAnnealingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/simulated-annealing")
public class SimulatedAnnealingController {

    private final SimulatedAnnealingService simulatedAnnealingService;

    public SimulatedAnnealingController(SimulatedAnnealingService simulatedAnnealingService) {
        this.simulatedAnnealingService = simulatedAnnealingService;
    }

    @GetMapping("/soluciones")
    public ResponseEntity<?> getSoluciones() {
        try {
            if (simulatedAnnealingService.getSoluciones().isEmpty()) {
                simulatedAnnealingService.runSimulatedAnnealing();
            }
            List<Solucion> soluciones = simulatedAnnealingService.getSoluciones();
            return ResponseEntity.ok(soluciones);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al ejecutar el algoritmo Simulated Annealing.");
        }
    }
}
