package com.curso.sistema.models.enums;

public enum TipoCliente {

    PESSOA_FISICA(1, "Pessoa Física"),
    PESSOA_JURIDICA(2, "Pessoa Jurídica");

    private Integer codigo;
    private String descricao;

    TipoCliente(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static TipoCliente toEnum(Integer codigo){

        if(codigo == null)
            return null;

        for(TipoCliente tipoCliente : TipoCliente.values()){
            if(tipoCliente.getCodigo().equals(codigo))
                return tipoCliente;
        }

        throw new IllegalArgumentException("Código Inválido: " + codigo);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
