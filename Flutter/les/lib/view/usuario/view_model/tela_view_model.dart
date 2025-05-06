import 'package:flutter/material.dart';
import 'package:les/model/usuario/tela.dart';
import 'package:les/services/tela_service.dart';
import 'package:result_command/result_command.dart';
import 'package:result_dart/result_dart.dart';

class TelaViewModel extends ChangeNotifier{
  final TelaService _telaService;

  TelaViewModel(this._telaService);

  late final getTelas = Command0(_getTelas);
  late final addTela = Command1(_addTela);
  late final updateTela = Command1(_updateTela);
  late final deleteTela = Command1(_deleteTela);
  late final getTelaById = Command1(_getTelaById);

  AsyncResult<List<Tela>> _getTelas() async {
    return _telaService.getAll();
  }

  AsyncResult<Tela> _addTela(Tela tela) async {
    return _telaService.create(tela.toJson());
  }

  AsyncResult<Tela> _updateTela(Tela tela) async {
    return _telaService.update(tela.id!, tela.toJson());
  }

  AsyncResult<String> _deleteTela(int id) async {
    return _telaService.delete(id);
  }

  AsyncResult<Tela> _getTelaById(int id) async {
    return _telaService.getById(id);
  }
}