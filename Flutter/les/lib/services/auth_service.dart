
import 'package:dio/dio.dart';
import 'package:les/core/endpoints.dart';
import 'package:les/model/usuario/credentials.dart';
import 'package:result_dart/result_dart.dart';

class AuthService {
  Dio _dio;

  AuthService(this._dio);

  AsyncResult<String> login(Credentials credentials) async{
    try{
      final json = await _dio.post(Endpoints.login, data: credentials.toJson());
      return Success(json.data.toString());
    } catch (e) {
      return Failure(Exception(e.toString()));
    }
  }

}