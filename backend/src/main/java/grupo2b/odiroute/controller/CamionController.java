package grupo2b.odiroute.controller;

import grupo2b.odiroute.model.Camion;
import grupo2b.odiroute.service.CamionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/camiones")
public class CamionController {

    private final CamionService camionService;

    @Autowired
    public CamionController(CamionService camionService) {
        this.camionService = camionService;
    }

    @GetMapping
    public ResponseEntity<List<Camion>> getAllCamiones() {
        List<Camion> camiones = camionService.findAll();
        return new ResponseEntity<>(camiones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Camion> getCamionById(@PathVariable Long id) {
        Optional<Camion> camion = camionService.findById(id);
        return camion.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Camion> createCamion(@RequestBody Camion camion) {
        Camion savedCamion = camionService.save(camion);
        return new ResponseEntity<>(savedCamion, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Camion> updateCamion(@PathVariable Long id, @RequestBody Camion camion) {
        try {
            Camion updatedCamion = camionService.update(id, camion);
            return new ResponseEntity<>(updatedCamion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCamion(@PathVariable Long id) {
        camionService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
