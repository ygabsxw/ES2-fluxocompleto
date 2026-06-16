package com.agenda.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "profissional_saude")
public class ProfissionalSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String telefone;
    private String endereco;

    @ElementCollection
    @CollectionTable(name = "profissional_categoria", joinColumns = @JoinColumn(name = "profissional_id"))
    @Column(name = "categoria")
    private List<String> categorias; 

    // Construtores
    public ProfissionalSaude() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public List<String> getCategorias() { return categorias; }
    public void setCategorias(List<String> categorias) { this.categorias = categorias; }
}