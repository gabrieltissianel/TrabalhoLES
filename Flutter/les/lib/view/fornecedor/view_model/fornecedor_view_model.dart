import 'package:flutter/material.dart';
import 'package:les/model/fornecedor.dart';
import 'package:les/services/fornecedor_service.dart';
import 'package:result_command/result_command.dart';
import 'package:result_dart/result_dart.dart';

class FornecedorViewModel extends ChangeNotifier{
  final FornecedorService _fornecedorService;

  FornecedorViewModel(this._fornecedorService);

  late final getFornecedores = Command0(_getFornecedores);
  late final addFornecedor = Command1(_addFornecedor);
  late final updateFornecedor = Command1(_updateFornecedor);
  late final deleteFornecedor = Command1(_deleteFornecedor);
  late final getFornecedorById = Command1(_getFornecedorById);

  AsyncResult<List<Fornecedor>> _getFornecedores() async {
    return _fornecedorService.getAll();
  }

  AsyncResult<Fornecedor> _addFornecedor(Fornecedor fornecedor) async {
    return _fornecedorService.create(fornecedor.toJson());
  }

  AsyncResult<Fornecedor> _updateFornecedor(Fornecedor fornecedor) async {
    return _fornecedorService.update(fornecedor.id!, fornecedor.toJson());
  }

  AsyncResult<String> _deleteFornecedor(int id) async {
    return _fornecedorService.delete(id);
  }

  AsyncResult<Fornecedor> _getFornecedorById(int id) async {
    return _fornecedorService.getById(id);
  }
}