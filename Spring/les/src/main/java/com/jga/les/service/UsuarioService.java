package com.jga.les.service;

import com.jga.les.exceptions.NotFoundException;
import com.jga.les.model.Permissao;
import com.jga.les.model.Usuario;
import com.jga.les.repository.PermissaoRepository;
import com.jga.les.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends GenericService<Usuario> {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PermissaoRepository permissaoRepository;

    public UsuarioService(JpaRepository<Usuario, Long> objRepository) {
        super(objRepository, Usuario.class);
    }

    @Override
    public ResponseEntity<Usuario> add(Usuario obj) throws IllegalArgumentException, OptimisticLockingFailureException {
        //VERIFICO SE O LOGIN USADO JA EXISTE ANTES DE SALVAR PRO BANCO
        if(((UsuarioRepository) objRepository).findByLogin(obj.getLogin()) != null){
            throw new RuntimeException("Login já Existe");
        }
        String sen = passwordEncoder.encode(obj.getPassword());
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
        String sen = passwordEncoder.encode(obj.getPassword());
        obj.setSenha(sen);

        for (Permissao permissao : obj.getPermissoes()) {
            if (permissao.getId() == null) {
                permissao.setId( permissaoRepository.save(permissao).getId() );
            }
        }

        return super.update(obj, id);
    }


}