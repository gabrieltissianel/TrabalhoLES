
import 'package:dio/dio.dart';
import 'package:les/core/endpoints.dart';
import 'package:les/core/generic_service.dart';
import 'package:les/model/compra/compra.dart';
import 'package:result_dart/result_dart.dart';

class CompraService extends GenericService<Compra> {

  CompraService(Dio dio) : super(dio, 'compra', (data) => Compra.fromJson(data));

  AsyncResult<Compra> concluir(int id) async {
    refreshToken();
    try {
      final response = await dio.put('${Endpoints.baseUrl}/compra/concluir/$id');
      return Success(fromJson(response.data));
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

  AsyncResult<Compra> getCompraByCartao(String cartao) async {
    try{
      final response = await dio.get('${Endpoints.baseUrl}/cliente/compraaberta/$cartao');
      return Success(fromJson(response.data));
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

}