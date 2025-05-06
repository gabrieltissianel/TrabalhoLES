import 'package:flutter/material.dart';
import 'package:les/model/produto/produto.dart';
import 'package:les/services/produto_service.dart';
import 'package:result_command/result_command.dart';
import 'package:result_dart/result_dart.dart';

class ProdutoViewModel extends ChangeNotifier{
  final ProdutoService _produtoService;

  ProdutoViewModel(this._produtoService);

  late final getProdutos = Command0(_getProdutos);
  late final addProduto = Command1(_addProduto);
  late final updateProduto = Command1(_updateProduto);
  late final deleteProduto = Command1(_deleteProduto);
  late final getProdutoById = Command1(_getProdutoById);

  AsyncResult<List<Produto>> _getProdutos() async {
    return _produtoService.getAll();
  }

  AsyncResult<Produto> _addProduto(Produto produto) async {
    return _produtoService.create(produto.toJson());
  }

  AsyncResult<Produto> _updateProduto(Produto produto) async {
    return _produtoService.update(produto.id!, produto.toJson());
  }

  AsyncResult<String> _deleteProduto(int id) async {
    return _produtoService.delete(id);
  }

  AsyncResult<Produto> _getProdutoById(int id) async {
    return _produtoService.getById(id);
  }


}