import 'package:flutter/material.dart';
import 'package:les/model/usuario/usuario.dart';
import 'package:les/services/usuario_service.dart';
import 'package:result_command/result_command.dart';
import 'package:result_dart/result_dart.dart';

class UsuarioViewModel extends ChangeNotifier{
  final UsuarioService _usuarioService;

  UsuarioViewModel(this._usuarioService);

  late final getUsuarios = Command0(_getUsuarioes);
  late final addUsuario = Command1(_addUsuario);
  late final updateUsuario = Command1(_updateUsuario);
  late final deleteUsuario = Command1(_deleteUsuario);
  late final getUsuarioById = Command1(_getUsuarioById);

  AsyncResult<List<Usuario>> _getUsuarioes() async {
    return _usuarioService.getAll();
  }

  AsyncResult<Usuario> _addUsuario(Usuario usuario) async {
    return _usuarioService.create(usuario.toJson());
  }

  AsyncResult<Usuario> _updateUsuario(Usuario usuario) async {
    return _usuarioService.update(usuario.id!, usuario.toJson());
  }

  AsyncResult<String> _deleteUsuario(int id) async {
    return _usuarioService.delete(id);
  }

  AsyncResult<Usuario> _getUsuarioById(int id) async {
    return _usuarioService.getById(id);
  }
}