
import 'package:dio/dio.dart';
import 'package:les/core/generic_service.dart';
import 'package:les/model/pagamento.dart';

class PagamentoService extends GenericService<Pagamento>{

  PagamentoService(Dio dio): super(dio, 'pagamento', (data) => Pagamento.fromJson(data));
}