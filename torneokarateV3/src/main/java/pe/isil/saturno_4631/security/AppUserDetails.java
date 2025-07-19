package pe.isil.saturno_4631.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.isil.saturno_4631.model.Usuario;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AppUserDetails implements UserDetails {

    private final String nombre;
    private final Usuario usuario;

    public AppUserDetails(Usuario usuario) {
        this.usuario = usuario;
        this.nombre= usuario.getNombres();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (usuario.getRol() == null) {
            // Puedes asignar un rol por defecto o lanzar una excepción más clara
            return List.of(); // Sin roles (opcional: bloquear el login luego)
        }

        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
    }


    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getNombres();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getNombre() {
        return nombre;
    }
}
