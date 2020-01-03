package com.curso.sistema.respositories;

import com.curso.sistema.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Transactional(readOnly = true)
    Boolean existsByEmail(String email);

    @Transactional(readOnly = true)
    Cliente findByEmail(String email);

}
