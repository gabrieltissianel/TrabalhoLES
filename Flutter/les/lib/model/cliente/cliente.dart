import 'package:les/model/entity.dart';

class Cliente extends Entity{

  String nome;
  String cartao;
  double limite;
  double saldo;
  DateTime? ultimoDiaNegativado;
  DateTime dtNascimento;

  Cliente({super.id, required this.cartao, required this.nome, required this.limite, this.saldo = 0, this.ultimoDiaNegativado, required this.dtNascimento});

  factory Cliente.fromJson(Map<dynamic, dynamic> json){
    return Cliente(
        id: json['id'],
        nome: json['nome'],
        cartao: json['cartao'],
        limite: json['limite'],
        saldo: json['saldo'],
        ultimoDiaNegativado: json['ultimo_dia_negativado'] == null ? null : DateTime.parse(json['ultimo_dia_negativado']),
        dtNascimento: DateTime.parse(json['dt_nascimento']));
  }

  @override
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'nome': nome,
      'cartao': cartao,
      'limite': limite,
      'saldo': saldo,
      'ultimo_dia_negativado': ultimoDiaNegativado?.toIso8601String(),
      'dt_nascimento': dtNascimento.toIso8601String()
    };
  }

}