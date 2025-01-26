package br.com.gerenciadordeveiculos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManipuladorExcecao {

    @ExceptionHandler(ExcecaoNegocio.class)
    public ResponseEntity<Map<String, String>> tratarExcecaoNegocio(ExcecaoNegocio ex) {
        Map<String, String> corpoErro = new HashMap<>();
        corpoErro.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(corpoErro);
    }
}
