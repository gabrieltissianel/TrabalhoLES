
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/fornecedor.dart';
import 'package:les/view/fornecedor/fornecedor_form_dialog.dart';
import 'package:les/view/fornecedor/pagamento_form_dialog.dart';
import 'package:les/view/fornecedor/view_model/fornecedor_view_model.dart';
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
                            child: SingleChildScrollView(
                              scrollDirection: Axis.vertical,
                              child: _table(fornecedores),
                            ),
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

  Widget _table(List<Fornecedor> fornecedores){
    return DataTable(
        columns: [
          DataColumn(label: Text("ID", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
          DataColumn(label: Text("Nome", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
          DataColumn(label: Text("Açoes", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
        ],
        rows: fornecedores.map((fornecedor) {
          return DataRow(cells: [
            DataCell(Text(fornecedor.id.toString())),
            DataCell(Text(fornecedor.nome)),
            DataCell(Row(
              children: [
                IconButton(
                    icon: Icon(Icons.shopping_bag),
                    onPressed: () {
                      showDialog(
                        context: context,
                        builder: (context) =>
                            PagamentoFormDialog(fornecedor: fornecedor),
                      );
                    }),
                IconButton(
                    icon: Icon(Icons.edit),
                    onPressed: () {
                      showDialog(
                        context: context,
                        builder: (context) =>
                            FornecedorFormDialog(fornecedor: fornecedor),
                      );
                    }),
                IconButton(
                    icon: Icon(Icons.delete),
                    onPressed: () async {
                      await _fornecedorViewModel.deleteFornecedor
                          .execute(fornecedor.id!);
                      _fornecedorViewModel.getFornecedores.execute();
                    })
              ],
            )),
          ]);
        }).toList());
  }

  Widget _buttons(FornecedorViewModel fornecedorViewModel, BuildContext context){
    return Column(
      mainAxisAlignment: MainAxisAlignment.end,
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        FloatingActionButton(
          onPressed: () {
            showDialog(
              context: context,
              builder: (context) => FornecedorFormDialog(),
            );
          },
          child: Icon(Icons.add),
        ),
        SizedBox(height: 16),
        FloatingActionButton(
          onPressed: () {
            context.go(AppRoutes.pagamentos);
          },
          child: Icon(Icons.shopping_cart),
        ),
      ]
    );
  }
}


