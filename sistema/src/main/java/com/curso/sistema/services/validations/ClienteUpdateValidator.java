package com.curso.sistema.services.validations;

import com.curso.sistema.controllers.exceptions.FieldMessage;
import com.curso.sistema.dto.ClienteDTO;
import com.curso.sistema.dto.ClienteNewDTO;
import com.curso.sistema.models.Cliente;
import com.curso.sistema.models.enums.TipoCliente;
import com.curso.sistema.respositories.ClienteRepository;
import com.curso.sistema.services.validations.utils.BR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private HttpServletRequest servletRequest;

    @Override
    public void initialize(ClienteUpdate ann) {
    }

    @Override
    public boolean isValid(ClienteDTO clienteDTO, ConstraintValidatorContext context) {

        Map<String, String> map = (Map<String, String>) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        List<FieldMessage> list = new ArrayList<>();

        Long clienteId = Long.valueOf(map.get("id"));

        Cliente cliente = clienteRepository.findByEmail(clienteDTO.getEmail());

        if(cliente != null && !cliente.getId().equals(clienteId)){
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
