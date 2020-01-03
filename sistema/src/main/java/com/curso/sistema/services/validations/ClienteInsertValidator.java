package com.curso.sistema.services.validations;

import com.curso.sistema.controllers.exceptions.FieldMessage;
import com.curso.sistema.dto.ClienteNewDTO;
import com.curso.sistema.models.enums.TipoCliente;
import com.curso.sistema.respositories.ClienteRepository;
import com.curso.sistema.services.validations.utils.BR;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void initialize(ClienteInsert ann) {
    }

    @Override
    public boolean isValid(ClienteNewDTO clienteNewDTO, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();

        if(clienteNewDTO.getTipoCliente().equals(TipoCliente.PESSOA_FISICA.getCodigo()) &&
        !BR.isValidCPF(clienteNewDTO.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj", "CPF Inválido!"));
        }

        if(clienteNewDTO.getTipoCliente().equals(TipoCliente.PESSOA_JURIDICA.getCodigo()) &&
                !BR.isValidCNPJ(clienteNewDTO.getCpfOuCnpj())){
            list.add(new FieldMessage("cpfOuCnpj", "CNPJ Inválido!"));
        }

        if(clienteRepository.existsByEmail(clienteNewDTO.getEmail())){
            list.add(new FieldMessage("email", "E-mail já existente!"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }

}
