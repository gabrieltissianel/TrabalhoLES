
import 'package:flutter/material.dart';
import 'package:les/model/fornecedor.dart';
import 'package:les/presentation/viewModel/fornecedor_view_model.dart';
import 'package:les/presentation/widgets/side_menu.dart';
import 'package:provider/provider.dart';

class FornecedorView extends StatefulWidget{
  const FornecedorView({super.key});

  @override
  State<StatefulWidget> createState() => _FornecedorViewState();
}

class _FornecedorViewState extends State<FornecedorView>{

  @override
  void initState() {
    super.initState();
    Provider.of<FornecedorViewModel>(context, listen: false).fetchFornecedores();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
            SideMenu(),
            Consumer<FornecedorViewModel>(
                builder: (context, fornecedorViewModel, child) {
                return Column(
                  children:[
                    _table(fornecedorViewModel),
                    _buttons(fornecedorViewModel, context),
                  ]
                );
            }),
          ],
        ),
    );
  }

  Widget _table(FornecedorViewModel fornecedorViewModel){
    return DataTable(
        columns: [
          DataColumn(label: Text("ID")),
          DataColumn(label: Text("Nome")),
          DataColumn(label: Text("Editar")),
          DataColumn(label: Text("Excluir")),
        ],
        rows: fornecedorViewModel.fornecedores.map((fornecedor) {
          return DataRow(cells: [
            DataCell(Text(fornecedor.id.toString())),
            DataCell(Text(fornecedor.nome)),
            DataCell(IconButton(
                icon: Icon(Icons.edit),
                onPressed: () {
                  showDialog(
                    context: context,
                    builder: (context) => FornecedorFormDialog(fornecedorViewModel: fornecedorViewModel, fornecedor: fornecedor),
                  );
                })),
            DataCell(IconButton(
                icon: Icon(Icons.remove),
                onPressed: () {
                  fornecedorViewModel.deleteFornecedor(fornecedor.id!);
                })),
          ]);
        }).toList());
  }

  Widget _buttons(FornecedorViewModel fornecedorViewModel, BuildContext context){
    return Column(
      mainAxisAlignment: MainAxisAlignment.end,
      crossAxisAlignment: CrossAxisAlignment.end,
      children: [
        FloatingActionButton(
          onPressed: () {
            showDialog(
              context: context,
              builder: (context) => FornecedorFormDialog(fornecedorViewModel: fornecedorViewModel),
            );
          },
          child: Icon(Icons.add),
        ),
      ]
    );
  }
}

class FornecedorFormDialog extends StatefulWidget {
  final FornecedorViewModel fornecedorViewModel;
  final Fornecedor? fornecedor;

  const FornecedorFormDialog({super.key, required this.fornecedorViewModel, this.fornecedor});

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

  void _submitForm() {
    if (_formKey.currentState!.validate()) {
      String name = _nameController.text;
      if (widget.fornecedor == null) {
        widget.fornecedorViewModel.addFornecedor(Fornecedor(nome: name));
      } else {
        widget.fornecedorViewModel.updateFornecedor(
            Fornecedor(id: widget.fornecedor!.id, nome: name));
      }

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
          onPressed: _submitForm,
          child: Text("Salvar"),
        ),
      ],
    );
  }
}
