
import 'package:les/model/cliente/historico_recarga.dart';
import 'package:les/services/recarga_service.dart';
import 'package:result_command/result_command.dart';
import 'package:result_dart/result_dart.dart';

class HistoricoRecargaViewModel {
  final RecargaService _recargaService;

  HistoricoRecargaViewModel(this._recargaService);

  late final getHistoricoCliente = Command1(_getHistoricoRecargaByClienteId);

  AsyncResult<List<HistoricoRecarga>> _getHistoricoRecargaByClienteId(int clienteId) {
    return _recargaService.getHistoricoRecargaByClienteId(clienteId);

  }
}