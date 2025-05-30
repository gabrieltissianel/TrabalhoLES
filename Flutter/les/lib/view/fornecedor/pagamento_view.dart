
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/fornecedor/fornecedor.dart';
import 'package:les/model/fornecedor/pagamento.dart';
import 'package:les/view/fornecedor/pagamento_form_dialog.dart';
import 'package:les/view/fornecedor/view_model/fornecedor_view_model.dart';
import 'package:les/view/fornecedor/view_model/pagamento_view_model.dart';
import 'package:les/view/widgets/custom_table.dart';
import 'package:les/view/widgets/widget_com_permissao.dart';
import 'package:result_command/result_command.dart';

class PagamentoView extends StatefulWidget{
  final int fornecedorId;

  const PagamentoView({required this.fornecedorId, super.key});

  @override
  State<StatefulWidget> createState() => _PagamentoViewState();
}

class _PagamentoViewState extends State<PagamentoView> {
  final PagamentoViewModel _pagamentoViewModel = injector.get<PagamentoViewModel>();
  final FornecedorViewModel _fornecedorViewModel = injector.get<FornecedorViewModel>();

  @override
  void initState() {
    super.initState();
    _pagamentoViewModel.getPagamentoByFornecedorId.execute(widget.fornecedorId);
    _fornecedorViewModel.getFornecedorById.execute(widget.fornecedorId);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          Expanded(
              child: Center(
                  child: ListenableBuilder(
                      listenable: _pagamentoViewModel.getPagamentoByFornecedorId,
                      builder: (context, child) {
                        if (_pagamentoViewModel.getPagamentoByFornecedorId.isRunning) {
                          return CircularProgressIndicator();
                        } else if (_pagamentoViewModel.getPagamentoByFornecedorId.isFailure) {
                          final error = _pagamentoViewModel.getPagamentoByFornecedorId
                              .value as FailureCommand;
                          return Text(error.error.toString());
                        } else {
                          final success = _pagamentoViewModel.getPagamentoByFornecedorId
                              .value as SuccessCommand;
                          final pagamentos = success.value as List<Pagamento>;
                          return SizedBox.expand(
                              child: CustomTable<Pagamento>(
                            title: "Pagamentos",
                            data: pagamentos,
                            columnHeaders: [
                              "id",
                              "fornecedor",
                              "valor",
                              "dt_vencimento",
                              "dt_pagamento"
                            ],
                            formatters: (pagamento) => {
                              "fornecedor": (p) {
                                return pagamento.fornecedor.nome;
                              },
                              "valor": NumberFormat.currency(
                                locale: 'pt_BR',
                                symbol: 'R\$',
                                decimalDigits: 2,
                              ).format,
                              "dt_vencimento": (p) => DateFormat('dd/MM/yyyy').format(pagamento.dt_vencimento),
                              "dt_pagamento": (p) => pagamento.dt_pagamento != null ? DateFormat('dd/MM/yyyy').format(pagamento.dt_pagamento!) : 'Nao Pago'
                            },
                            getActions: (pagamento) {
                              return [
                                WidgetComPermissao(
                                  permission: "/pagamento",
                                  delete: true,
                                  child: IconButton(
                                      icon: Icon(Icons.delete),
                                      tooltip: 'Deletar',
                                      onPressed: () async {
                                        await _pagamentoViewModel
                                            .deletePagamento
                                            .execute(pagamento.id!);
                                        _pagamentoViewModel.getPagamentos
                                            .execute();
                                      }),
                                ),
                                if (pagamento.dt_pagamento == null)
                                  WidgetComPermissao(
                                    permission: "/pagamento",
                                    edit: true,
                                    child: IconButton(
                                        icon: Icon(Icons.check),
                                        tooltip: 'Pagar',
                                        onPressed: () async {
                                          _selecionarData(context, pagamento);
                                        }),
                                  )
                              ];
                            },
                          ));
                        }
                      })
              )
          ),
        ],
      ),
      floatingActionButton: _buttons(context),
    );
  }

  Widget _buttons(BuildContext context){
    return Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          FloatingActionButton(
            onPressed: () async {
              if(_fornecedorViewModel.getFornecedorById.isSuccess){
                final success = _fornecedorViewModel.getFornecedorById
                    .value as SuccessCommand;
                final fornecedor = success.value as Fornecedor;
                showDialog(
                    context: context,
                    builder: (context) => PagamentoFormDialog(fornecedor: fornecedor)
                );
              }
            },
            child: Icon(Icons.add),
          ),
        ]);
  }

  Future<void> _selecionarData(BuildContext context, Pagamento pagamento) async {
    DateTime? dataSelecionada = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(2000),
      lastDate: DateTime.now(),
    );

    if (dataSelecionada != null) {
      pagamento.dt_pagamento = dataSelecionada;
      await _pagamentoViewModel.updatePagamento.execute(pagamento);
      _pagamentoViewModel.getPagamentos.execute();
    }
  }

}