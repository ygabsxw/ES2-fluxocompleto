import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({ baseURL: API_URL });

// ========== PROFISSIONAIS DE SAÚDE ==========
export const profissionalService = {
    listar:             () => api.get('/profissionais'),
    buscar:             (id) => api.get(`/profissionais/${id}`),
    criar:              (profissional) => api.post('/profissionais', profissional),
    atualizar:          (id, dados) => api.put(`/profissionais/${id}`, dados),
    deletar:            (id) => api.delete(`/profissionais/${id}`),
    
    // Consultas específicas definidas no diagrama
    buscarPorNome:      (nome) => api.get('/profissionais/buscar/nome', { params: { nome } }),
    buscarPorCategoria: (categoria) => api.get('/profissionais/buscar/categoria', { params: { categoria } })
};

export default api;