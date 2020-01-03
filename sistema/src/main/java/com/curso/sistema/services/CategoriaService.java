package com.curso.sistema.services;

import com.curso.sistema.dto.CategoriaDTO;
import com.curso.sistema.models.Categoria;
import com.curso.sistema.respositories.CategoriaRepository;
import com.curso.sistema.services.exceptions.DataIntegrityException;
import com.curso.sistema.services.exceptions.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria find(Long id) {
        Optional<Categoria> obj = categoriaRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                    "Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
    }

    public List<Categoria> findAll(){
        return categoriaRepository.findAll();
    }

    @Transactional
    public Categoria insert(Categoria categoria){
        categoria.setId(null);
        return categoriaRepository.save(categoria);
    }

    public Categoria update(Categoria newCategoria) {
        Categoria categoria = find(newCategoria.getId());
        updateCategoriaData(categoria, newCategoria);
        return categoriaRepository.save(categoria);
    }

    public void delete(Long id) {
        try{
            categoriaRepository.delete(find(id));
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Não é possível excluir uma Categoria que possui Produto(s)!");
        }
    }

    public Page<Categoria> findPage(Integer page, Integer size, String orderBy, String direction){
        PageRequest pageRequest;
        if(direction.equalsIgnoreCase("DESC")){
            pageRequest = PageRequest.of(page, size, Sort.by(orderBy).descending());
        } else {
            pageRequest = PageRequest.of(page, size, Sort.by(orderBy).ascending());
        }
        return categoriaRepository.findAll(pageRequest);
    }

    public Categoria fromDTO(CategoriaDTO categoriaDTO){
        return new Categoria(categoriaDTO.getId(), categoriaDTO.getNome());
    }

    private void updateCategoriaData(Categoria categoria, Categoria newCategoria){
        categoria.setNome(newCategoria.getNome());
    }

}
