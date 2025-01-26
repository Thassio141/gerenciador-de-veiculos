package br.com.gerenciadordeveiculos.entities;

import br.com.gerenciadordeveiculos.enums.TipoCombustivel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Carro extends Veiculo {

    private Integer quantidadePortas;

    private TipoCombustivel tipoCombustivel;

}