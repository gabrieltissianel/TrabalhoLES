
import 'package:dio/dio.dart';
import 'package:les/core/generic_service.dart';
import 'package:les/model/usuario/usuario.dart';

class UsuarioService extends GenericService<Usuario> {

  UsuarioService(Dio dio) : super(dio, 'usuario', (data) => Usuario.fromJson(data));

}