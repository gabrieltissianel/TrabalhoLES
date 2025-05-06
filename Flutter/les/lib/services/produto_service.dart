
import 'package:dio/dio.dart';
import 'package:les/core/generic_service.dart';
import 'package:les/model/produto/produto.dart';

class ProdutoService extends GenericService<Produto> {

  ProdutoService(Dio dio) : super(dio, 'produto', (data) => Produto.fromJson(data));

}