
import 'package:flutter/material.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/injector.dart';
import 'package:les/view/home/view_model/home_view_model.dart';
import 'package:result_dart/result_dart.dart';

class CompraCartaoView extends StatefulWidget{
  const CompraCartaoView({super.key});

  @override
  State<StatefulWidget> createState() => _CompraCartaoView();
}

class _CompraCartaoView extends State<CompraCartaoView>{
  final _viewModel = injector.get<HomeViewModel>();

  final _cartaoController = TextEditingController();
  final _cartaoFocus = FocusNode();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Caixa"),),
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
                            showError("Cliente nao registrou entrada.");
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
      floatingActionButton: button(),
    );
  }

  button(){
    return FloatingActionButton(
      child: Icon(Icons.history),
      onPressed: () {
        router.go(AppRoutes.compras);
      },
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