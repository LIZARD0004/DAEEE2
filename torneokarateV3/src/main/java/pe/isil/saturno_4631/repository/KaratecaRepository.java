package pe.isil.saturno_4631.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.isil.saturno_4631.model.Karateca;

@Repository
public interface KaratecaRepository extends JpaRepository<Karateca, Integer> {
    Page<Karateca> findByNombreCompletoContaining(String nombre, Pageable pageable);

    //List<Karateca> findTop4ByOrderByFechaCumpleaños();
}
