
import 'package:dio/dio.dart';
import 'package:les/core/endpoints.dart';
import 'package:les/core/generic_service.dart';
import 'package:les/model/produto/produto.dart';
import 'package:result_dart/result_dart.dart';

class ProdutoService extends GenericService<Produto> {

  ProdutoService(Dio dio) : super(dio, 'produto', (data) => Produto.fromJson(data));

  AsyncResult<String> imprimirCodigoBarras(String codigo, int qtde) async {
    try{
      await dio.post('${Endpoints.baseUrl}/printerdl200/barcode?data=$codigo&qntd=$qtde');
      return Success('Impresso com sucesso.');
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

}