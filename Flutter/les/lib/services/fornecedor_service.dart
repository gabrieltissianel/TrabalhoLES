
import 'package:dio/dio.dart';
import 'package:les/core/generic_service.dart';
import 'package:les/model/fornecedor.dart';

class FornecedorService extends GenericService<Fornecedor> {

  FornecedorService(Dio dio) : super(dio, 'fornecedor', (data) => Fornecedor.fromJson(data));

}