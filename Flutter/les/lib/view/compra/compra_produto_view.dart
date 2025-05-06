
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:intl/intl.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/cliente/cliente.dart';
import 'package:les/model/compra/compra.dart';
import 'package:les/model/compra/compra_produto.dart';
import 'package:les/model/produto/produto.dart';
import 'package:les/view/cliente/view_model/cliente_view_model.dart';
import 'package:les/view/compra/view_model/compra_view_model.dart';
import 'package:les/view/produto/view_model/produto_view_model.dart';
import 'package:les/view/widgets/custom_table.dart';
import 'package:les/view/widgets/peso_text_field.dart';
import 'package:result_command/result_command.dart';

class CompraProdutoView extends StatefulWidget {
  final int compraId;

  const CompraProdutoView({super.key, required this.compraId});

  @override
  State<StatefulWidget> createState() => _CompraProdutoViewState();
}

class _CompraProdutoViewState extends State<CompraProdutoView> {
  final CompraViewModel _compraViewModel = injector.get<CompraViewModel>();
  final ProdutoViewModel _produtoViewModel = injector.get<ProdutoViewModel>();
  late Compra compra;

  final TextEditingController _pesquisarController = TextEditingController();
  double _peso = 0;

  @override
  void dispose() {
    _pesquisarController.dispose();
    super.dispose();
  }

