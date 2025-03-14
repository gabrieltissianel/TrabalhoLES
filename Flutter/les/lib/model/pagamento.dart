
import 'package:les/model/entity.dart';
import 'package:les/model/fornecedor.dart';

class Pagamento extends Entity {
  double valor;
  DateTime dt_vencimento;
  DateTime? dt_pagamento;
  Fornecedor fornecedor;

  Pagamento({super.id, required this.valor, required this.dt_vencimento, this.dt_pagamento, required this.fornecedor});

  factory Pagamento.fromJson(Map<dynamic, dynamic> json){
    return Pagamento(
      id: json['id'],
      valor: json['valor'],
      dt_vencimento: DateTime.parse(json['dt_vencimento']),
      dt_pagamento: json['dt_pagamento'] != null ? DateTime.parse(json['dt_pagamento']) : null,
      fornecedor: Fornecedor.fromJson(json['fornecedor'])
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'valor': valor,
      'dt_vencimento': dt_vencimento.toIso8601String(),
      'dt_pagamento': dt_pagamento != null ? dt_pagamento?.toIso8601String() : null,
      'fornecedor': fornecedor.toJson()
    };
  }

}