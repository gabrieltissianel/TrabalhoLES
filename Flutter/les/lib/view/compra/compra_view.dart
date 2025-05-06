
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/cliente/cliente.dart';
import 'package:les/model/compra/compra.dart';
import 'package:les/view/cliente/view_model/cliente_view_model.dart';
import 'package:les/view/compra/view_model/compra_view_model.dart';
import 'package:les/view/widgets/custom_table.dart';
import 'package:les/view/widgets/widget_com_permissao.dart';
import 'package:result_command/result_command.dart';

class CompraView extends StatefulWidget{
  const CompraView({super.key});

  @override
  State<StatefulWidget> createState() => _CompraViewState();
}

class _CompraViewState extends State<CompraView> {
  final CompraViewModel _compraViewModel = injector.get<CompraViewModel>();
  final ClienteViewModel _clienteViewModel = injector.get<ClienteViewModel>();

  @override
  void initState() {
    super.initState();
    _compraViewModel.getCompras.execute();
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
                      listenable: _compraViewModel.getCompras,
                      builder: (context, child) {
                        if (_compraViewModel.getCompras.isRunning) {
                          return CircularProgressIndicator();
                        } else if (_compraViewModel.getCompras.isFailure) {
                          final error = _compraViewModel.getCompras
                              .value as FailureCommand;
                          return Text(error.error.toString());
                        } else {
                          final success = _compraViewModel.getCompras
                              .value as SuccessCommand;
                          var compras = success.value as List<Compra>;
                          compras.sort( (a, b) {
                            DateTime? sA = a.saida;
                            DateTime? sB = b.saida;
                            if (sA == null && sB == null) return 0;
                            if (sA == null) return -1;
                            if (sB == null) return 1;
                            return sB.compareTo(sA);
                          });
                          return SizedBox.expand(
                              child: CustomTable<Compra>(title: "Compras",
                                  data: compras,
                                  columnHeaders: ["cliente", "entrada", "saida"],
                                  formatters: (compra) => {
                                    "cliente" : (cliente) {
                                      Cliente c = Cliente.fromJson(cliente);
                                      return c.nome;
                                    },
                                    "entrada" : (entrada) => DateFormat('dd/MM/yyyy HH:mm').format(DateTime.parse(entrada)).toString(),
                                    "saida" : (saida) {
                                      if (saida != null) {
                                        return DateFormat('HH:mm').format(DateTime.parse(saida)).toString();
                                      }
                                      return "Ainda não saiu.";
                                    }
                                  },
                                  getActions: (compra) {
                                    return [
                                      WidgetComPermissao(
                                          permission: "/compra",
                                          edit: true,
                                          child: IconButton(
                                              onPressed: () {
                                                context.go('${AppRoutes.compras}/${compra.id}');
                                              },
                                              icon: compra.saida == null
                                                  ? Icon(Icons.shopping_cart_outlined)
                                                  : Icon(Icons.remove_red_eye))
                                      ),
                                      WidgetComPermissao(
                                          permission: "/compra",
                                          delete: true,
                                          child: IconButton(
                                              onPressed: () async {
                                                await _compraViewModel.deleteCompra.execute(compra.id!);
                                                _compraViewModel.getCompras.execute();
                                              },
                                              icon: Icon(Icons.delete))
                                      )
                                    ];
                                  },

                                  )

                          );
                        }
                      })
              )
          ),
        ],
      ),
      floatingActionButton: _buttons(_compraViewModel, context),
    );
  }

  Widget _buttons(CompraViewModel compraViewModel, BuildContext context){
    return Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          WidgetComPermissao(
              permission: "/compra",
              add: true,
              child: FloatingActionButton(
                onPressed: () {
                  showDialog(
                      context: context,
                      builder: (context) => _dialog()
                  );
                },
                child: Icon(Icons.add),
              )
          )
        ]
    );
  }

  _dialog() {
    return AlertDialog(
      content: ListenableBuilder(
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
                      formatters: (cliente) => {
                        "saldo": (value) => "R\$ $value",
                        "limite": (value) => "R\$ $value",
                        "dt_nascimento": (value) =>
                            DateFormat('dd/MM/yyyy').format(DateTime.parse(value)).toString()
                      },
                      getActions: (cliente) {
                        return [
                          IconButton(
                              onPressed: () async {
                                Compra compra = Compra(
                                    cliente: cliente,
                                    entrada: DateTime.now(),
                                    compraProdutos: []
                                );
                                await _compraViewModel.addCompra.execute(
                                    compra);
                                _compraViewModel.getCompras.execute();
                                Navigator.pop(context);
                              },
                              icon: Icon(Icons.shopping_cart))
                        ];
                      })

              );
            }
          }),
    );
  }
}

