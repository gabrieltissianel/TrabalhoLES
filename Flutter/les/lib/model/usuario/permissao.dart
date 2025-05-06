
import 'package:les/model/entity.dart';
import 'package:les/model/usuario/tela.dart';

class Permissao extends Entity{

  final Tela tela;
  bool add;
  bool edit;
  bool delete;

  Permissao({super.id, required this.tela, required this.add, required this.edit, required this.delete});

  factory Permissao.fromJson(Map<String, dynamic> json){
    return Permissao(
        id: json['id'],
        tela: Tela.fromJson(json['tela']),
        add: json['add'],
        edit: json['edit'],
        delete: json['delete']
    );
  }

  @override
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'tela': tela.toJson(),
      'add': add,
      'edit': edit,
      'delete': delete
    };
  }
}