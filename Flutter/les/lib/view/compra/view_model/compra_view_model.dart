import 'package:flutter/material.dart';
import 'package:les/model/compra/compra.dart';
import 'package:les/services/compra_service.dart';
import 'package:result_command/result_command.dart';
import 'package:result_dart/result_dart.dart';

class CompraViewModel extends ChangeNotifier{
  final CompraService _compraService;

  CompraViewModel(this._compraService);

  late final getCompras = Command0(_getCompraes);
  late final addCompra = Command1(_addCompra);
  late final updateCompra = Command1(_updateCompra);
  late final deleteCompra = Command1(_deleteCompra);
  late final getCompraById = Command1(_getCompraById);

  AsyncResult<List<Compra>> _getCompraes() async {
    return _compraService.getAll();
  }

  AsyncResult<Compra> _addCompra(Compra compra) async {
    return _compraService.create(compra.toJson());
  }

  AsyncResult<Compra> _updateCompra(Compra compra) async {
    return _compraService.update(compra.id!, compra.toJson());
  }

  AsyncResult<String> _deleteCompra(int id) async {
    return _compraService.delete(id);
  }

  AsyncResult<Compra> _getCompraById(int id) async {
    return _compraService.getById(id);
  }
}