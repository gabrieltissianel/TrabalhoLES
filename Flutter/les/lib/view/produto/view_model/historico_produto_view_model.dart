import 'package:flutter/material.dart';
import 'package:les/model/produto/historico_produto.dart';
import 'package:result_command/result_command.dart';
import 'package:result_dart/result_dart.dart';

import '../../../services/historico_produto_service.dart';

class HistoricoProdutoViewModel extends ChangeNotifier{
  final HistoricoProdutosService _historicoProdutoService;

  HistoricoProdutoViewModel(this._historicoProdutoService);

  late final getHistoricoProdutos = Command0(_getHistoricoProdutos);
  late final addHistoricoProduto = Command1(_addHistoricoProduto);
  late final updateHistoricoProduto = Command1(_updateHistoricoProduto);
  late final deleteHistoricoProduto = Command1(_deleteHistoricoProduto);
  late final getHistoricoProdutoById = Command1(_getHistoricoProdutoById);
  late final getByClienteId = Command1(_getByClienteId);

  AsyncResult<List<HistoricoProduto>> _getHistoricoProdutos() async {
    return _historicoProdutoService.getAll();
  }

  AsyncResult<HistoricoProduto> _addHistoricoProduto(HistoricoProduto historicoProduto) async {
    return _historicoProdutoService.create(historicoProduto.toJson());
  }

  AsyncResult<HistoricoProduto> _updateHistoricoProduto(HistoricoProduto historicoProduto) async {
    return _historicoProdutoService.update(historicoProduto.id!, historicoProduto.toJson());
  }

  AsyncResult<String> _deleteHistoricoProduto(int id) async {
    return _historicoProdutoService.delete(id);
  }

  AsyncResult<HistoricoProduto> _getHistoricoProdutoById(int id) async {
    return _historicoProdutoService.getById(id);
  }

  AsyncResult<List<HistoricoProduto>> _getByClienteId(int id) async {
    return _historicoProdutoService.getByProdutoId(id);
  }
}