package pe.isil.saturno_4631.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Llave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_llave")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_karateca1")
    private Karateca idKarateca1;

    @ManyToOne
    @JoinColumn(name = "id_karateca2")
    private Karateca idKarateca2;

    private Integer ronda;

    @ManyToOne
    @JoinColumn(name = "id_ganador")
    private Karateca ganador; // puede ser null al inicio
}
