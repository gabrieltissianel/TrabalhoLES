import 'package:les/model/entity.dart';

class Usuario extends Entity{
  final String nome;
  final String login;
  final String? senha;
  final List<String> permissoes;

  Usuario({super.id, required this.nome, required this.login, this.senha, required this.permissoes});

  factory Usuario.fromJson(Map<String, dynamic> json){
    return Usuario(
        id: json['id'],
        nome: json['nome'],
        login: json['login'] ?? json['Login'],
        permissoes:(json['permissoes'] as List<dynamic>).cast<String>());
  }

  @override
  Map<String, dynamic> toJson(){
    return {
      'id': id,
      'nome': nome,
      'login': login,
      'senha': senha,
      'permissoes': permissoes
    };
  }

  bool hasPermission(String permission) => permissoes.contains(permission);
}