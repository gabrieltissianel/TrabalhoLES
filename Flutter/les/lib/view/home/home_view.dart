
import 'package:flutter/material.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/compra/compra.dart';
import 'package:les/view/home/view_model/home_view_model.dart';
import 'package:result_dart/result_dart.dart';

class HomeView extends StatefulWidget{
  const HomeView({super.key});

  @override
  State<StatefulWidget> createState() => _HomeViewState();

}

class _HomeViewState extends State<HomeView>{
  final _viewModel = injector.get<HomeViewModel>();

  final _cartaoController = TextEditingController();
  final _cartaoFocus = FocusNode();
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Icon(
            Icons.credit_card,
            size: 400,
          ),
          Center(
            child: SizedBox(
              width: 500,
              child: TextField(
            controller: _cartaoController,
            autofocus: true,
            focusNode: _cartaoFocus,
            decoration: const InputDecoration(
              labelText: "Numero Cartao",
              border: OutlineInputBorder(),
            ),
            onSubmitted: (value) async {
              _viewModel.getClienteByCartao(value).fold((cliente) {
                _viewModel.getCompraByCartao(value).fold((compraAberta) {
                   router.go("${AppRoutes.compras}/${compraAberta.id}");
                }, (failure) {
                  Compra compra = Compra(entrada: DateTime.now(), cliente: cliente, compraProdutos: []);
                  _viewModel.criarCompra(compra).fold((onSuccess){
                    showMessage("Entrada de ${cliente.nome}.");
                  }, (onError) {
                    showError("Acesso negado.");
                  });
                });
              }, (failure){
                showError("Acesso negado.");
              });
              _cartaoController.clear();
              _cartaoFocus.requestFocus();
            }
          ))),
          const SizedBox(height: 32),
        ],
      ),
    );
  }

  void showMessage(String message){
    ScaffoldMessenger.of(context).showSnackBar(SnackBar(
      content: Text(
        message,
        style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.bold),
      ),
      backgroundColor: Colors.green,
    ));
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