package br.com.gerenciadordeveiculos.dtos;

import br.com.gerenciadordeveiculos.enums.TipoCombustivel;
import br.com.gerenciadordeveiculos.enums.TipoVeiculo;
import lombok.Data;

@Data
public class VeiculoResponseDTO {

    private Integer id;

    private TipoVeiculo tipo;

    private String modelo;

    private String fabricante;

    private Integer ano;

    private Double preco;

    private Integer quantidadePortas;

    private TipoCombustivel tipoCombustivel;

    private Integer cilindrada;
}
