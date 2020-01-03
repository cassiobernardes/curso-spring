package com.curso.sistema.models.enums;

public enum EstadoPagamento {

    PENDENTE(1, "Pendente"),
    QUITADO(2, "Quitado"),
    CANCELADO(3, "Cancelado");

    private Integer codigo;
    private String status;

    EstadoPagamento(Integer codigo, String status) {
        this.codigo = codigo;
        this.status = status;
    }

    public static EstadoPagamento toEnum(Integer codigo){

        if(codigo == null)
            return null;

        for(EstadoPagamento estadoPagamento : EstadoPagamento.values()){
            if(estadoPagamento.getCodigo().equals(codigo))
                return estadoPagamento;
        }

        throw new IllegalArgumentException("Código Inválido: " + codigo);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
