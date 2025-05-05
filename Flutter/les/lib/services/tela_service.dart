
import 'package:dio/dio.dart';
import 'package:les/core/generic_service.dart';
import 'package:les/model/usuario/tela.dart';

class TelaService extends GenericService<Tela> {

  TelaService(Dio dio) : super(dio, 'tela', (data) => Tela.fromJson(data));

}