package com.agenda.model;

import com.agenda.enums.TipoReceita;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "atendimento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @Column(nullable = false)
    private String titulo;

    // Link gerado pela integração com API de vídeo (ex: Google Meet, Zoom)
    @Column(name = "link_videoconferencia")
    private String linkVideoconferencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_receita")
    private TipoReceita receita;

    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private ProfissionalSaude profissional;

    @ManyToOne
    @JoinColumn(name = "exame_id")
    private ExameLaboratorio exameLaboratorio;
}
