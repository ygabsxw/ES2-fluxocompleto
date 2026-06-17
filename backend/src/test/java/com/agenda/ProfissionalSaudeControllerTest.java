package com.agenda;

import com.agenda.controller.ProfissionalSaudeController;
import com.agenda.model.ProfissionalSaude;
import com.agenda.repository.ProfissionalSaudeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfissionalSaudeController.class)
class ProfissionalSaudeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfissionalSaudeRepository repository;

    @Test
    void deveCriarProfissionalComSucesso() throws Exception {
        // Preparando o objeto de teste
        ProfissionalSaude profissional = new ProfissionalSaude();
        profissional.setId(1L);
        profissional.setNome("Ana Souza");
        profissional.setTelefone("31999999999");
        profissional.setEndereco("Rua Central, 123");
        profissional.setCategorias(Arrays.asList("Médico", "Psicólogo"));

        // Simulando o comportamento do banco de dados (Mockito)
        when(repository.save(any(ProfissionalSaude.class))).thenReturn(profissional);

        // Executando a requisição e validando os resultados
        mockMvc.perform(post("/api/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profissional)))
                .andExpect(status().isOk()) // Nota: O controller anterior retorna 200 OK. 
                .andExpect(jsonPath("$.nome").value("Ana Souza"))
                .andExpect(jsonPath("$.categorias[0]").value("Médico"));
    }

    @Test
    void deveRetornar404ParaProfissionalInexistente() throws Exception {
        // Simulando que o ID 999 não existe no banco
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Executando a requisição GET e esperando status 404
        mockMvc.perform(get("/api/profissionais/999"))
                .andExpect(status().isNotFound());
    }
}