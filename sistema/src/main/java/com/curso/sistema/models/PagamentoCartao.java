package com.curso.sistema.models;

import com.curso.sistema.models.enums.EstadoPagamento;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pagamento_cartao")
@JsonTypeName("pagamentoCartao")
public class PagamentoCartao extends Pagamento {

    private static final long serialVersionUID = 1L;

    private Integer numeroParcelas;

    public PagamentoCartao() {
    }

    public PagamentoCartao(Integer numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public PagamentoCartao(Integer estadoPagamento, Pedido pedido, Integer numeroParcelas) {
        super(estadoPagamento, pedido);
        this.numeroParcelas = numeroParcelas;
    }

    public Integer getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(Integer numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }
}
