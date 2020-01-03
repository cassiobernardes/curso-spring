package com.curso.sistema.controllers.exceptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ValidationError extends StandardError {

    private static final long serialVersionUID = 1L;

    List<FieldMessage> erros = new ArrayList<>();

    public ValidationError(Integer status, String message, Date timeStamp) {
        super(status, message, timeStamp);
    }

    public List<FieldMessage> getErros() {
        return erros;
    }

    public void addError(String fieldName, String message) {
        erros.add(new FieldMessage(fieldName, message));
    }
}
