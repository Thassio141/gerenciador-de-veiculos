package br.com.gerenciadordeveiculos.models;

import br.com.gerenciadordeveiculos.enums.TipoVeiculo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Veiculo {

    protected Integer id;

    protected TipoVeiculo tipo;

    protected String modelo;

    protected String fabricante;

    protected Integer ano;

    protected Double preco;

    protected String cor;
}