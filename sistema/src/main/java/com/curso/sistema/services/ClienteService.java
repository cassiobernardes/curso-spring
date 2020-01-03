package com.curso.sistema.services;

import com.curso.sistema.dto.ClienteDTO;
import com.curso.sistema.dto.ClienteNewDTO;
import com.curso.sistema.models.Cidade;
import com.curso.sistema.models.Cliente;
import com.curso.sistema.models.Endereco;
import com.curso.sistema.respositories.ClienteRepository;
import com.curso.sistema.respositories.EnderecoRepository;
import com.curso.sistema.services.exceptions.AuthorizationException;
import com.curso.sistema.services.exceptions.DataIntegrityException;
import com.curso.sistema.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Value("${img.prefix.client.profile}")
    private String prefix;

    private final BCryptPasswordEncoder passwordEncoder;

    private final ClienteRepository clienteRepository;

    private final EnderecoRepository enderecoRepository;

    private final S3Service s3Service;

    private final ImageService imageService;

    public ClienteService(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository, BCryptPasswordEncoder passwordEncoder, S3Service s3Service, ImageService imageService) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Service = s3Service;
        this.imageService = imageService;
    }

    public Cliente find(Long id) {

        Optional<Cliente> obj = clienteRepository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));

    }

    public Cliente findByEmail(String email) {

        Cliente obj = clienteRepository.findByEmail(email);

        if(obj == null){
            throw new ObjectNotFoundException(
                    "Objeto não encontrado! E-mail: " + email + ", Tipo: " + Cliente.class.getName());
        } else{
            return obj;
        }
    }

    public List<Cliente> findAll(){
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente insert(Cliente cliente){
        cliente.setId(null);
        cliente = clienteRepository.save(cliente);
        enderecoRepository.saveAll(cliente.getEnderecos());
        return cliente;
    }

    public Cliente update(Cliente newCliente) {
        Cliente cliente = find(newCliente.getId());
        updateClienteData(cliente, newCliente);
        return clienteRepository.save(cliente);
    }

    public void delete(Long id) {
        try{
            clienteRepository.delete(find(id));
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionadas!");
        }
    }

    public Page<Cliente> findPage(Integer page, Integer size, String orderBy, String direction){
        PageRequest pageRequest;
        if(direction.equalsIgnoreCase("DESC")){
            pageRequest = PageRequest.of(page, size, Sort.by(orderBy).descending());
        } else {
            pageRequest = PageRequest.of(page, size, Sort.by(orderBy).ascending());
        }
        return clienteRepository.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO clienteDTO) {
        return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null, null);
    }

    public Cliente fromDTO(ClienteNewDTO clienteNewDTO) {
        Cidade cidade = new Cidade(clienteNewDTO.getCidadeId(), null, null);
        Cliente cliente = new Cliente(null,
                clienteNewDTO.getNome(),
                clienteNewDTO.getEmail(),
                clienteNewDTO.getCpfOuCnpj(),
                clienteNewDTO.getTipoCliente(),
                passwordEncoder.encode(clienteNewDTO.getSenha()));
        Endereco endereco = new Endereco(null,
                clienteNewDTO.getLogradouro(),
                clienteNewDTO.getNumero(),
                clienteNewDTO.getComplemento(),
                clienteNewDTO.getBairro(),
                clienteNewDTO.getCep(),
                cliente, cidade);
        cliente.getEnderecos().add(endereco);
        cliente.getTelefones().add(clienteNewDTO.getTelefone1());
        if(clienteNewDTO.getTelefone2() != null){
            cliente.getTelefones().add(clienteNewDTO.getTelefone2());
        }
        if(clienteNewDTO.getTelefone3() != null){
            cliente.getTelefones().add(clienteNewDTO.getTelefone3());
        }

        return cliente;

    }

    private void updateClienteData(Cliente cliente, Cliente newCliente){
        cliente.setNome(newCliente.getNome());
        cliente.setEmail(newCliente.getEmail());
    }

    public URI uploadProfilePicture(MultipartFile multipartFile){

        Cliente clienteAuthenticated = getClienteAuthenticated();

        if(clienteAuthenticated == null){
            throw new AuthorizationException("Acesso negado");
        }

        BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);

        String fileName = prefix + clienteAuthenticated.getId() + ".jpg";

        return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");

    }

    private Cliente getClienteAuthenticated() {

        Cliente cliente;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            cliente = clienteRepository.findByEmail(username);
            return cliente;
        } else {
            String username = principal.toString();
            cliente = clienteRepository.findByEmail(username);
            return cliente;
        }
    }
}
