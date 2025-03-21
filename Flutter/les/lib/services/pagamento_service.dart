
import 'package:dio/dio.dart';
import 'package:les/core/endpoints.dart';
import 'package:les/core/generic_service.dart';
import 'package:les/model/fornecedor/pagamento.dart';
import 'package:result_dart/result_dart.dart';

class PagamentoService extends GenericService<Pagamento>{

  PagamentoService(Dio dio): super(dio, 'pagamento', (data) => Pagamento.fromJson(data));

  AsyncResult<List<Pagamento>> getPagamentoByFornecedorId(int id) async {
    try {
      final response = await dio.get('${Endpoints.baseUrl}/$endpoint/list/$id');
      return Success((response.data as List).map((e) => fromJson(e)).toList());
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

}