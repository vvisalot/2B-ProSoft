package grupo2b.odiroute.service;

import grupo2b.odiroute.model.Venta;
import grupo2b.odiroute.repository.OficinaRepository;
import grupo2b.odiroute.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired
    private final VentaRepository ventaRepository;

    @Autowired
    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    // Crear una nueva venta y guardarla en la base de datos
    public Venta crearVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    // Obtener todas las ventas almacenadas
    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    // Obtener una venta específica por su ID
    public Optional<Venta> obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id);
    }

    // Actualizar una venta existente
    public Venta actualizarVenta(Long id, Venta ventaActualizada) {
        return ventaRepository.findById(id).map(venta -> {
            venta.setFechaHora(ventaActualizada.getFechaHora());
            venta.setOrigen(ventaActualizada.getOrigen());
            venta.setDestino(ventaActualizada.getDestino());
            venta.setCantidad(ventaActualizada.getCantidad());
            venta.setIdCliente(ventaActualizada.getIdCliente());
            return ventaRepository.save(venta);
        }).orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    // Eliminar una venta por su ID
    public void eliminarVenta(Long id) {
        ventaRepository.deleteById(id);
    }
}
