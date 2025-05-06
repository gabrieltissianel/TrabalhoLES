import 'package:dio/dio.dart';
import 'package:les/core/endpoints.dart';
import 'package:les/core/generic_service.dart';
import 'package:les/model/produto/historico_produto.dart';
import 'package:result_dart/result_dart.dart';

class HistoricoProdutosService extends GenericService<HistoricoProduto> {

  HistoricoProdutosService(Dio dio) : super(dio, 'historicoprodutos', (data) => HistoricoProduto.fromJson(data));

  AsyncResult<List<HistoricoProduto>> getByProdutoId(int id) async {
    try {
      final response = await dio.get('${Endpoints.baseUrl}/$endpoint/list/$id');
      return Success((response.data as List).map((e) => fromJson(e)).toList());
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }
}