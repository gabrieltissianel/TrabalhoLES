import 'package:dio/dio.dart';
import 'package:les/core/endpoints.dart';

class ApiService<T> {
  final Dio _dio;
  final String _endpoint;
  final T Function(Map<String, dynamic>) fromJson;

  ApiService(this._dio, this._endpoint, this.fromJson);

  // 📌 GET - Buscar todos os registros
  Future<List<T>> getAll() async {
    try {
      final response = await _dio.get('${Endpoints.baseUrl}/$_endpoint/list');
      return (response.data as List).map((e) => fromJson(e)).toList();
    } catch (e) {
      throw Exception("Erro ao buscar dados: $e");
    }
  }

  // 📌 GET - Buscar por ID
  Future<T> getById(int id) async {
    try {
      final response = await _dio.get('${Endpoints.baseUrl}/$_endpoint/$id');
      return fromJson(response.data);
    } catch (e) {
      throw Exception("Erro ao buscar item: $e");
    }
  }

  // 📌 POST - Criar novo item
  Future<T> create(Map<String, dynamic> data) async {
    try {
      final response = await _dio.post('${Endpoints.baseUrl}/$_endpoint/add', data: data);
      return fromJson(response.data);
    } catch (e) {
      throw Exception("Erro ao criar item: $e");
    }
  }

  // 📌 PUT - Atualizar item
  Future<T> update(int id, Map<String, dynamic> data) async {
    try {
      final response = await _dio.put('${Endpoints.baseUrl}/$_endpoint/$id', data: data);
      return fromJson(response.data);
    } catch (e) {
      throw Exception("Erro ao atualizar item: $e");
    }
  }

  // 📌 DELETE - Excluir item
  Future<void> delete(int id) async {
    try {
      await _dio.delete('${Endpoints.baseUrl}/$_endpoint/$id');
    } catch (e) {
      throw Exception("Erro ao excluir item: $e");
    }
  }
}
