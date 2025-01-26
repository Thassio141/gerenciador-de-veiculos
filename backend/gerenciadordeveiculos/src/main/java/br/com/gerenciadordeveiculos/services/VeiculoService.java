package br.com.gerenciadordeveiculos.services;

import br.com.gerenciadordeveiculos.dtos.VeiculoRequestDTO;
import br.com.gerenciadordeveiculos.dtos.VeiculoResponseDTO;
import br.com.gerenciadordeveiculos.enums.TipoVeiculo;

import java.util.List;

public interface VeiculoService {
    VeiculoResponseDTO criarVeiculo(VeiculoRequestDTO dto);

    List<VeiculoResponseDTO> buscarTodos();

    VeiculoResponseDTO buscarPorId(Integer id);

    List<VeiculoResponseDTO> buscarPorFiltros(TipoVeiculo tipo, String modelo, Integer ano, String cor);

    VeiculoResponseDTO atualizarVeiculo(Integer id, VeiculoRequestDTO dto);

    void deletarVeiculo(Integer id);
}
