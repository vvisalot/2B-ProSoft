package grupo2b.odiroute.service;

import grupo2b.odiroute.model.Oficina;
import grupo2b.odiroute.repository.OficinaRepository;
import grupo2b.odiroute.utils.LeerDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OficinaService {

    private final OficinaRepository oficinaRepository;

    @Autowired
    public OficinaService(OficinaRepository oficinaRepository) {
        this.oficinaRepository = oficinaRepository;
    }

    public List<Oficina> getAllOficinas() {
        return oficinaRepository.findAll();
    }

    public Optional<Oficina> getOficinaById(Long id) {
        return oficinaRepository.findById(id);
    }

    public Oficina saveOficina(Oficina oficina) {
        return oficinaRepository.save(oficina);
    }

    public void deleteOficina(Long id) {
        oficinaRepository.deleteById(id);
    }

    public Oficina findByCodigo(String codigo) {
        Optional<Oficina> oficina = oficinaRepository.findByCodigo(codigo);
        return oficina.orElse(null);
    }
    
    public void guardarOficinasDesdeArchivo(String filePath) {
        Map<String, Oficina> oficinas = LeerDatos.leerOficinasDesdeArchivo(filePath);
        oficinaRepository.saveAll(oficinas.values());
    }
}
