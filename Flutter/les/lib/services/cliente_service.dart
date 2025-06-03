
import 'package:dio/dio.dart';
import 'package:les/core/generic_service.dart';
import 'package:les/model/cliente/cliente.dart';
import 'package:result_dart/result_dart.dart';

import '../core/endpoints.dart';

class ClienteService extends GenericService<Cliente> {

  ClienteService(Dio dio) : super(dio, 'cliente', (data) => Cliente.fromJson(data));

  AsyncResult<Cliente> getClienteByCartao(String cartao) async {
    try{
      final response = await dio.get('${Endpoints.baseUrl}/cliente/cartao/$cartao');
      return Success(fromJson(response.data));
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }
}