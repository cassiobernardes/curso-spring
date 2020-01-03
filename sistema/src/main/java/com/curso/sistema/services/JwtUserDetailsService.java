package com.curso.sistema.services;

import com.curso.sistema.models.Cliente;
import com.curso.sistema.respositories.ClienteRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final ClienteRepository clienteRepository;

    public JwtUserDetailsService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(email);

        if(cliente == null){
            throw new UsernameNotFoundException(email);
        } else{
            return cliente;
        }
    }
}
