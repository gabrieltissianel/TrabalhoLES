package com.jga.les.dtos;

import com.jga.les.model.Permissao;

import java.util.List;

public record UsuarioDto(long id, String nome, String login, String token, List<Permissao> permissoes) {

}