  @override
  void initState() {
    super.initState();
    _compraViewModel.getCompraById.execute(widget.compraId);
    _produtoViewModel.getProdutos.execute();
  }

  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
        listenable: _compraViewModel.getCompraById,
        builder: (context, child) {
          if(_compraViewModel.getCompraById.isRunning) {
            return CircularProgressIndicator();
          } else if (_compraViewModel.getCompras.isFailure) {
            final error = _compraViewModel.getCompraById
                .value as FailureCommand;
            return Text(error.error.toString());
          } else if(_compraViewModel.getCompraById.isSuccess){
            final success = _compraViewModel.getCompraById
                .value as SuccessCommand;
            compra = success.value as Compra;
            return Scaffold(
              appBar: _appBar(),
              body: _body(),
            );
          }
          return Container();
        });
  }

  _body() {
    return Padding(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Parte superior - Tabela
          Expanded(
            flex: 4,
            child: _table(),
          ),

          // Espaçamento
          const SizedBox(height: 16), // Alterado de width para height

          // Parte inferior - Formulário (apenas se compra não finalizada)
            Expanded(
              flex: 1,
              child: Card(
                elevation: 4,
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: _form(), // Removida a Column extra
                ),
              ),
            ),
        ],
      ),
    );
  }

  _form() {
    return Column(
      children: [
        // Informações do total
        Text("Total: ${_formatCurrency(compra.compraProdutos.fold(0,
                (previousValue, element) => previousValue + (element.preco * element.qntd)))}",
            style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)
        ),

        const SizedBox(height: 16), // Espaçamento

        Row(
          children: [
            Expanded(
              flex: 2,
              child:
                compra.saida == null ? TextField(
                controller: _pesquisarController,
                autofocus: true,
                decoration: const InputDecoration(
                  labelText: 'Pesquisar Produto',
                  border: OutlineInputBorder(),
                ),
                onSubmitted: (value) async {
                  await _pesquisar();
                  _compraViewModel.getCompraById.execute(compra.id!);
                },
              ) : Container(),
            ),
            const SizedBox(width: 16),
            Expanded(
              flex: 1,
              child: compra.saida == null ?  PesoTextField(
                initialValue: 0,
                label: "Peso balança",
                onChanged: (value){
                  _peso = value;
                },
              ) : Container(),
            ),
            const SizedBox(width: 16),
            Expanded(
              flex: 1,
              child: compra.saida == null ? OutlinedButton.icon(
                onPressed: () {
                  var res = _concluir();
                  if (res){
                    compra.saida = DateTime.now();
                    _compraViewModel.updateCompra.execute(compra);
                    context.go(AppRoutes.compras);
                  } else {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(
                        backgroundColor: Colors.red,
                        content: Text("Saldo insuficiente", style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),),
                      ),
                    );
                  }
                },
                icon: const Icon(Icons.check),
                label: const Text('Concluir compra'),
                style: OutlinedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 16),
                ),
              ) : Container(),
            ),
          ],
        ),
      ],
    );
  }

  _pesquisar() async {
    if (_pesquisarController.text.isNotEmpty){
      final success = _produtoViewModel.getProdutos
          .value as SuccessCommand;
      var produtos = success.value as List<Produto>;

      produtos = produtos.where((produto) {
        final nome = produto.nome.toLowerCase();
        final codigo = produto.codigo.toLowerCase();
        final pesquisa = _pesquisarController.text.toLowerCase();
        return nome.contains(pesquisa) || codigo.contains(pesquisa);
      }).toList();

      if (produtos.length == 1){
        Produto produto = produtos[0];
        _pesquisarController.clear();
        await _addProduto(produto);
      }
    }
  }

  _addProduto(Produto produto) async {
    Cliente cliente = compra.cliente;
    var saldo = cliente.saldo - compra.compraProdutos.fold(0,
            (previousValue, element) => previousValue + (element.preco * element.qntd));
    if (saldo < (cliente.limite * -1)){
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          backgroundColor: Colors.red,
          content: Text("Saldo insuficiente", style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),),
        ));
      return;
    }
    if (!produto.unitario){
      compra.compraProdutos.add(CompraProduto(produto: produto, qntd: _peso, custo: produto.custo, preco: produto.preco));
      await _compraViewModel.updateCompra.execute(compra);
      return;
    }
    for (CompraProduto cp in compra.compraProdutos){
      if (cp.produto.id == produto.id){
        cp.qntd++;
        await _compraViewModel.updateCompra.execute(compra);
        return;
      }
    }
    compra.compraProdutos.add(CompraProduto(produto: produto, qntd: 1, custo: produto.custo, preco: produto.preco));
    await _compraViewModel.updateCompra.execute(compra);
  }

  // _productTable(List<Produto> data){
  //     return CustomTable<Produto>(
  //         title: "Produtos",
  //         data: data,
  //         columnHeaders: columnHeaders);
  // }

  _table(){
    return SizedBox.expand(
      child: CustomTable<CompraProduto>(
        title: "Produtos comprados",
        data: compra.compraProdutos,
        columnHeaders: ["produto", "qntd", "preco"],
        formatters: (p) => {
          "produto" : (produto) {
            Produto p = Produto.fromJson(produto);
            return '${p.nome}  ${p.codigo}';
          },
          "preco": (preco) {
            return _formatCurrency(preco*p.qntd);
          },
        },
        getActions: compra.saida == null ? (compraProduto) {
          return [
            IconButton(
                onPressed: () async {
                  compraProduto.qntd++;
                  await _compraViewModel.updateCompra.execute(compra);
                  _compraViewModel.getCompraById.execute(compra.id!);
                },
                icon: Icon(Icons.add)),
            IconButton(
                onPressed: () async{
                  compraProduto.qntd--;
                  await _compraViewModel.updateCompra.execute(compra);
                  _compraViewModel.getCompraById.execute(compra.id!);
                },
                icon: Icon(Icons.remove)
            ),
          ];
        } : null,
      )
    );
  }

  _appBar(){
    return AppBar(
      title: Row(
        children: [
          Text(
              compra.cliente.nome,
              style: TextStyle(fontWeight: FontWeight.bold)
          ),
          Spacer(),
          Row(
            children: [
              Icon(Icons.account_balance_wallet, size: 20),
              SizedBox(width: 4),
              Text(_formatCurrency(compra.cliente.saldo), style: TextStyle(fontSize: 18)),
              SizedBox(width: 16),
              Icon(Icons.calendar_today, size: 20),
              SizedBox(width: 4),
              Text(_formatDate(compra.entrada), style: TextStyle(fontSize: 18)),
              SizedBox(width: 4),
            ],
          ),
        ],
      ),
    );
  }

  String _formatCurrency(double value) {
    return NumberFormat.currency(
      locale: 'pt_BR',
      symbol: 'R\$',
    ).format(value);
  }

  String _formatDate(DateTime date) {
    return DateFormat('dd/MM/yyyy HH:mm').format(date);
  }

  bool _concluir() {
    Cliente cliente = compra.cliente;
    cliente.saldo -= compra.compraProdutos.fold(0,
            (previousValue, element) => previousValue + (element.preco * element.qntd));
    if (cliente.saldo < cliente.limite * -1){
      return false;
    }
    var clienteViewModel = injector.get<ClienteViewModel>();
    clienteViewModel.updateCliente.execute(cliente);
    return true;
  }

}