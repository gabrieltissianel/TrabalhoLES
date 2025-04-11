import 'package:flutter/material.dart';
import 'package:flutter_masked_text2/flutter_masked_text2.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/produto/produto.dart';
import 'package:les/view/produto/view_model/produto_view_model.dart';

class ProdutoFormDialog extends StatefulWidget {
  final Produto? produto;

  const ProdutoFormDialog({this.produto, super.key});

  @override
  _UserFormDialogState createState() => _UserFormDialogState();
}

class _UserFormDialogState extends State<ProdutoFormDialog> {
  final _formKey = GlobalKey<FormState>();
  final ProdutoViewModel produtoViewModel = injector.get<ProdutoViewModel>();

  final MoneyMaskedTextController _precoController = MoneyMaskedTextController(
    decimalSeparator: ',',
    thousandSeparator: '.',
    leftSymbol: 'R\$ ',
  );

  final MoneyMaskedTextController _custoController = MoneyMaskedTextController(
    decimalSeparator: ',',
    thousandSeparator: '.',
    leftSymbol: 'R\$ ',
  );

  final TextEditingController _codigoController = TextEditingController();
  final TextEditingController _nomeController = TextEditingController();

  bool _unitario = true;

  @override
  void initState() {
    super.initState();
    if (widget.produto != null) {
      _nomeController.text = widget.produto!.nome;
      _codigoController.text = widget.produto!.codigo;
      _precoController.text = widget.produto!.preco.toString();
      _custoController.text = widget.produto!.custo.toString();
    }
  }

  Future<void> _submitForm() async {
    if (_formKey.currentState!.validate()) {
      if (widget.produto == null){
        double preco = _precoController.numberValue;
        double custo = _custoController.numberValue;
        String nome = _nomeController.text;
        String codigo = _codigoController.text;
        Produto produto =Produto(
          codigo: codigo,
          nome: nome,
          preco: preco,
          custo: custo,
          unitario: _unitario
        );
        await produtoViewModel.addProduto.execute(produto);
      } else {

      }
      await produtoViewModel.getProdutos.execute();
      Navigator.of(context).pop(); // Fecha o pop-up após o envio
    }
  }


  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text("Cadastrar Produto"),
      content: Form(
        key: _formKey,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextFormField(
              controller: _nomeController,
              decoration: const InputDecoration(
                labelText: "Nome",
                border: OutlineInputBorder(),
              ),
              validator: (value) =>
              value!.isEmpty ? "O nome não pode estar vazio" : null,
            ),
            const SizedBox(height: 16),
            TextFormField(
              controller: _codigoController,
              decoration: const InputDecoration(
                labelText: "Codigo",
                border: OutlineInputBorder(),
              ),
              validator: (value) =>
              value!.isEmpty ? "O codigo não pode estar vazio" : null,
            ),
            const SizedBox(height: 16),
            TextFormField(
              controller: _precoController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(
                labelText: "Preço",
                border: OutlineInputBorder(),
              ),
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return "Informe um preço";
                }
                return null;
              },
            ),
            const SizedBox(height: 16),
            TextFormField(
              controller: _custoController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(
                labelText: "Custo",
                border: OutlineInputBorder(),
              ),
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return "Informe um custo";
                }
                return null;
              },
            ),
            const SizedBox(height: 16),
            RadioListTile<bool>(
              title: Text('Unitario'),
              value: true,
              groupValue: _unitario,
              onChanged: (bool? value) {
                setState(() {
                  _unitario = value!;
                });
              },
            ),
            // RadioButton para opção falsa (false)
            RadioListTile<bool>(
              title: Text('Nao unitario'),
              value: false,
              groupValue: _unitario,
              onChanged: (bool? value) {
                setState(() {
                  _unitario = value!;
                });
              },
            ),
          ],
        ),
      ),
      actions: [
        TextButton(
          onPressed: () => Navigator.of(context).pop(), // Fecha o pop-up
          child: Text("Cancelar"),
        ),
        ElevatedButton(
          onPressed: ()  {
            _submitForm();
          },
          child: Text("Salvar"),
        ),
      ],
    );
  }
}