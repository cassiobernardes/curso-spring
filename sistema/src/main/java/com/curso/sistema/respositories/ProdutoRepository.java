package com.curso.sistema.respositories;

import com.curso.sistema.models.Categoria;
import com.curso.sistema.models.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE\n" +
            "obj.nome LIKE %:nome% AND cat IN :categorias")
    Page<Produto> findDistinctByNomeContainingAndCategoriasIn(@Param("nome") String nome,
                         @Param("categorias") List<Categoria> categorias,
                         Pageable pageRequest);

    Produto findProdutoById(Long id);
}