package grupo2b.odiroute.repository;

import grupo2b.odiroute.model.Oficina;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Long>{
    @Query("FROM Oficina o WHERE o.codigo = :codigo")
    Optional<Oficina> findByCodigo(@Param("codigo") String codigo);
}
