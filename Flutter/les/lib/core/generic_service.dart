

import 'package:dio/dio.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/endpoints.dart';
import 'package:result_dart/result_dart.dart';

class GenericService <T extends Object> {
  final Dio dio;
  final String endpoint;
  final T Function(Map<String, dynamic>) fromJson;

  GenericService(this.dio, this.endpoint, this.fromJson);

  void refreshToken() {
    String token = userProvider.user!.token!;
    dio.options.headers['Authorization'] = 'Bearer $token';
  }

  // 📌 GET - Buscar todos os registros
  AsyncResult<List<T>> getAll() async {
    refreshToken();
    try {
      final response = await dio.get('${Endpoints.baseUrl}/$endpoint/list');
      return Success((response.data as List).map((e) => fromJson(e)).toList());
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

  // 📌 GET - Buscar por ID
  AsyncResult<T> getById(int id) async {
    refreshToken();
    try {
      final response = await dio.get('${Endpoints.baseUrl}/$endpoint/$id');
      return Success(fromJson(response.data));
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

  // 📌 POST - Criar novo item
  AsyncResult<T> create(Map<String, dynamic> data) async {
    refreshToken();
    try {
      final response = await dio.post('${Endpoints.baseUrl}/$endpoint/add', data: data);
      return Success(fromJson(response.data));
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

  // 📌 PUT - Atualizar item
  AsyncResult<T> update(int id, Map<String, dynamic> data) async {
    refreshToken();
    try {
      final response = await dio.put('${Endpoints.baseUrl}/$endpoint/$id', data: data);
      return Success(fromJson(response.data));
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

  // 📌 DELETE - Excluir item
  AsyncResult<String> delete(int id) async {
    refreshToken();
    try {
      await dio.delete('${Endpoints.baseUrl}/$endpoint/$id');
      return Success("Item excluído com sucesso!");
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }
}