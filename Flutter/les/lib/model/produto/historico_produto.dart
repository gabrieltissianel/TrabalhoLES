import 'package:les/model/entity.dart';
import 'package:les/model/produto/produto.dart';

class HistoricoProduto extends Entity{

  Produto produto;
  DateTime data;
  double precoNovo;
  double custoNovo;

  HistoricoProduto({super.id, required this.produto, required this.data, required this.precoNovo, required this.custoNovo});

  factory HistoricoProduto.fromJson(Map<dynamic, dynamic> json) =>
      HistoricoProduto(id: json['id'], produto: Produto.fromJson(json['produto']), data: DateTime.parse(json['data']), precoNovo: json['preco_novo'], custoNovo: json['custo_novo']);

  @override
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'produto': produto.toJson(),
      'data': data.toIso8601String(),
      'preco_novo': precoNovo,
      'custo_novo': custoNovo
    };
  }

}