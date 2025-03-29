import 'package:flutter/material.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/usuario/usuario.dart';
import 'package:les/view/usuario/view_model/usuario_view_model.dart';

class UsuarioFormDialog extends StatefulWidget {
  final UsuarioViewModel usuarioViewModel = injector.get<UsuarioViewModel>();
  final Usuario? usuario;

  UsuarioFormDialog({super.key, this.usuario});

  @override
  _UserFormDialogState createState() => _UserFormDialogState();
}

class _UserFormDialogState extends State<UsuarioFormDialog> {
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _loginController = TextEditingController();
  final TextEditingController _senhaController = TextEditingController();
  final TextEditingController _permissoesController = TextEditingController();
  
  List<String> _permissoes = [];

  @override
  void initState() {
    super.initState();
    if (widget.usuario != null) {
      _nameController.text = widget.usuario!.nome;
      _loginController.text = widget.usuario!.login;
      _permissoes = widget.usuario!.permissoes;
    }
  }

  Future<void> _submitForm() async {
    if (_formKey.currentState!.validate()) {
      String name = _nameController.text;
      String login = _loginController.text;
      String senha = _senhaController.text;
      if (widget.usuario == null) {
       await widget.usuarioViewModel.addUsuario.execute(
           Usuario(
               nome: name,
               login: login,
               senha: senha,
               permissoes: _permissoes
           )
       );
      } else {
        await widget.usuarioViewModel.updateUsuario.execute(
            Usuario(
                id: widget.usuario?.id,
                nome: name,
                login: login,
                senha: senha,
                permissoes: _permissoes
            )
        );
      }
      await widget.usuarioViewModel.getUsuarios.execute();
      Navigator.of(context).pop(); // Fecha o pop-up após o envio
    }
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text("Adicionar Usuario"),
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
            TextFormField(
              controller: _loginController,
              decoration: InputDecoration(labelText: "Login"),
              validator: (value) =>
              value!.isEmpty ? "O login não pode estar vazio" : null,
            ),
            TextFormField(
              controller: _senhaController,
              decoration: InputDecoration(labelText: "Senha"),
              obscureText: true,
              validator: (value) =>
              value!.isEmpty ? "A senha não pode estar vazia" : null,
            ),


                TextFormField(
                  controller: _permissoesController,
                  decoration: InputDecoration(labelText: "Permissões"),
                ),
                IconButton(
                    onPressed: () {
                      setState(() {
                        _permissoes.add(_permissoesController.text);
                        _permissoesController.clear();
                      });
                    },
                    icon: Icon(Icons.add)),

            DataTable(
                columns:[DataColumn(label: Text("Permissoes", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold)))],
                rows: _permissoes.map((permissao) => DataRow(cells: [DataCell(Text(permissao))])).toList()
            )
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