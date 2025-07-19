package pe.isil.saturno_4631.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.isil.saturno_4631.model.Liga;

@Repository
public interface LigaRepository extends JpaRepository<Liga, Integer> {

}
