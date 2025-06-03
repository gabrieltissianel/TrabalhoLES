import 'package:flutter/material.dart';
import 'package:les/model/compra/compra.dart';
import 'package:les/model/compra/compra_produto.dart';
import 'package:les/model/produto/produto.dart';
import 'package:les/services/balanca_service.dart';
import 'package:les/services/compra_produto_service.dart';
import 'package:les/services/compra_service.dart';
import 'package:les/services/produto_service.dart';
import 'package:result_command/result_command.dart';
import 'package:result_dart/result_dart.dart';

class CompraProdutoViewModel extends ChangeNotifier{
  final CompraService _compraService;
  final ProdutoService _produtoService;
  final CompraProdutoService _compraProdutoService;
  final BalancaService _balancaService;

  final TextEditingController pesquisarController = TextEditingController();
  final FocusNode pesquisarFocusNode = FocusNode();

  CompraProdutoViewModel(this._compraService, this._produtoService, this._compraProdutoService, this._balancaService);

  late final getCompra = Command1(_getCompra);
  late final getProdutos = Command0(_getProdutos);
  late final getPeso = Command0(_getPeso);
  late final addProduto = Command2(_addProduto);
  late final removeProduto = Command2(_removeProduto);
  late final pesquisar = Command0(_pesquisarProduto);
  late final concluir = Command1(_concluirCompra);
  late final peso = Command0(_getPeso);

  AsyncResult<Compra> _getCompra(int idCompra) async {
    return _compraService.getById(idCompra);
  }

  AsyncResult<double> _getPeso() async {
    return _balancaService.getPeso();
  }

  AsyncResult<Compra> _concluirCompra(int idCompra) async {
    return _compraService.concluir(idCompra);
  }

  AsyncResult<List<Produto>> _getProdutos() async {
    return _produtoService.getAll();
  }

  AsyncResult<CompraProduto> _addProduto(int idcompra, int idproduto, {double? qtde}) async {
    return _compraProdutoService.addProduto(idcompra, idproduto, qtde: qtde).onSuccess((onSuccess) => getCompra.execute(idcompra));
  }

  AsyncResult<String> _removeProduto(int idcompra, int idproduto) async {
    return _compraProdutoService.removeProduto(idcompra, idproduto).onSuccess((onSuccess) => getCompra.execute(idcompra));
  }

  AsyncResult<CompraProduto> updateProduto(CompraProduto compraProduto) async {
    return _compraProdutoService.updateProduto(compraProduto).onSuccess((onSuccess) => getCompra.execute(compraProduto.idComposto.idCompra));
  }

  double total(Compra compra) {
    double total = 0;
    for (CompraProduto cp in compra.compraProdutos) {
      total += cp.preco * cp.qntd;
    }
    return total;
  }

  AsyncResult<String> _pesquisarProduto() async {
    if (pesquisarController.text.isNotEmpty){
      final pesquisa = pesquisarController.text.toLowerCase();
      pesquisarController.clear();
      final result = await _produtoService.getAll();

      if(!result.isSuccess()){
        return Failure(Exception("Erro ao obter produtos"));
      }

      var produtos = (result as Success).getOrNull() as List<Produto>?;

      final sucess = getCompra.value as SuccessCommand;
      final compra = sucess.value as Compra;

      if(produtos!.isEmpty){
        return Failure(Exception("Nenhum produto encontrado"));
      }

      produtos = produtos.where((produto) {
        final nome = produto.nome.toLowerCase();
        final codigo = produto.codigo.toLowerCase();
        return nome.contains(pesquisa) || codigo.contains(pesquisa);
      }).toList();

      if (produtos.length > 1){
        return Failure(Exception("Mais de um produto encontrado"));
      }

      Produto produto = produtos.first;
      var saldoSuficiente = await _addProduto(compra.id!, produto.id!).isSuccess();

      if (!saldoSuficiente){
        return Failure(Exception("Saldo insuficiente"));
      }

      return Success("Produto inserido.");
    }
    pesquisarController.clear();
    return Failure(Exception("Nenhum produto encontrado"));
  }
}