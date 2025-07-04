import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/view/cliente/view_model/cliente_view_model.dart';
import 'package:les/view/widgets/qntd_dialog.dart';
import 'package:result_command/result_command.dart';

import '../../core/injector.dart';
import '../../model/cliente/cliente.dart';
import '../../services/relatorios_service.dart';
import '../widgets/custom_table.dart';

class RelatoriosView extends StatelessWidget {
  const RelatoriosView({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Relatorios")),
      body: _body(context),
    );
  }

  _body(BuildContext context) {
    final service = injector.get<RelatoriosService>();

    final Map<String, Function(BuildContext)> buttons = {
      '🎂 Aniversariantes': (context) {
        QuantityEditDialog(
          context: context,
          title: 'Selecione o mês:',
          minQuantity: 1,
          maxQuantity: 12,
          onSave: (value) => service.aniversariantes(value),
          initialQuantity: DateTime.now().month,
        ).show();
      },
      'Clientes Negativados': (context) => service.clientesDevedores(),
      'Compras de Hoje': (context) => service.consumoDiarioHoje(),
      'Ultimas Compras dos Clientes': (context) => service.ultimasVendas(),
      'Compra de Produtos': (context) =>
          selecionarDuasDatas(context, service.vendaProdutos),
      'Ticket Médio': (context) =>
          selecionarDuasDatas(context, service.ticketMedio),
      'Compras por Data': (context) =>
          selecionarUmaDatas(context, service.consumoData),
      'Ultima Compra de um Cliente': (context) {
        showDialog(
          context: context,
          builder: (context) => _dialogCliente(service.ultimaCompraCliente),
        );
      },
      'DRE Diario': (context) =>
          selecionarDuasDatas(context, service.dreDiario),
      'Grafico Consumo': (context) => context.go(AppRoutes.graficoConsumo),
    };

    return GridView.count(
      crossAxisCount: 4,
      // Número de colunas
      childAspectRatio: 1.75,
      // Proporção dos itens (largura/altura)
      padding: EdgeInsets.all(8.0),
      mainAxisSpacing: 16.0,
      // Espaçamento vertical entre os itens
      crossAxisSpacing: 16.0,
      // Espaçamento horizontal entre os itens
      children: buttons.entries.map((entry) {
        final text = entry.key;
        final onPressed = entry.value;
        return SizedBox(
          width: double.infinity, // Largura total
          height: 50, // Altura fixa
          child: ElevatedButton(
            onPressed: () => onPressed(context),
            style: ElevatedButton.styleFrom(
              backgroundColor: Color.fromARGB(255, 205, 231, 236),
              foregroundColor: Colors.grey,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(15),
              ),
            ),
            child: Text(
              text,
              style: TextStyle(
                color: Colors.black45,
                fontSize: 20,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
        );
      }).toList(),
    );
  }

  void selecionarDuasDatas(
    BuildContext context,
    Function(DateTime, DateTime) function,
  ) async {
    final DateTime? dataInicio = await showDatePicker(
      context: context, // Precisa d
      helpText: "Data Inicial", // o BuildContext
      firstDate: DateTime(2020),
      lastDate: DateTime(2050),
      initialDate: DateTime.now(),
    );
    if (dataInicio == null) {
      return; // Se o usuário cancelar a seleção, saia da função
    }
    final DateTime? dataFim = await showDatePicker(
      context: context,
      helpText: "Data Final",
      firstDate: dataInicio, // Não permitir data final antes da inicial
      lastDate: DateTime(2050),
      initialDate: dataInicio,
    );
    if (dataFim == null) return;

    function(dataInicio, dataFim);
  }

  void selecionarUmaDatas(
    BuildContext context,
    Function(DateTime) function,
  ) async {
    final DateTime? data = await showDatePicker(
      context: context,
      firstDate: DateTime(2020),
      lastDate: DateTime.now(),
      initialDate: DateTime.now(),
    );
    if (data == null) return;
    function(data);
  }

  _dialogCliente(Function(int id) acao) {
    var clienteViewModel = injector.get<ClienteViewModel>();
    clienteViewModel.getClientes.execute();
    return AlertDialog(
      content: ListenableBuilder(
        listenable: clienteViewModel.getClientes,
        builder: (context, child) {
          if (clienteViewModel.getClientes.isRunning) {
            return CircularProgressIndicator();
          } else if (clienteViewModel.getClientes.isFailure) {
            final error = clienteViewModel.getClientes.value as FailureCommand;
            return Text(error.error.toString());
          } else {
            final success =
                clienteViewModel.getClientes.value as SuccessCommand;
            final clientes = success.value as List<Cliente>;
            return SizedBox.expand(
              child: CustomTable(
                title: "Clientes",
                data: clientes,
                columnHeaders: ["nome", "limite", "saldo"],
                formatters: (cliente) => {
                  "saldo": (value) => "R\$ $value",
                  "limite": (value) => "R\$ $value",
                },
                getActions: (cliente) {
                  return [
                    IconButton(
                      onPressed: () {
                        acao(cliente.id!);
                        Navigator.pop(context);
                      },
                      icon: Icon(Icons.search),
                    ),
                  ];
                },
              ),
            );
          }
        },
      ),
    );
  }
}
