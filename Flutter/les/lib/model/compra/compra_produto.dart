import 'package:les/model/entity.dart';
import 'package:les/model/produto/produto.dart';

class Id implements Entity{
  int idCompra;
  int idProduto;

  Id({
    required this.idCompra,
    required this.idProduto
  });

  @override
  Map<String, dynamic> toJson() {
    return {
      'idcompra': idCompra,
      'idproduto': idProduto
    };
  }

  factory Id.fromJson(Map<String, dynamic> json){
    return Id(
      idCompra: json['idcompra'],
      idProduto: json['idproduto']
    );

  }

  @override
  int? id;
}

class CompraProduto implements Entity{
  Id idComposto;
  Produto produto;
  double qntd;
  double preco;
  double custo;

  CompraProduto({
    required this.idComposto,
    required this.produto,
    required this.qntd,
    required this.preco,
    required this.custo
  });

  factory CompraProduto.fromJson(Map<String, dynamic> json){
    return CompraProduto(
      idComposto: Id.fromJson(json['id']),
      produto: Produto.fromJson(json['produto']),
      qntd: json['qntd'],
      preco: json['preco'],
      custo: json['custo']
    );
  }

  @override
  Map<String, dynamic> toJson(){
    return {
      'id': idComposto.toJson(),
      'produto': produto.toJson(),
      'qntd': qntd,
      'preco': preco,
      'custo': custo
    };
  }

  @override
  int? id;

}