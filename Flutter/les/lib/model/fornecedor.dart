
class Fornecedor{
  int? id;
  String nome;

  Fornecedor({this.id, required this.nome});

  factory Fornecedor.fromJson(Map<String, dynamic> json){
    return Fornecedor(id: json['id'], nome: json['nome']);
  }

  Map<String, dynamic> toJson(){
    return {
      'id' : id,
      'nome' : nome
    };
  }

}