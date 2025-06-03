
import 'package:les/model/compra/compra.dart';
import 'package:les/services/cliente_service.dart';
import 'package:les/services/compra_service.dart';
import 'package:result_dart/result_dart.dart';

import '../../../model/cliente/cliente.dart';

class HomeViewModel {
  final CompraService _compraService;
  final ClienteService _clienteService;

  HomeViewModel(this._compraService, this._clienteService);

  AsyncResult<Compra> getCompraByCartao(String cartao) async{
    return _compraService.getCompraByCartao(cartao);
  }

  AsyncResult<Cliente> getClienteByCartao(String cartao) async{
    return _clienteService.getClienteByCartao(cartao);
  }

  AsyncResult<Compra> criarCompra(Compra compra) async {
    return _compraService.create(compra.toJson());
  }

  AsyncResult<Compra> concluirCompra(Compra compra) async {
    return _compraService.concluir(compra.id!);
  }
}