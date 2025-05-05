import 'package:flutter/material.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/usuario/tela.dart';
import 'package:les/view/usuario/view_model/tela_view_model.dart';

class TelaFormDialog extends StatefulWidget {
  final TelaViewModel telaViewModel = injector.get<TelaViewModel>();
  final Tela? tela;

  TelaFormDialog({super.key, this.tela});

  @override
  _UserFormDialogState createState() => _UserFormDialogState();
}

class _UserFormDialogState extends State<TelaFormDialog> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();

  @override
  void initState() {
    super.initState();
    if (widget.tela != null) {
      _nameController.text = widget.tela!.nome;
    }
  }

  Future<void> _submitForm() async {
    if (_formKey.currentState!.validate()) {
      String name = _nameController.text;
      if (widget.tela == null) {
        await widget.telaViewModel.addTela.execute(Tela(nome: name));
      } else {
        await widget.telaViewModel.updateTela.execute(
            Tela(id: widget.tela!.id, nome: name));
      }
      await widget.telaViewModel.getTelas.execute();
      Navigator.of(context).pop(); // Fecha o pop-up após o envio
    }
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text("Adicionar Tela"),
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