package com.jga.les.dtos;

import java.util.List;

public record UsuarioDto(long id, String nome, String Login, String token, List<String> permissoes) {

}
