import 'package:flutter/material.dart';
import 'package:les/model/cliente/cliente.dart';
import 'package:les/services/cliente_service.dart';
import 'package:result_command/result_command.dart';
import 'package:result_dart/result_dart.dart';

class ClienteViewModel extends ChangeNotifier{
  final ClienteService _clienteService;

  ClienteViewModel(this._clienteService);

  late final getClientes = Command0(_getClientes);
  late final addCliente = Command1(_addCliente);
  late final updateCliente = Command1(_updateCliente);
  late final deleteCliente = Command1(_deleteCliente);
  late final getClienteById = Command1(_getClienteById);

  AsyncResult<List<Cliente>> _getClientes() async {
    return _clienteService.getAll();
  }

  AsyncResult<Cliente> _addCliente(Cliente cliente) async {
    return _clienteService.create(cliente.toJson());
  }

  AsyncResult<Cliente> _updateCliente(Cliente cliente) async {
    return _clienteService.update(cliente.id!, cliente.toJson());
  }

  AsyncResult<String> _deleteCliente(int id) async {
    return _clienteService.delete(id);
  }

  AsyncResult<Cliente> _getClienteById(int id) async {
    return _clienteService.getById(id);
  }
}