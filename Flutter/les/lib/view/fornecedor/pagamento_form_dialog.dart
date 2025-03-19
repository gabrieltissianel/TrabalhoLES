import 'package:flutter/material.dart';
import 'package:flutter_masked_text2/flutter_masked_text2.dart';
import 'package:intl/intl.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/fornecedor.dart';
import 'package:les/model/pagamento.dart';
import 'package:les/view/fornecedor/view_model/pagamento_view_model.dart';

class PagamentoFormDialog extends StatefulWidget {
  final Fornecedor fornecedor;

  const PagamentoFormDialog({required this.fornecedor, super.key});

  @override
  _UserFormDialogState createState() => _UserFormDialogState();
}

class _UserFormDialogState extends State<PagamentoFormDialog> {
  final _formKey = GlobalKey<FormState>();
  final PagamentoViewModel pagamentoViewModel = injector.get<PagamentoViewModel>();

  final MoneyMaskedTextController _valorController = MoneyMaskedTextController(
    decimalSeparator: ',',
    thousandSeparator: '.',
    leftSymbol: 'R\$ ',
  );

  final TextEditingController _dataController = TextEditingController();

  DateTime? _dataSelecionada;

  @override
  void initState() {
    super.initState();
  }

  Future<void> _submitForm() async {
    if (_formKey.currentState!.validate()) {
      double valor = _valorController.numberValue;
      Pagamento pagamento = Pagamento(dt_vencimento: _dataSelecionada!, valor: valor, fornecedor: widget.fornecedor);
      pagamentoViewModel.addPagamento.execute(pagamento);
      Navigator.of(context).pop(); // Fecha o pop-up após o envio
    }
  }

  Future<void> _selecionarData(BuildContext context) async {
    DateTime? dataSelecionada = await showDatePicker(
      context: context,
      initialDate: _dataSelecionada ?? DateTime.now(),
      firstDate: DateTime.now(),
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
      title: Text("Cadastrar Pagamento"),
      content: Form(
        key: _formKey,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextFormField(
              controller: _valorController,
              keyboardType: TextInputType.number,
              decoration: const InputDecoration(
                labelText: "Valor",
                border: OutlineInputBorder(),
              ),
              validator: (value) {
                if (value == null || value.isEmpty) {
                  return "Informe um valor";
                }
                return null;
              },
            ),
            const SizedBox(height: 16),
            TextFormField(
              controller: _dataController,
              decoration: const InputDecoration(
                labelText: "Data",
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