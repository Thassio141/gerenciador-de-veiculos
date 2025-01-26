package br.com.gerenciadordeveiculos.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Veiculo {

    protected Integer id;

    protected String modelo;

    protected String fabricante;

    protected Integer ano;

    protected Double preco;
}