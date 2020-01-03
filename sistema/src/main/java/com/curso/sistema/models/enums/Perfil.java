package com.curso.sistema.models.enums;

public enum Perfil {

    CLIENTE(1, "CLIENTE"),
    ADMINISTRADOR(2, "ADMINISTRADOR");

    private Integer codigo;
    private String status;

    Perfil(Integer codigo, String status) {
        this.codigo = codigo;
        this.status = status;
    }

    public static Perfil toEnum(Integer codigo){

        if(codigo == null)
            return null;

        for(Perfil estadoPagamento : Perfil.values()){
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
