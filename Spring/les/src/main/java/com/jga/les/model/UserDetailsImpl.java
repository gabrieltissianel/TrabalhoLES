package com.jga.les.model;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
public class UserDetailsImpl implements UserDetails {

    private final Usuario usuario;

    public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getPermissoes().stream()
                .flatMap(permissao -> {
                    List<String> autoridades = new ArrayList<>();
                    String nomeTela = permissao.getTela().getNome().toUpperCase();
                    autoridades.add(nomeTela);
                    if (permissao.isAdd()) autoridades.add(nomeTela + "_ADD");
                    if (permissao.isEdit()) autoridades.add(nomeTela + "_EDIT");
                    if (permissao.isDelete()) autoridades.add(nomeTela + "_DELETE");
                    return autoridades.stream();
                })
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getLogin();
    }
}
