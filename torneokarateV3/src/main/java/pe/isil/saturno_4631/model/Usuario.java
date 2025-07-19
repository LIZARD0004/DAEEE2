package pe.isil.saturno_4631.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_usuario")
    private Integer id;
    @NotBlank
    private String nombres;
    @NotBlank
    private String apellidos;
    @Column(name = "nom_completo")
    private String nombreCompleto;
    @NotEmpty
    @Email
    private String email;
    private String password;

    //@NotBlank
    @Transient
    private String password1;
    //@NotBlank
    @Transient
    private String password2;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    public enum Rol {
        ADMIN,
        LIGA
    }

    @PrePersist
    @PreUpdate
    void asignarNombreCompleto()
    {
        nombreCompleto= nombres +" "+ apellidos;
    }
}
