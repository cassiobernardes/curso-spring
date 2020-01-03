package com.curso.sistema.respositories;

import com.curso.sistema.models.ItemPedido;
import com.curso.sistema.models.ItemPedidoPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

}
