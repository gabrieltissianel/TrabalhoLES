
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/fornecedor/fornecedor.dart';
import 'package:les/view/fornecedor/fornecedor_form_dialog.dart';
import 'package:les/view/fornecedor/view_model/fornecedor_view_model.dart';
import 'package:les/view/widgets/custom_table.dart';
import 'package:les/view/widgets/widget_com_permissao.dart';
import 'package:result_command/result_command.dart';

class FornecedorView extends StatefulWidget{
  const FornecedorView({super.key});

  @override
  State<StatefulWidget> createState() => _FornecedorViewState();
}

class _FornecedorViewState extends State<FornecedorView> {
  final FornecedorViewModel _fornecedorViewModel = injector.get<FornecedorViewModel>();

  @override
  void initState() {
    super.initState();
    _fornecedorViewModel.getFornecedores.execute();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          Expanded(
            child: Center(
              child: ListenableBuilder(
                  listenable: _fornecedorViewModel.getFornecedores,
                  builder: (context, child) {
                    if (_fornecedorViewModel.getFornecedores.isRunning) {
                      return CircularProgressIndicator();
                    } else if (_fornecedorViewModel.getFornecedores.isFailure) {
                      final error = _fornecedorViewModel.getFornecedores
                          .value as FailureCommand;
                      return Text(error.error.toString());
                    } else {
                      final success = _fornecedorViewModel.getFornecedores
                          .value as SuccessCommand;
                      final fornecedores = success.value as List<Fornecedor>;
                      return SizedBox.expand(
                            child: CustomTable(title: "Fornecedores",
                                data: fornecedores,
                                columnHeaders: ["id", "nome"],
                                getActions: (fornecedor) {
                                  return [
                                    WidgetComPermissao(
                                        permission: "/pagamento",
                                        child: IconButton(
                                            icon: Icon(Icons.shopping_bag),
                                            onPressed: () {
                                              context
                                                  .go('${AppRoutes.pagamentos}/${fornecedor.id}');
                                            })),
                                    WidgetComPermissao(
                                        permission: "/fornecedor",
                                        edit: true,
                                        child: IconButton(
                                            icon: Icon(Icons.edit),
                                            onPressed: () {
                                              showDialog(
                                                context: context,
                                                builder: (context) =>
                                                    FornecedorFormDialog(fornecedor: fornecedor),
                                              );
                                            })
                                    ),
                                    WidgetComPermissao(
                                        permission: "/fornecedor",
                                        delete: true,
                                        child: IconButton(
                                            icon: Icon(Icons.delete),
                                            onPressed: () async {
                                              await _fornecedorViewModel.deleteFornecedor
                                                  .execute(fornecedor.id!);
                                              _fornecedorViewModel.getFornecedores.execute();
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
      floatingActionButton: _buttons(_fornecedorViewModel, context),
    );
  }

  Widget _buttons(FornecedorViewModel fornecedorViewModel, BuildContext context){
    return Column(
      mainAxisAlignment: MainAxisAlignment.end,
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        WidgetComPermissao(
              permission: "/fornecedor",
              add: true,
              child: FloatingActionButton(
                onPressed: () {
                  showDialog(
                    context: context,
                    builder: (context) => FornecedorFormDialog(),
                  );
                },
                child: Icon(Icons.add),
              )
        )
        ]
    );
  }
}


