
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:les/model/fornecedor.dart';
import 'package:les/service/fornecedor_service.dart';

class FornecedorViewModel extends ChangeNotifier{
  final FornecedorService _fornecedorService = FornecedorService(Dio());

  List<Fornecedor> _fornecedores = [];

  List<Fornecedor> get fornecedores => _fornecedores;

  Future<void> fetchFornecedores() async{
    _fornecedores = await _fornecedorService.getAll();
    notifyListeners();
  }

  Future<void> addFornecedor(Fornecedor fornecedor) async{
    await _fornecedorService.create(fornecedor.toJson());
    await fetchFornecedores();
  }

  Future<void> updateFornecedor(Fornecedor fornecedor) async{
    await _fornecedorService.update(fornecedor.id!, fornecedor.toJson());
    await fetchFornecedores();
  }

  Future<void> deleteFornecedor(int id) async{
    await _fornecedorService.delete(id);
    await fetchFornecedores();
  }

}