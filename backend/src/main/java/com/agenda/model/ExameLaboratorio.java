package com.agenda.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exame_laboratorio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExameLaboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    private String posologia;
}
