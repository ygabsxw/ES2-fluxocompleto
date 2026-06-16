package com.agenda.repository;

import com.agenda.model.ProfissionalSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfissionalSaudeRepository extends JpaRepository<ProfissionalSaude, Long> {
    
    // Consultar(nome)
    List<ProfissionalSaude> findByNomeContainingIgnoreCase(String nome);
    
    // Consultar(categoria)
    List<ProfissionalSaude> findByCategoriasContainingIgnoreCase(String categoria);
}