package grupo2b.odiroute.service;

import grupo2b.odiroute.model.Camion;
import grupo2b.odiroute.repository.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CamionService {

    private final CamionRepository camionRepository;

    @Autowired
    public CamionService(CamionRepository camionRepository) {
        this.camionRepository = camionRepository;
    }


    public List<Camion> findAll() {
        return camionRepository.findAll();
    }

    public Optional<Camion> findById(Long id) {
        return camionRepository.findById(id);
    }

    public Camion save(Camion camion) {
        return camionRepository.save(camion);
    }

    public Camion update(Long id, Camion camion) {
        return camionRepository.findById(id).map(existingCamion -> {
            existingCamion.setCodigo(camion.getCodigo());
            existingCamion.setTipo(camion.getTipo());
            existingCamion.setCapacidad(camion.getCapacidad());
            existingCamion.setCargaActual(camion.getCargaActual());
            existingCamion.setPaquetes(camion.getPaquetes());
            existingCamion.setPosicionFinal(camion.getPosicionFinal());
            existingCamion.setFechaDeLlegadaPosicionFinal(camion.getFechaDeLlegadaPosicionFinal());
            existingCamion.setEnRuta(camion.getEnRuta());
            existingCamion.setMantenimientosProgramados(camion.getMantenimientosProgramados());
            existingCamion.setFechaUltimoMantenimiento(camion.getFechaUltimoMantenimiento());
            existingCamion.setEnMantenimiento(camion.getEnMantenimiento());
            existingCamion.setAlmacenCarga(camion.getAlmacenCarga());
            existingCamion.setRegresoAlmacen(camion.getRegresoAlmacen());
            return camionRepository.save(existingCamion);
        }).orElseThrow(() -> new RuntimeException("Camión no encontrado con el ID " + id));
    }

    public void deleteById(Long id) {
        camionRepository.deleteById(id);
    }
}
