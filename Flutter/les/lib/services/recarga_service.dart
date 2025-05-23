
import 'package:dio/dio.dart';
import 'package:les/core/endpoints.dart';
import 'package:les/core/generic_service.dart';
import 'package:les/model/cliente/historico_recarga.dart';
import 'package:result_dart/result_dart.dart';

class RecargaService extends GenericService<HistoricoRecarga>{

  RecargaService(Dio dio) : super(dio, 'historicorecargas', HistoricoRecarga.fromJson);

  AsyncResult<List<HistoricoRecarga>> getHistoricoRecargaByClienteId(int clienteId) async {
    refreshToken();
    try {
      final response = await dio.get('${Endpoints.baseUrl}/$endpoint/cliente/$clienteId');
      return Success((response.data as List).map((e) => fromJson(e)).toList());
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }
}