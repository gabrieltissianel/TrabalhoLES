
import 'package:dio/dio.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/endpoints.dart';
import 'package:les/model/compra/compra_produto.dart';
import 'package:result_dart/result_dart.dart';

class CompraProdutoService {
  final Dio dio;

  CompraProdutoService(this.dio);

  void refreshToken() {
    String token = userProvider.user!.token!;
    dio.options.headers['Authorization'] = 'Bearer $token';
  }

  AsyncResult<CompraProduto> addProduto(int idCompra, int idProduto, {double? qtde}) async {
    try{
      Map<String, dynamic> key = {
        'id': {
          'idcompra': idCompra,
          'idproduto': idProduto
        },
        "qntd":qtde
      };
      final response = await dio.post('${Endpoints.baseUrl}/compraproduto/add', data: key);
      return Success(CompraProduto.fromJson(response.data));
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

  AsyncResult<String> removeProduto(int idCompra, int idProduto) async {
    try{
      Map<String, int> key = {
          'idcompra': idCompra,
          'idproduto': idProduto
      };
      final response = await dio.delete('${Endpoints.baseUrl}/compraproduto/del', data: key);
      return Success(response.data);
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }
}