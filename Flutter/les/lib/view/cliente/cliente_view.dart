
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/cliente/cliente.dart';
import 'package:les/view/cliente/cliente_form_dialog.dart';
import 'package:les/view/cliente/recarga_dialog.dart';
import 'package:les/view/cliente/view_model/cliente_view_model.dart';
import 'package:les/view/widgets/custom_table.dart';
import 'package:les/view/widgets/widget_com_permissao.dart';
import 'package:result_command/result_command.dart';

class ClienteView extends StatefulWidget{
  const ClienteView({super.key});

  @override
  State<StatefulWidget> createState() => _ClienteViewState();
}

class _ClienteViewState extends State<ClienteView> {
  final ClienteViewModel _clienteViewModel = injector.get<ClienteViewModel>();

  @override
  void initState() {
    super.initState();
    _clienteViewModel.getClientes.execute();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          Expanded(
              child: Center(
                  child: ListenableBuilder(
                      listenable: _clienteViewModel.getClientes,
                      builder: (context, child) {
                        if (_clienteViewModel.getClientes.isRunning) {
                          return CircularProgressIndicator();
                        } else if (_clienteViewModel.getClientes.isFailure) {
                          final error = _clienteViewModel.getClientes
                              .value as FailureCommand;
                          return Text(error.error.toString());
                        } else {
                          final success = _clienteViewModel.getClientes
                              .value as SuccessCommand;
                          final clientes = success.value as List<Cliente>;
                          return SizedBox.expand(
                              child: CustomTable(title: "Clientes",
                                  data: clientes,
                                  columnHeaders: ["id", "nome", "limite", "saldo", "dt_nascimento"],
                                  formatters: {
                                    "saldo": (value) => "R\$ $value",
                                    "limite": (value) => "R\$ $value",
                                    "dt_nascimento": (value) =>
                                      DateFormat('dd/MM/yyyy').format(DateTime.parse(value)).toString()
                                  },
                                  getActions: (cliente) {
                                    return [
                                      WidgetComPermissao(
                                          permission: "/cliente",
                                          edit: true,
                                          child: IconButton(
                                              onPressed: () {
                                                showDialog(
                                                    context: context,
                                                    builder: (context) =>
                                                    RecargaDialog(cliente: cliente)
                                                );
                                              },
                                              icon: Icon(Icons.add_card))
                                      ),
                                      WidgetComPermissao(
                                          permission: "/cliente",
                                          edit: true,
                                          child: IconButton(
                                              icon: Icon(Icons.edit),
                                              onPressed: () {
                                                showDialog(
                                                  context: context,
                                                  builder: (context) =>
                                                      ClienteFormDialog(cliente: cliente),
                                                );
                                              })
                                      ),
                                      WidgetComPermissao(
                                          permission: "/cliente",
                                          delete: true,
                                          child: IconButton(
                                              icon: Icon(Icons.delete),
                                              onPressed: () async {
                                                await _clienteViewModel.deleteCliente
                                                    .execute(cliente.id!);
                                                _clienteViewModel.getClientes.execute();
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
      floatingActionButton: _buttons(_clienteViewModel, context),
    );
  }

  Widget _buttons(ClienteViewModel clienteViewModel, BuildContext context){
    return Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          WidgetComPermissao(
              permission: "/cliente",
              add: true,
              child: FloatingActionButton(
                onPressed: () {
                  showDialog(
                    context: context,
                    builder: (context) => ClienteFormDialog(),
                  );
                },
                child: Icon(Icons.add),
              )
          )
        ]
    );
  }
}


