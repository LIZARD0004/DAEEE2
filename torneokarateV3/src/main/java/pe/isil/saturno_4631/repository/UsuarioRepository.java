package pe.isil.saturno_4631.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.isil.saturno_4631.model.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Integer> {

    Optional <Usuario> findByEmail (String email);

    Boolean existsByEmail (String email);
}
