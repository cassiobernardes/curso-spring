package com.curso.sistema.models;

import com.curso.sistema.models.enums.Perfil;
import com.curso.sistema.models.enums.TipoCliente;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "cliente")
public class Cliente implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;
    private String email;
    private String CpfOuCnpj;
    private Integer tipoCliente;

    @JsonIgnore
    private String senha;

    @OneToMany(mappedBy = "cliente", targetEntity = Endereco.class, cascade = CascadeType.ALL)
    private Set<Endereco> enderecos;

    @ElementCollection
    @CollectionTable(name = "telefone")
    @Column(name = "telefone")
    private Set<String> telefones;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente", targetEntity = Pedido.class)
    private Set<Pedido> pedidos;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "perfil")
    @Column(name = "perfil")
    private Set<Integer> perfis;

    public Cliente() {
        this.enderecos = new HashSet<>();
        this.telefones = new HashSet<>();
        this.pedidos = new HashSet<>();
        this.perfis = new HashSet<>();
        addPerfil(Perfil.CLIENTE);
    }

    public Cliente(Long id, String nome, String email, String CpfOuCnpj, Integer tipoCliente, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.CpfOuCnpj = CpfOuCnpj;
        this.tipoCliente = tipoCliente;
        this.senha = senha;
        this.enderecos = new HashSet<>();
        this.telefones = new HashSet<>();
        this.pedidos = new HashSet<>();
        this.perfis = new HashSet<>();
        addPerfil(Perfil.CLIENTE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return id.equals(cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addPerfil(Perfil perfil){
        perfis.add(perfil.getCodigo());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpfOuCnpj() {
        return CpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        CpfOuCnpj = cpfOuCnpj;
    }

    public TipoCliente getTipoCliente() {
        return TipoCliente.toEnum(tipoCliente);
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente.getCodigo();
    }

    public Set<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(Set<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Set<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<String> telefones) {
        this.telefones = telefones;
    }

    public Set<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Set<Perfil> getPerfis() {
        return perfis.stream().map(Perfil::toEnum).collect(Collectors.toSet());
    }

    public void setPerfis(Set<Integer> perfis) {
        this.perfis = perfis;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> auths = new ArrayList<>();
        for(Integer integer : perfis){
            auths.add(new SimpleGrantedAuthority(Perfil.toEnum(integer).getStatus()));
        }
        return auths;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
