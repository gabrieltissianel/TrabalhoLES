
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_masked_text2/flutter_masked_text2.dart';
import 'package:les/core/injector.dart';
import 'package:les/view/cliente/view_model/cliente_view_model.dart';

import '../../model/cliente/cliente.dart';
import '../widgets/currency_form_text_field.dart';

class RecargaDialog extends StatefulWidget {
  final Cliente cliente;

  const RecargaDialog({super.key, required this.cliente});

  @override
  State<StatefulWidget> createState() => _RecargaDialogState();

}

class _RecargaDialogState extends State<RecargaDialog> {
  final _formKey = GlobalKey<FormState>();
  final ClienteViewModel clienteViewModel = injector.get<ClienteViewModel>();

  final MoneyMaskedTextController _recargaController = MoneyMaskedTextController(
    decimalSeparator: ',',
    thousandSeparator: '.',
    leftSymbol: 'R\$ ',
    initialValue: 0.00
  );

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text("Recarga"),
      content: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text("Saldo atual: ${widget.cliente.saldo}"),
              const SizedBox(height: 16),
              TextFormField(
                controller: _recargaController,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: "Valor",
                  border: OutlineInputBorder(),
                ),
                inputFormatters: [
                  FilteringTextInputFormatter.digitsOnly,
                  CurrencyPtBrInputFormatter(),
                ],
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return "Informe um valor";
                  }
                  return null;
                },
                )
              ]
          )
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

  void _submitForm() async {
    if (_formKey.currentState!.validate()) {
      double recarga = _recargaController.numberValue;
      double saldoAtual = widget.cliente.saldo;
      double novoSaldo = saldoAtual + recarga;
      widget.cliente.saldo = novoSaldo;
      await clienteViewModel.updateCliente.execute(widget.cliente);
      await clienteViewModel.getClientes.execute();
      Navigator.of(context).pop();
    }
  }

}