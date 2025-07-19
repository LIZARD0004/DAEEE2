package pe.isil.saturno_4631.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "liga", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ruc"})
})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Liga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_liga")
    @EqualsAndHashCode.Include
    private Integer id;

    private String nombre;

    private String ruc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
}
