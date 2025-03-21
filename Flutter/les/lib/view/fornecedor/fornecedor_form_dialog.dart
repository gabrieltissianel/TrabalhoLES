import 'package:flutter/material.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/fornecedor/fornecedor.dart';
import 'package:les/view/fornecedor/view_model/fornecedor_view_model.dart';

class FornecedorFormDialog extends StatefulWidget {
  final FornecedorViewModel fornecedorViewModel = injector.get<FornecedorViewModel>();
  final Fornecedor? fornecedor;

  FornecedorFormDialog({super.key, this.fornecedor});

  @override
  _UserFormDialogState createState() => _UserFormDialogState();
}

class _UserFormDialogState extends State<FornecedorFormDialog> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();

  @override
  void initState() {
    super.initState();
    if (widget.fornecedor != null) {
      _nameController.text = widget.fornecedor!.nome;
    }
  }

  Future<void> _submitForm() async {
    if (_formKey.currentState!.validate()) {
      String name = _nameController.text;
      if (widget.fornecedor == null) {
        await widget.fornecedorViewModel.addFornecedor.execute(Fornecedor(nome: name));
      } else {
        await widget.fornecedorViewModel.updateFornecedor.execute(
            Fornecedor(id: widget.fornecedor!.id, nome: name));
      }
      await widget.fornecedorViewModel.getFornecedores.execute();
      Navigator.of(context).pop(); // Fecha o pop-up após o envio
    }
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text("Adicionar Fornecedor"),
      content: Form(
        key: _formKey,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextFormField(
              controller: _nameController,
              decoration: InputDecoration(labelText: "Nome"),
              validator: (value) =>
              value!.isEmpty ? "O nome não pode estar vazio" : null,
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
          onPressed: () async {
            await _submitForm();
          },
          child: Text("Salvar"),
        ),
      ],
    );
  }
}