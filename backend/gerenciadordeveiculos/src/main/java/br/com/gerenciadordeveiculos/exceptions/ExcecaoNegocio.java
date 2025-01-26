package br.com.gerenciadordeveiculos.exceptions;

public class ExcecaoNegocio extends RuntimeException {

    public ExcecaoNegocio(String mensagem) {
        super(mensagem);
    }
}
