import React, { useState } from 'react';
import { profissionalService } from '../services/api';

const ProfissionalForm = () => {
    const [formData, setFormData] = useState({
        nome: '',
        telefone: '',
        endereco: '',
        categorias: []
    });

    const [categoriaInput, setCategoriaInput] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await profissionalService.inserir(formData);
            alert('Profissional cadastrado com sucesso!');
            setFormData({ nome: '', telefone: '', endereco: '', categorias: [] });
        } catch (error) {
            console.error('Erro ao cadastrar:', error);
            alert('Erro ao salvar profissional.');
        }
    };

    const handleAddCategoria = () => {
        if (categoriaInput && !formData.categorias.includes(categoriaInput)) {
            setFormData({
                ...formData,
                categorias: [...formData.categorias, categoriaInput]
            });
            setCategoriaInput('');
        }
    };

    return (
        <div>
            <h2>Cadastro de Profissional de Saúde</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Nome:</label>
                    <input 
                        type="text" 
                        value={formData.nome} 
                        onChange={(e) => setFormData({...formData, nome: e.target.value})} 
                        required 
                    />
                </div>
                <div>
                    <label>Telefone:</label>
                    <input 
                        type="text" 
                        value={formData.telefone} 
                        onChange={(e) => setFormData({...formData, telefone: e.target.value})} 
                    />
                </div>
                <div>
                    <label>Endereço:</label>
                    <input 
                        type="text" 
                        value={formData.endereco} 
                        onChange={(e) => setFormData({...formData, endereco: e.target.value})} 
                    />
                </div>
                
                {/* Lógica para adicionar a lista de categorias */}
                <div>
                    <label>Categoria (Psicólogo, Fisioterapeuta, Médico):</label>
                    <select value={categoriaInput} onChange={(e) => setCategoriaInput(e.target.value)}>
                        <option value="">Selecione...</option>
                        <option value="Psicólogo">Psicólogo</option>
                        <option value="Fisioterapeuta">Fisioterapeuta</option>
                        <option value="Médico">Médico</option>
                    </select>
                    <button type="button" onClick={handleAddCategoria}>Adicionar</button>
                </div>
                
                <ul>
                    {formData.categorias.map((cat, index) => (
                        <li key={index}>{cat}</li>
                    ))}
                </ul>

                <button type="submit">Salvar</button>
            </form>
        </div>
    );
};

export default ProfissionalForm;