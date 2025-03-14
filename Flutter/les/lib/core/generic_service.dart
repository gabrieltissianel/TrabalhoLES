

import 'package:dio/dio.dart';
import 'package:les/core/endpoints.dart';
import 'package:result_dart/result_dart.dart';

class GenericService <T extends Object> {
  final Dio _dio ;
  final String _endpoint;
  final T Function(Map<String, dynamic>) fromJson;

  GenericService(this._dio, this._endpoint, this.fromJson);

  // 📌 GET - Buscar todos os registros
  AsyncResult<List<T>> getAll() async {
    try {
      final response = await _dio.get('${Endpoints.baseUrl}/$_endpoint/list');
      return Success((response.data as List).map((e) => fromJson(e)).toList());
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

  // 📌 GET - Buscar por ID
  AsyncResult<T> getById(int id) async {
    try {
      final response = await _dio.get('${Endpoints.baseUrl}/$_endpoint/$id');
      return Success(fromJson(response.data));
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

  // 📌 POST - Criar novo item
  AsyncResult<T> create(Map<String, dynamic> data) async {
    try {
      final response = await _dio.post('${Endpoints.baseUrl}/$_endpoint/add', data: data);
      return Success(fromJson(response.data));
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

  // 📌 PUT - Atualizar item
  AsyncResult<T> update(int id, Map<String, dynamic> data) async {
    try {
      final response = await _dio.put('${Endpoints.baseUrl}/$_endpoint/$id', data: data);
      return Success(fromJson(response.data));
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

  // 📌 DELETE - Excluir item
  AsyncResult<String> delete(int id) async {
    try {
      await _dio.delete('${Endpoints.baseUrl}/$_endpoint/$id');
      return Success("Item excluído com sucesso!");
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }
}