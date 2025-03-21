
import 'package:les/model/entity.dart';

class Fornecedor extends Entity{
  String nome;

  Fornecedor({super.id, required this.nome});

  factory Fornecedor.fromJson(Map<dynamic, dynamic> json){
    return Fornecedor(id: json['id'], nome: json['nome']);
  }

  Map<String, dynamic> toJson(){
    return {
      'id' : id,
      'nome' : nome
    };
  }

}