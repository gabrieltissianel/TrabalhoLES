import 'package:les/model/entity.dart';
import 'package:les/model/usuario/permissao.dart';

class Usuario extends Entity{
  final String nome;
  final String login;
  final String? senha;
  final String? token;
  final List<Permissao> permissoes;

  Usuario({super.id, required this.nome, required this.login, this.senha, required this.permissoes, this.token});

  factory Usuario.fromJson(Map<String, dynamic> json){
    return Usuario(
        id: json['id'],
        nome: json['nome'],
        login: json['login'] ?? json['Login'],
        token: json['token'],
        permissoes:(json['permissoes'] as List<dynamic>).map((permissao) => Permissao.fromJson(permissao)).toList());
  }

  @override
  Map<String, dynamic> toJson(){
    return {
      'id': id,
      'nome': nome,
      'login': login,
      'senha': senha,
      'permissoes': permissoes.map((permissao) => permissao.toJson()).toList()
    };
  }

  bool hasPermission(String nomeTela, {bool add = false, bool edit = false, bool delete = false}){
    var permissao = permissoes.indexWhere((element) => element.tela.nome == nomeTela);

    if(permissao == -1){
      return false;
    }

    if(add && !permissoes[permissao].add){
        return false;
    }

    if(edit && !permissoes[permissao].edit){
        return false;
    }

    if(delete && !permissoes[permissao].delete){
        return false;
    }

    return true;
  }
}