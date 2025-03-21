
import 'package:flutter/material.dart';
import 'package:les/model/fornecedor/pagamento.dart';
import 'package:les/services/pagamento_service.dart';
import 'package:result_command/result_command.dart';
import 'package:result_dart/result_dart.dart';

class PagamentoViewModel extends ChangeNotifier{
  final PagamentoService _pagamentoService;
  
  PagamentoViewModel(this._pagamentoService);

  late final getPagamentos = Command0(_getPagamentos);
  late final addPagamento = Command1(_addPagamento);
  late final updatePagamento = Command1(_updatePagamento);
  late final deletePagamento = Command1(_deletePagamento);
  late final getPagamentoByFornecedorId = Command1(_getPagamentosByFornecedorId);


  AsyncResult<List<Pagamento>> _getPagamentos() async {
    return _pagamentoService.getAll();
  }

  AsyncResult<Pagamento> _addPagamento(Pagamento pagamento) async {
    return _pagamentoService.create(pagamento.toJson());
  }

  AsyncResult<Pagamento> _updatePagamento(Pagamento pagamento) async {
    return _pagamentoService.update(pagamento.id!, pagamento.toJson());
  }

  AsyncResult<String> _deletePagamento(int id) async {
    return _pagamentoService.delete(id);
  }

  AsyncResult<List<Pagamento>> _getPagamentosByFornecedorId(int id) async {
    return _pagamentoService.getPagamentoByFornecedorId(id);
  }

}