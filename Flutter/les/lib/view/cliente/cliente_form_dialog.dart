import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_masked_text2/flutter_masked_text2.dart';
import 'package:intl/intl.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/cliente/cliente.dart';
import 'package:les/view/cliente/view_model/cliente_view_model.dart';

import '../widgets/currency_form_text_field.dart';

class ClienteFormDialog extends StatefulWidget {
  final Cliente? cliente;

  const ClienteFormDialog({this.cliente, super.key});

  @override
  _UserFormDialogState createState() => _UserFormDialogState();
}

class _UserFormDialogState extends State<ClienteFormDialog> {
  final _formKey = GlobalKey<FormState>();
  final ClienteViewModel clienteViewModel = injector.get<ClienteViewModel>();

  final MoneyMaskedTextController _limiteController = MoneyMaskedTextController(
    decimalSeparator: ',',
    thousandSeparator: '.',
    leftSymbol: 'R\$ ',
    initialValue: 0.00
  );

  final TextEditingController _nomeController = TextEditingController();
  final TextEditingController _cartaoController = TextEditingController();
  final TextEditingController _dataController = TextEditingController();

  DateTime? _dataSelecionada;

  @override
  void initState() {
    super.initState();
    if (widget.cliente != null) {
      _nomeController.text = widget.cliente!.nome;
      _limiteController.text = widget.cliente!.limite.toStringAsFixed(2);
      _dataSelecionada = widget.cliente!.dtNascimento;
      _cartaoController.text = widget.cliente!.cartao;
      _dataController.text = DateFormat('dd/MM/yyyy').format(_dataSelecionada!);
    }
  }

  Future<void> _submitForm() async {
    if (_formKey.currentState!.validate()) {
      if (widget.cliente == null){
        double valor = _limiteController.numberValue;
        String nome = _nomeController.text;
        String cartao = _cartaoController.text;
        Cliente cliente = Cliente(dtNascimento: _dataSelecionada!, cartao: cartao, limite: valor, nome: nome);
        await clienteViewModel.addCliente.execute(cliente);
      } else {
        Cliente cliente = widget.cliente!;
        cliente.nome = _nomeController.text;
        cliente.limite = _limiteController.numberValue;
        cliente.cartao = _cartaoController.text;
        cliente.dtNascimento = _dataSelecionada!;
        await clienteViewModel.updateCliente.execute(cliente);
      }
      await clienteViewModel.getClientes.execute();
      Navigator.of(context).pop(); // Fecha o pop-up após o envio
    }
  }

  Future<void> _selecionarData(BuildContext context) async {
    DateTime? dataSelecionada = await showDatePicker(
      context: context,
      initialDate: _dataSelecionada ?? DateTime.now(),
      firstDate: DateTime(1900),
      lastDate: DateTime(2100),
    );

    if (dataSelecionada != null) {
      setState(() {
        _dataSelecionada = dataSelecionada;
        _dataController.text = DateFormat('dd/MM/yyyy').format(dataSelecionada);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text("Cadastrar Cliente"),
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
              value!.isEmpty ? "Informe um nome" : null,
            ),
            const SizedBox(height: 16),
            TextFormField(
              controller: _cartaoController,
              decoration: const InputDecoration(
                labelText: "Numero Cartao",
                border: OutlineInputBorder(),
              ),
              validator: (value) =>
              value!.isEmpty ? "Informe um numero de cartao" : null,
            ),
            const SizedBox(height: 16),
            TextFormField(
              controller: _limiteController,
              keyboardType: TextInputType.number,
              inputFormatters: [
                FilteringTextInputFormatter.digitsOnly,
                CurrencyPtBrInputFormatter(),
              ],
              decoration: const InputDecoration(
                labelText: "Limite",
                border: OutlineInputBorder(),
              ),
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return "Informe um limite";
                }
                return null;
              },
            ),
            const SizedBox(height: 16),
            TextFormField(
              controller: _dataController,
              decoration: const InputDecoration(
                labelText: "Data de Aniversario",
                suffixIcon: Icon(Icons.calendar_today),
                border: OutlineInputBorder(),
              ),
              readOnly: true,
              onTap: () => _selecionarData(context),
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return "Selecione uma data";
                }
                return null;
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