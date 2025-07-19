package pe.isil.saturno_4631.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.isil.saturno_4631.model.Usuario;
import pe.isil.saturno_4631.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado:" + username));

        return new AppUserDetails(usuario);
    }
}
