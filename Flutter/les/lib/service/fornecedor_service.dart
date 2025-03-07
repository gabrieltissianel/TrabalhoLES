
import 'package:dio/dio.dart';
import 'package:les/core/api_service.dart';
import 'package:les/model/fornecedor.dart';

class FornecedorService extends ApiService<Fornecedor>{

  FornecedorService(Dio dio)
    : super (dio, 'fornecedor', (json) => Fornecedor.fromJson(json));

}