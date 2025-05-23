import 'package:les/model/cliente/cliente.dart';
import 'package:les/model/entity.dart';

class HistoricoRecarga extends Entity{

  final DateTime data;
  final double valor;
  final Cliente cliente;

  HistoricoRecarga({super.id, required this.data, required this.valor, required this.cliente});

  factory HistoricoRecarga.fromJson(Map<String, dynamic> json) {
    return HistoricoRecarga(
        id: json['id'],
        data: DateTime.parse(json['data']),
        valor: json['valor'],
        cliente: Cliente.fromJson(json['cliente'])
    );
  }

  @override
  Map<String, dynamic> toJson() {
    return {
      'id' : this.id,
      'data' : this.data.toIso8601String(),
      'valor' : this.valor,
      'cliente' : this.cliente.toJson()
    };
  }
}