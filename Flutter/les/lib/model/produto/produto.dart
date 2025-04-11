
import 'package:les/model/entity.dart';

class Produto extends Entity{

  String codigo;
  bool unitario;
  String nome;
  double preco;
  double custo;

  Produto({
    super.id,
    required this.codigo,
    required this.unitario,
    required this.nome,
    required this.preco,
    required this.custo});

  @override
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'codigo': codigo,
      'unitario': unitario,
      'nome': nome,
      'preco': preco,
      'custo': custo
    };
  }

  factory Produto.fromJson(Map<String, dynamic> json) =>
    Produto(id: json['id'], codigo: json['codigo'], unitario: json['unitario'], nome: json['nome'], preco: json['preco'], custo: json['custo']);
}