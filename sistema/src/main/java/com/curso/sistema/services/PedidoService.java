package com.curso.sistema.services;

import com.curso.sistema.models.ItemPedido;
import com.curso.sistema.models.PagamentoBoleto;
import com.curso.sistema.models.Pedido;
import com.curso.sistema.models.Produto;
import com.curso.sistema.models.enums.EstadoPagamento;
import com.curso.sistema.respositories.ItemPedidoRepository;
import com.curso.sistema.respositories.PagamentoRepository;
import com.curso.sistema.respositories.PedidoRepository;
import com.curso.sistema.respositories.ProdutoRepository;
import com.curso.sistema.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final PagamentoRepository pagamentoRepository;

    private final ProdutoRepository produtoRepository;

    private final ItemPedidoRepository itemPedidoRepository;

    private final ClienteService clienteService;

    private final SmtpEmailService smtpEmailService;

    public PedidoService(PedidoRepository pedidoRepository,
                         PagamentoRepository pagamentoRepository,
                         ProdutoRepository produtoRepository,
                         ItemPedidoRepository itemPedidoRepository, ClienteService clienteService, SmtpEmailService smtpEmailService) {
        this.pedidoRepository = pedidoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.produtoRepository = produtoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.clienteService = clienteService;
        this.smtpEmailService = smtpEmailService;
    }

    public Pedido find(Long id) {

        Optional<Pedido> obj = pedidoRepository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));

    }

    @Transactional
    public Pedido insert(Pedido pedido) {
        Produto produto;

        pedido.setId(null);
        pedido.setInstante(new Date());
        pedido.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE.getCodigo());
        pedido.getPagamento().setPedido(pedido);

        if(pedido.getPagamento() instanceof PagamentoBoleto){
            PagamentoBoleto pagamentoBoleto = (PagamentoBoleto) pedido.getPagamento();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(pedido.getInstante());
            calendar.add(Calendar.DAY_OF_MONTH, 7);
            pagamentoBoleto.setDataVencimento(calendar.getTime());
        }

        pedido.setCliente(clienteService.find(pedido.getCliente().getId()));

        pedido = pedidoRepository.save(pedido);
        pagamentoRepository.save(pedido.getPagamento());

        for(ItemPedido itemPedido : pedido.getItens()){
            itemPedido.setDesconto((double) 0);
            produto = produtoRepository.findProdutoById(itemPedido.getProduto().getId());
            itemPedido.setProduto(produto);
            itemPedido.setPreco(produto.getPreco());
            itemPedido.setPedido(pedido);
        }

        itemPedidoRepository.saveAll(pedido.getItens());
        smtpEmailService.sendEmail(pedido);
        return pedido;
    }
}
