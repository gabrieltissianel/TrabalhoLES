
import 'dart:async';

import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/compra/compra.dart';
import 'package:les/model/compra/compra_produto.dart';
import 'package:les/model/produto/produto.dart';
import 'package:les/view/compra/view_model/compra_produto_view_model.dart';
import 'package:les/view/widgets/custom_table.dart';
import 'package:result_command/result_command.dart';

class CompraProdutoView extends StatefulWidget {
  final int compraId;

  const CompraProdutoView({super.key, required this.compraId});

  @override
  State<StatefulWidget> createState() => _CompraProdutoViewState();
}

class _CompraProdutoViewState extends State<CompraProdutoView> {
  final _compraProdutoViewModel = injector.get<CompraProdutoViewModel>();
  late Timer _timer;
  double? _peso;

  @override
  void initState() {
    super.initState();
    _compraProdutoViewModel.getCompra.execute(widget.compraId);
    _compraProdutoViewModel.getProdutos.execute();
    _compraProdutoViewModel.getPeso.execute();
    //_timer = Timer.periodic(Duration(milliseconds: 500), (timer) => _compraProdutoViewModel.getPeso.execute());
  }

  @override
  void dispose() {
    _timer.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: _appBar(),
      body: _body(),
    );
  }

  _body() {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Parte superior - Tabela
          Expanded(
            flex: 4,
            child: _table(),
          ),

          // Espaçamento
          const SizedBox(height: 16), // Alterado de width para height

          // Parte inferior - Formulário (apenas se compra não finalizada)
            Expanded(
              flex: 1,
              child: Card(
                elevation: 4,
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: _form(), // Removida a Column extra
                ),
              ),
            ),
        ],
      ),
    );
  }

  _form() {
    return Column(
        children: [
          ListenableBuilder(
              listenable: _compraProdutoViewModel.getCompra,
              builder: (context, child) {
                if (_compraProdutoViewModel.getCompra.isRunning) {
                  return Center(child: CircularProgressIndicator());
                }
                else if (_compraProdutoViewModel.getCompra.isFailure) {
                  return Text("Error.", style: TextStyle(color: Colors.red),);
                }
                else {
                  final success = _compraProdutoViewModel.getCompra
                      .value as SuccessCommand;
                  var compra = success.value as Compra;
                  return Text("Total: ${_formatCurrency(_compraProdutoViewModel.total(compra))}",
                      style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)
                  );
                }
              }),

          const SizedBox(height: 16), // Espaçamento

          Row(
            children: [
              Expanded(
                flex: 2,
                child:
                TextField(
                  controller: _compraProdutoViewModel.pesquisarController,
                  autofocus: true,
                  focusNode: _compraProdutoViewModel.pesquisarFocusNode,
                  decoration: const InputDecoration(
                    labelText: 'Pesquisar Produto',
                    border: OutlineInputBorder(),
                  ),
                  onSubmitted: (value) async {
                    await _compraProdutoViewModel.pesquisar.execute();
                    if (_compraProdutoViewModel.pesquisar.isFailure){
                      final res = _compraProdutoViewModel.pesquisar.value as FailureCommand;
                      showError(res.error.toString());
                    }
                    _compraProdutoViewModel.pesquisarFocusNode.requestFocus();
                    _compraProdutoViewModel.getCompra.execute(widget.compraId);
                  },
                ),
              ),
              const SizedBox(width: 16),
          Expanded(
              flex: 1,
              child:
              ListenableBuilder(
                  listenable: _compraProdutoViewModel.getPeso,
                  builder: (context, child) {
                    if (_compraProdutoViewModel.getPeso.isRunning) {
                      if (_peso == null) {
                        return Center(child: CircularProgressIndicator());
                      } else {
                        return Center( child: Text(
                          "Peso: ${_peso?.toStringAsFixed(3)} kg",
                          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                        ));
                      }
                    } else if (_compraProdutoViewModel.getPeso.isFailure) {
                      _peso = null;
                      return Center(child: CircularProgressIndicator());
                    } else {
                      final success = _compraProdutoViewModel.getPeso.value
                          as SuccessCommand;
                      _peso = success.value as double;
                      return Center( child: Text(
                        "Peso: ${_peso?.toStringAsFixed(3)} kg",
                        style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                      ));
                    }
                  })),
              Expanded(
                  flex: 1,
                  child: ElevatedButton(
                      onPressed: () => context.go(AppRoutes.compraCartao),
                      child: Text("Fechar"))
              )
            ],
          )
    ]

    );



  }

  _table(){
    return SizedBox.expand(
      child: ListenableBuilder(
        listenable: _compraProdutoViewModel.getCompra,
        builder: (context, child) {
          if(_compraProdutoViewModel.getCompra.isRunning)
          {
            return Center(child: CircularProgressIndicator());
          }
           else if (_compraProdutoViewModel.getCompra.isFailure)
          {
            final error = _compraProdutoViewModel.getCompra.value as FailureCommand;
            return Text(error.error.toString());
          }
           else if(_compraProdutoViewModel.getCompra.isSuccess)
          {
            final success = _compraProdutoViewModel.getCompra.value as SuccessCommand;
            var compra = success.value as Compra;
            return CustomTable<CompraProduto>(
              title: "Produtos comprados",
              data: compra.compraProdutos,
              columnHeaders: ["produto", "qntd", "preco"],
              formatters: (p) => {
                "produto" : (produto) {
                  Produto p = Produto.fromJson(produto);
                  return '${p.nome}  ${p.codigo}';
                },
                "preco": (preco) {
                  return _formatCurrency(preco*p.qntd);
                },
              },
              getActions: compra.saida == null ? (compraProduto) {
                return [
                  IconButton(
                      onPressed: () async {
                        await _compraProdutoViewModel.addProduto.execute(compraProduto.idComposto.idCompra, compraProduto.idComposto.idProduto);
                        _compraProdutoViewModel.getCompra.execute(widget.compraId);
                      },
                      icon: Icon(Icons.add)),
                  IconButton(
                      onPressed: () async{
                        await _compraProdutoViewModel.removeProduto.execute(compraProduto.idComposto.idCompra, compraProduto.idComposto.idProduto);
                        _compraProdutoViewModel.getCompra.execute(widget.compraId);
                      },
                      icon: Icon(Icons.remove)
                  ),
                ];
              } : null,
          );
          }
          return Container();
        }));

  }

  _appBar() {
    return AppBar(
        title:
        ListenableBuilder(
            listenable: _compraProdutoViewModel.getCompra,
            builder: (context, child) {
              if (_compraProdutoViewModel.getCompra.isRunning) {
                return Row(mainAxisAlignment: MainAxisAlignment.end,
                  children: [Center(child: CircularProgressIndicator())],);
              }
              else if (_compraProdutoViewModel.getCompra.isSuccess) {
                final sucess = _compraProdutoViewModel.getCompra
                    .value as SuccessCommand;
                var compra = sucess.value as Compra;
                return Row(
                  children: [
                    Text(
                        compra.cliente.nome,
                        style: TextStyle(fontWeight: FontWeight.bold)
                    ),
                    Spacer(),
                    Row(
                      children: [
                        Icon(Icons.account_balance_wallet, size: 20),
                        SizedBox(width: 4),
                        Text(_formatCurrency(compra.cliente.saldo),
                            style: TextStyle(fontSize: 18)),
                        SizedBox(width: 16),
                        Icon(Icons.calendar_today, size: 20),
                        SizedBox(width: 4),
                        Text(_formatDate(compra.entrada),
                            style: TextStyle(fontSize: 18)),
                        SizedBox(width: 4),
                      ],
                    ),
                  ],
                );
              }
              return Container();
            }
        )
    );
  }

  String _formatCurrency(double value) {
    return NumberFormat.currency(
      locale: 'pt_BR',
      symbol: 'R\$',
    ).format(value);
  }

  String _formatDate(DateTime date) {
    return DateFormat('dd/MM/yyyy HH:mm').format(date);
  }

  void showError(String error){
    ScaffoldMessenger.of(context).showSnackBar(SnackBar(
      content: Text(
        error,
        style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.bold),
      ),
      backgroundColor: Colors.red,
    ));
  }
}