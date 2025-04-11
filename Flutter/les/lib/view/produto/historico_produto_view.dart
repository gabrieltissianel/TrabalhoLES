
import 'package:flutter/material.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/produto/historico_produto.dart';
import 'package:les/view/produto/view_model/historico_produto_view_model.dart';
import 'package:les/view/widgets/custom_table.dart';
import 'package:result_command/result_command.dart';

class HistoricoProdutoView extends StatefulWidget{
  const HistoricoProdutoView({super.key});

  @override
  State<StatefulWidget> createState() => _HistoricoProdutoViewState();
}

class _HistoricoProdutoViewState extends State<HistoricoProdutoView> {
  final HistoricoProdutoViewModel _historicoHistoricoProdutoViewModel = injector.get<HistoricoProdutoViewModel>();

  @override
  void initState() {
    super.initState();
    _historicoHistoricoProdutoViewModel.getHistoricoProdutos.execute();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          Expanded(
              child: Center(
                  child: ListenableBuilder(
                      listenable: _historicoHistoricoProdutoViewModel.getHistoricoProdutos,
                      builder: (context, child) {
                        if (_historicoHistoricoProdutoViewModel.getHistoricoProdutos.isRunning) {
                          return CircularProgressIndicator();
                        } else if (_historicoHistoricoProdutoViewModel.getHistoricoProdutos.isFailure) {
                          final error = _historicoHistoricoProdutoViewModel.getHistoricoProdutos
                              .value as FailureCommand;
                          return Text(error.error.toString());
                        } else {
                          final success = _historicoHistoricoProdutoViewModel.getHistoricoProdutos
                              .value as SuccessCommand;
                          final historicoHistoricoProdutos = success.value as List<HistoricoProduto>;
                          return SizedBox.expand(
                              child: CustomTable(title: "HistoricoProdutos",
                                  data: historicoHistoricoProdutos,
                                  columnHeaders: ["preco_novo", "custo_novo", "data"]
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


