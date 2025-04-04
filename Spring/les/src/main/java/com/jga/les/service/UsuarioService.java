package com.jga.les.service;

import com.jga.les.dtos.LoginDto;
import com.jga.les.exceptions.NotFoundException;
import com.jga.les.model.Permissao;
import com.jga.les.model.UserDetailsImpl;
import com.jga.les.model.Usuario;
import com.jga.les.repository.PermissaoRepository;
import com.jga.les.repository.UsuarioRepository;

import com.jga.les.security.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends GenericService<Usuario> {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private PermissaoRepository permissaoRepository;

    UsuarioRepository usuarioRepository = ((UsuarioRepository) objRepository);

    public UsuarioService(JpaRepository<Usuario, Long> objRepository) {
        super(objRepository, Usuario.class);
    }

    public ResponseEntity<String> authenticateUser(LoginDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.login(), loginUserDto.senha());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return ResponseEntity.ok().body(jwtTokenService.generateToken(userDetails));
    }

    @Override
    public ResponseEntity<Usuario> add(Usuario obj) throws IllegalArgumentException, OptimisticLockingFailureException {
        //VERIFICO SE O LOGIN USADO JA EXISTE ANTES DE SALVAR PRO BANCO
        if(usuarioRepository.findByLogin(obj.getLogin()).isPresent()){
            throw new RuntimeException("Login já Existe");
        }
        String sen = securityConfiguration.passwordEncoder().encode(obj.getSenha());
        obj.setSenha(sen);

        for (Permissao permissao : obj.getPermissoes()) {
            if (permissao.getId() == null) {
                permissao.setId( permissaoRepository.save(permissao).getId() );
            }
        }

        return super.add(obj);
    }

    @Override
    public ResponseEntity<String> remove(long id) {
        Usuario usuario = objRepository.findById(id).orElseThrow(()-> new NotFoundException(this.classe.getSimpleName()));

        permissaoRepository.deleteAll(usuario.getPermissoes());

        return super.remove(id);
    }

    @Override
    public ResponseEntity<Usuario> update(Usuario obj, long id) {
        String sen = securityConfiguration.passwordEncoder().encode(obj.getSenha());
        obj.setSenha(sen);

        Usuario usuario = objRepository.findById(id).orElseThrow(
                ()-> new NotFoundException(this.classe.getSimpleName()));

        for (Permissao permissao : usuario.getPermissoes()) {
            if (!obj.getPermissoes().contains(permissao)) {
                permissaoRepository.delete(permissao);
            }
        }

        for (Permissao permissao : obj.getPermissoes()) {
            if (permissao.getId() == null) {
                permissao.setId( permissaoRepository.save(permissao).getId() );
            }
        }

        return super.update(obj, id);
    }


}