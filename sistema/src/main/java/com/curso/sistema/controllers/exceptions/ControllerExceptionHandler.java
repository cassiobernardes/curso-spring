package com.curso.sistema.controllers.exceptions;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.curso.sistema.services.exceptions.AuthorizationException;
import com.curso.sistema.services.exceptions.DataIntegrityException;
import com.curso.sistema.services.exceptions.FileException;
import com.curso.sistema.services.exceptions.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest httpServletRequest){

        StandardError standardError = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), new Date());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);

    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest httpServletRequest){

        StandardError standardError = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), new Date());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest httpServletRequest){

        ValidationError validationError = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de validação!", new Date());

        for(FieldError fieldError : e.getBindingResult().getFieldErrors()){
            validationError.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationError);

    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<StandardError> file(FileException e, HttpServletRequest httpServletRequest){

        StandardError standardError = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), new Date());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);

    }

    @ExceptionHandler(AmazonServiceException.class)
    public ResponseEntity<StandardError> amazonService(AmazonServiceException e, HttpServletRequest httpServletRequest){

        HttpStatus httpStatus = HttpStatus.valueOf(e.getStatusCode());

        StandardError standardError = new StandardError(httpStatus.value(), e.getMessage(), new Date());

        return ResponseEntity.status(httpStatus.value()).body(standardError);

    }

    @ExceptionHandler(AmazonClientException.class)
    public ResponseEntity<StandardError> amazonClient(AmazonClientException e, HttpServletRequest httpServletRequest){

        StandardError standardError = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), new Date());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);

    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<StandardError> amazonS3(AmazonS3Exception e, HttpServletRequest httpServletRequest){

        StandardError standardError = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), new Date());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardError);

    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request) {

        StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(), e.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

}
