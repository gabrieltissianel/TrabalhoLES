import 'package:les/model/cliente/cliente.dart';
import 'package:les/model/compra/compra_produto.dart';
import 'package:les/model/entity.dart';

class Compra extends Entity{
  DateTime entrada;
  DateTime? saida;
  Cliente cliente;
  List<CompraProduto> compraProdutos;

  Compra({
    super.id,
    required this.entrada,
    this.saida,
    required this.cliente,
    required this.compraProdutos,
  });

  factory Compra.fromJson(Map<String, dynamic> json){
    return Compra(
      id: json['id'],
      entrada: DateTime.parse(json['entrada']),
      saida: json["saida"] == null ? null : DateTime.parse(json['saida']),
      cliente: Cliente.fromJson(json['cliente']),
      compraProdutos: json['compraProdutos'] == null ? [] : (json['compraProdutos'] as List).map((produto) => CompraProduto.fromJson(produto)).toList(),
    );
  }

  @override
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'entrada': entrada.toIso8601String(),
      'saida': saida?.toIso8601String(),
      'cliente': cliente.toJson(),
      'compraProdutos': compraProdutos.map((produto) => produto.toJson()).toList(),
    };
  }
}