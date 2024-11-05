package grupo2b.odiroute.controller;

import grupo2b.odiroute.model.Venta;
import grupo2b.odiroute.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // Crear una nueva venta
    @PostMapping
    public ResponseEntity<Venta> createVenta(@RequestBody Venta venta) {
        Venta savedVenta = ventaService.crearVenta(venta);
        return ResponseEntity.ok(savedVenta);
    }

    // Obtener todas las ventas
    @GetMapping
    public ResponseEntity<List<Venta>> getAllVentas() {
        List<Venta> ventas = ventaService.obtenerTodasLasVentas();
        return ResponseEntity.ok(ventas);
    }

    // Obtener una venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentaById(@PathVariable Long id) {
        Optional<Venta> venta = ventaService.obtenerVentaPorId(id);
        return venta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar una venta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        return ResponseEntity.noContent().build();
    }
}
