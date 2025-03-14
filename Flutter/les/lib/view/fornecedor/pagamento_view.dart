
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/pagamento.dart';
import 'package:les/view/fornecedor/view_model/pagamento_view_model.dart';
import 'package:result_command/result_command.dart';

class PagamentoView extends StatefulWidget{
  const PagamentoView({super.key});

  @override
  State<StatefulWidget> createState() => _PagamentoViewState();
}

class _PagamentoViewState extends State<PagamentoView> {
  final PagamentoViewModel _pagamentoViewModel = injector.get<PagamentoViewModel>();

  @override
  void initState() {
    super.initState();
    _pagamentoViewModel.getPagamentos.execute();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          Expanded(
              child: Center(
                  child: ListenableBuilder(
                      listenable: _pagamentoViewModel.getPagamentos,
                      builder: (context, child) {
                        if (_pagamentoViewModel.getPagamentos.isRunning) {
                          return CircularProgressIndicator();
                        } else if (_pagamentoViewModel.getPagamentos.isFailure) {
                          final error = _pagamentoViewModel.getPagamentos
                              .value as FailureCommand;
                          return Text(error.error.toString());
                        } else {
                          final success = _pagamentoViewModel.getPagamentos
                              .value as SuccessCommand;
                          final pagamentos = success.value as List<Pagamento>;
                          return SizedBox.expand(
                            child: SingleChildScrollView(
                              scrollDirection: Axis.vertical,
                              child: _table(pagamentos),
                            ),
                          );
                        }
                      })
              )
          ),
        ],
      ),
    );
  }

  Widget _table(List<Pagamento> pagamentos){
    return DataTable(
        columns: [
          DataColumn(label: Text("ID", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
          DataColumn(label: Text("Fornecedor", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
          DataColumn(label: Text("Valor", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
          DataColumn(label: Text("Data Vencimento", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
          DataColumn(label: Text("Data Pagamento", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
          DataColumn(label: Text("Açoes", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
        ],
        rows: pagamentos.map((pagamento) {
          return DataRow(cells: [
            DataCell(Text(pagamento.id.toString())),
            DataCell(Text(pagamento.fornecedor.nome)),
            DataCell(Text(NumberFormat.currency(
              locale: 'pt_BR',
              symbol: 'R\$',
              decimalDigits: 2,
            ).format(pagamento.valor))),
            DataCell(Text(DateFormat('dd/MM/yyyy').format(pagamento.dt_vencimento))),
            DataCell(Text(pagamento.dt_pagamento != null ? DateFormat('dd/MM/yyyy').format(pagamento.dt_pagamento!): 'Nao Pago')),
            DataCell(Row(
              children: [
                IconButton(
                    icon: Icon(Icons.delete),
                    tooltip: 'Deletar',
                    onPressed: () async {
                      await _pagamentoViewModel.deletePagamento
                          .execute(pagamento.id!);
                      _pagamentoViewModel.getPagamentos.execute();
                    }),
                if (pagamento.dt_pagamento == null)
                IconButton(
                    icon: Icon(Icons.check),
                    tooltip: 'Pagar',
                    onPressed: () async {
                      pagamento.dt_pagamento = DateTime.now();
                      await _pagamentoViewModel.updatePagamento
                          .execute(pagamento);
                      _pagamentoViewModel.getPagamentos.execute();
                    }),
              ],
            )),
          ]);
        }).toList());
  }
}