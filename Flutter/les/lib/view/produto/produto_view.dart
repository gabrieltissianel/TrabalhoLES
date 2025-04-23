
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/produto/produto.dart';
import 'package:les/view/produto/produto_form_dialog.dart';
import 'package:les/view/produto/view_model/produto_view_model.dart';
import 'package:les/view/widgets/custom_table.dart';
import 'package:les/view/widgets/widget_com_permissao.dart';
import 'package:result_command/result_command.dart';

class ProdutoView extends StatefulWidget{
  const ProdutoView({super.key});

  @override
  State<StatefulWidget> createState() => _ProdutoViewState();
}

class _ProdutoViewState extends State<ProdutoView> {
  final ProdutoViewModel _produtoViewModel = injector.get<ProdutoViewModel>();

  @override
  void initState() {
    super.initState();
    _produtoViewModel.getProdutos.execute();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          Expanded(
              child: Center(
                  child: ListenableBuilder(
                      listenable: _produtoViewModel.getProdutos,
                      builder: (context, child) {
                        if (_produtoViewModel.getProdutos.isRunning) {
                          return CircularProgressIndicator();
                        } else if (_produtoViewModel.getProdutos.isFailure) {
                          final error = _produtoViewModel.getProdutos
                              .value as FailureCommand;
                          return Text(error.error.toString());
                        } else {
                          final success = _produtoViewModel.getProdutos
                              .value as SuccessCommand;
                          final produtos = success.value as List<Produto>;
                          return SizedBox.expand(
                              child: CustomTable(title: "Produtos",
                                  data: produtos,
                                  columnHeaders: ["id", "codigo", "nome", "preco", "custo", "unitario"],
                                  formatters: {
                                    "preco": (value) => "R\$ $value",
                                    "custo": (value) => "R\$ $value",
                                    "unitario": (value) => value.toString() == 'true' ? "Sim" : "Nao"
                                  },
                                  getActions: (produto) {
                                    return [
                                      WidgetComPermissao(
                                          permission: "/produto",
                                          edit: true,
                                          child: IconButton(
                                              onPressed: () {
                                                context
                                                    .go('${AppRoutes.historicoProduto}/${produto.id}');
                                              },
                                              icon: Icon(Icons.history))
                                      ),
                                      WidgetComPermissao(
                                          permission: "/produto",
                                          edit: true,
                                          child: IconButton(
                                              icon: Icon(Icons.edit),
                                              onPressed: () {
                                                showDialog(
                                                  context: context,
                                                  builder: (context) =>
                                                      ProdutoFormDialog(produto: produto),
                                                );
                                              })
                                      ),
                                      WidgetComPermissao(
                                          permission: "/produto",
                                          delete: true,
                                          child: IconButton(
                                              icon: Icon(Icons.delete),
                                              onPressed: () async {
                                                await _produtoViewModel.deleteProduto
                                                    .execute(produto.id!);
                                                _produtoViewModel.getProdutos.execute();
                                              })
                                      )

                                    ];
                                  })

                          );
                        }
                      })
              )
          ),
        ],
      ),
      floatingActionButton: _buttons(_produtoViewModel, context),
    );
  }

  Widget _buttons(ProdutoViewModel produtoViewModel, BuildContext context){
    return Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          WidgetComPermissao(
              permission: "/produto",
              add: true,
              child: FloatingActionButton(
                onPressed: () {
                  showDialog(
                    context: context,
                    builder: (context) => ProdutoFormDialog(),
                  );
                },
                child: Icon(Icons.add),
              )
          )
        ]
    );
  }
}


