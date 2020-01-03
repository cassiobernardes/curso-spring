package com.curso.sistema.services;

import com.curso.sistema.models.Categoria;
import com.curso.sistema.models.Produto;
import com.curso.sistema.respositories.CategoriaRepository;
import com.curso.sistema.respositories.ProdutoRepository;
import com.curso.sistema.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    private final CategoriaRepository categoriaRepository;
    
    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Produto find(Long id) {

        Optional<Produto> obj = produtoRepository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));

    }

    public Page<Produto> search(String nome, List<Long> ids, Integer page, Integer size, String orderBy, String direction){
        PageRequest pageRequest;
        List<Categoria> categorias = categoriaRepository.findAllById(ids);

        if(direction.equalsIgnoreCase("DESC")){
            pageRequest = PageRequest.of(page, size, Sort.by(orderBy).descending());
        } else {
            pageRequest = PageRequest.of(page, size, Sort.by(orderBy).ascending());
        }


        return produtoRepository.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
    }
}
