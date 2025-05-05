
import 'package:dio/dio.dart';
import 'package:les/core/generic_service.dart';
import 'package:les/model/cliente/cliente.dart';

class ClienteService extends GenericService<Cliente> {

  ClienteService(Dio dio) : super(dio, 'cliente', (data) => Cliente.fromJson(data));

}