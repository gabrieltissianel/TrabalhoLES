
import 'package:dio/dio.dart';
import 'package:les/core/endpoints.dart';
import 'package:result_dart/result_dart.dart';

class BalancaService {
  Dio dio;

  BalancaService(this.dio);

  AsyncResult<double> getPeso() async{
    try{
      final response = await dio.get("${Endpoints.baseUrl}/balanca/peso");
      double peso = response.data;
      return Success(peso);
    }catch(e){
      return Failure(Exception(e.toString()));
    }
  }
}