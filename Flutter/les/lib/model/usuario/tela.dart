
import 'package:les/model/entity.dart';

class Tela extends Entity{
  final String nome;

  Tela({super.id, required this.nome});

  factory Tela.fromJson(Map<String, dynamic> json){
    return Tela(
        id: json['id'],
        nome: json['nome']
    );
  }

  @override
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nome': nome
    };
  }
}