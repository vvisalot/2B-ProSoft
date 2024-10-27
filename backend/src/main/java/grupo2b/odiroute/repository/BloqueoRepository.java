package grupo2b.odiroute.repository;

import grupo2b.odiroute.model.Bloqueo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BloqueoRepository extends CrudRepository<Bloqueo, Integer>{
    Optional<model.Bloqueo> findBloqueoPorCodigo(String codigo);
}