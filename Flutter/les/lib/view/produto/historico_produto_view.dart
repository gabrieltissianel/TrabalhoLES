
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/produto/historico_produto.dart';
import 'package:les/view/produto/view_model/historico_produto_view_model.dart';
import 'package:les/view/widgets/custom_table.dart';
import 'package:result_command/result_command.dart';

class HistoricoProdutoView extends StatefulWidget{
  final int produtoId;
  
  const HistoricoProdutoView({super.key, required this.produtoId});

  @override
  State<StatefulWidget> createState() => _HistoricoProdutoViewState();
}

class _HistoricoProdutoViewState extends State<HistoricoProdutoView> {
  final HistoricoProdutoViewModel _historicoHistoricoProdutoViewModel = injector.get<HistoricoProdutoViewModel>();

  @override
  void initState() {
    super.initState();
    _historicoHistoricoProdutoViewModel.getByClienteId.execute(widget.produtoId);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          Expanded(
              child: Center(
                  child: ListenableBuilder(
                      listenable: _historicoHistoricoProdutoViewModel.getByClienteId,
                      builder: (context, child) {
                        if (_historicoHistoricoProdutoViewModel.getByClienteId.isRunning) {
                          return CircularProgressIndicator();
                        } else if (_historicoHistoricoProdutoViewModel.getByClienteId.isFailure) {
                          final error = _historicoHistoricoProdutoViewModel.getByClienteId
                              .value as FailureCommand;
                          return Text(error.error.toString());
                        } else {
                          final success = _historicoHistoricoProdutoViewModel.getByClienteId
                              .value as SuccessCommand;
                          final historicoHistoricoProdutos = success.value as List<HistoricoProduto>;
                          return SizedBox.expand(
                              child: CustomTable(title: "Historico Produtos",
                                  data: historicoHistoricoProdutos,
                                  columnHeaders: ["preco_novo", "custo_novo", "data"],
                                  formatters: {
                                    "data": (value) =>
                                        DateFormat('dd/MM/yyyy').format(DateTime.parse(value)).toString()
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
  
}


