import 'package:les/model/entity.dart';
import 'package:les/model/produto/produto.dart';

class CompraProduto extends Entity{
  Produto produto;
  double qntd;
  double preco;
  double custo;

  CompraProduto({
    super.id,
    required this.produto,
    required this.qntd,
    required this.preco,
    required this.custo
  });

  factory CompraProduto.fromJson(Map<String, dynamic> json){
    return CompraProduto(
      id: json['id'],
      produto: Produto.fromJson(json['produto']),
      qntd: json['qntd'],
      preco: json['preco'],
      custo: json['custo']
    );
  }

  @override
  Map<String, dynamic> toJson(){
    return {
      'id': id,
      'produto': produto.toJson(),
      'qntd': qntd,
      'preco': preco,
      'custo': custo
    };
  }

}