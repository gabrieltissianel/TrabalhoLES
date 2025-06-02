
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/cliente/historico_recarga.dart';
import 'package:les/view/cliente/view_model/historico_recarga_view_model.dart';
import 'package:les/view/widgets/custom_table.dart';
import 'package:result_command/result_command.dart';

class HistoricoRecargaView extends StatefulWidget{
  final int clienteId;

  const HistoricoRecargaView({super.key, required this.clienteId});

  @override
  State<StatefulWidget> createState() => _HistoricoRecargaViewState();
}

class _HistoricoRecargaViewState extends State<HistoricoRecargaView> {
  final HistoricoRecargaViewModel _historicoRecargaViewModel = injector.get<HistoricoRecargaViewModel>();

  @override
  void initState() {
    super.initState();
    _historicoRecargaViewModel.getHistoricoCliente.execute(widget.clienteId);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          Expanded(
              child: Center(
                  child: ListenableBuilder(
                      listenable: _historicoRecargaViewModel.getHistoricoCliente,
                      builder: (context, child) {
                        if (_historicoRecargaViewModel.getHistoricoCliente.isRunning) {
                          return CircularProgressIndicator();
                        } else if (_historicoRecargaViewModel.getHistoricoCliente.isFailure) {
                          final error = _historicoRecargaViewModel.getHistoricoCliente
                              .value as FailureCommand;
                          return Text(error.error.toString());
                        } else {
                          final success = _historicoRecargaViewModel.getHistoricoCliente
                              .value as SuccessCommand;
                          final historicoRecarga = success.value as List<HistoricoRecarga>;
                          return SizedBox.expand(
                              child: CustomTable(title: "Historico Recargas",
                                data: historicoRecarga,
                                columnHeaders: ["data", "valor"],
                                formatters: (historico) => {
                                  "data": (value) =>
                                      DateFormat('dd/MM/yyyy').format(DateTime.parse(value)).toString(),
                                  "valor": (value) => _formatCurrency(value),
                                },
                              )
                          );
                        }
                      })
              )
          ),
        ],
      ),
    );
  }

  String _formatCurrency(double value) {
    return NumberFormat.currency(
      locale: 'pt_BR',
      symbol: 'R\$',
    ).format(value);
  }

}


