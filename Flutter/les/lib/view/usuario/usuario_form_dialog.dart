import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:les/view/usuario/view_model/tela_view_model.dart';
import 'package:les/view/usuario/view_model/usuario_view_model.dart';
import 'package:les/view/widgets/toast.dart';
import 'package:result_command/result_command.dart';

import '../../core/injector.dart';
import '../../model/usuario/permissao.dart';
import '../../model/usuario/tela.dart';
import '../../model/usuario/usuario.dart';

class UsuarioFormDialog extends StatefulWidget {
  final UsuarioViewModel usuarioViewModel = injector.get<UsuarioViewModel>();
  final Usuario? usuario;

  UsuarioFormDialog({super.key, this.usuario});

  @override
  _UserFormDialogState createState() => _UserFormDialogState();
}

class _UserFormDialogState extends State<UsuarioFormDialog> {
  final TelaViewModel telaViewModel = injector.get<TelaViewModel>();
  final UsuarioViewModel usuarioViewModel = injector.get<UsuarioViewModel>();
  
  final _formKey = GlobalKey<FormState>();
  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _loginController = TextEditingController();
  final TextEditingController _senhaController = TextEditingController();

  List<Tela> _todasTelas = [];
  Tela? _telaSelecionada;
  bool _permAdd = false, _permEdit = false, _permDelete = false;
  List<Permissao> _permissoes = [];

  @override
  void initState() {
    super.initState();
    telaViewModel.getTelas.execute();
    if (widget.usuario != null) {
      _nameController.text = widget.usuario!.nome;
      _loginController.text = widget.usuario!.login;
      _permissoes = List.from(widget.usuario!.permissoes);
    }
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.usuario == null ? "Adicionar Usuário" : "Editar Usuário"),
      content: SingleChildScrollView(
        child: Form(
          key: _formKey,
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextFormField(
                controller: _nameController,
                decoration: InputDecoration(labelText: "Nome"),
                validator: (value) => value!.isEmpty ? "Campo obrigatório" : null,
              ),
              TextFormField(
                controller: _loginController,
                decoration: InputDecoration(labelText: "Login"),
                validator: (value) => value!.isEmpty ? "Campo obrigatório" : null,
              ),
              TextFormField(
                controller: _senhaController,
                decoration: InputDecoration(labelText: "Senha"),
                obscureText: true,
                validator: (value) => value!.isEmpty ? "Campo obrigatório" : null,
              ),

              // Adicionar permissões
              ListenableBuilder(
                  listenable: telaViewModel.getTelas,
                  builder: (context, child) {
                    if (telaViewModel.getTelas.isRunning) {
                      return CircularProgressIndicator();
                    } else if (telaViewModel.getTelas.isFailure) {
                      final error =
                          telaViewModel.getTelas.value as FailureCommand;
                      MensagemAlerta(context, error.error.toString());
                      return Container();
                    } else {
                      final success =
                          telaViewModel.getTelas.value as SuccessCommand;
                      _todasTelas = success.value as List<Tela>;
                      _todasTelas.removeWhere((tela) => _permissoes.any((permissao) => permissao.tela.id == tela.id));
                      return DropdownButtonFormField<Tela>(
                        value: _telaSelecionada,
                        items: _todasTelas
                            .map((tela) => DropdownMenuItem(
                          value: tela,
                          child: Text(tela.nome),
                        ))
                            .toList(),
                        onChanged: (tela) =>
                            setState(() => _telaSelecionada = tela),
                        decoration:
                        InputDecoration(labelText: "Selecione a Tela"),
                      );
                    }
                  }),

              Row(
                children: [
                  Checkbox(value: _permAdd, onChanged: (v) => setState(() => _permAdd = v!)),
                  Text("Add"),
                  Checkbox(value: _permEdit, onChanged: (v) => setState(() => _permEdit = v!)),
                  Text("Edit"),
                  Checkbox(value: _permDelete, onChanged: (v) => setState(() => _permDelete = v!)),
                  Text("Delete"),
                  IconButton(
                    icon: Icon(Icons.add),
                    onPressed: _telaSelecionada == null ? null : () {
                      setState(() {
                        _permissoes.add(Permissao(
                          tela: _telaSelecionada!,
                          add: _permAdd,
                          edit: _permEdit,
                          delete: _permDelete,
                        ));
                        _telaSelecionada = null;
                        _permAdd = _permEdit = _permDelete = false;
                      });
                    },
                  ),
                ],
              ),

              // Lista de permissões
              ...[
                SizedBox(height: 16),
                Text("Permissões Adicionadas", style: TextStyle(fontWeight: FontWeight.bold)),
                DataTable(
                  columns: [
                    DataColumn(label: Text("Tela")),
                    DataColumn(label: Text("Add")),
                    DataColumn(label: Text("Edit")),
                    DataColumn(label: Text("Delete")),
                    DataColumn(label: Text("Ações")),
                  ],
                  rows: _permissoes.map((permissao) => DataRow(
                    cells: [
                      DataCell(Text(permissao.tela.nome)),
                      DataCell(Checkbox(
                        value: permissao.add,
                        onChanged: (v) => permissao.add = v!,
                      )),
                      DataCell(Checkbox(
                        value: permissao.edit,
                        onChanged: (v) => permissao.edit = v!,
                      )),
                      DataCell(Checkbox(
                        value: permissao.delete,
                        onChanged: (v) => permissao.delete = v!,
                      )),
                      DataCell(IconButton(
                        icon: Icon(Icons.delete),
                        onPressed: () {
                          _permissoes.removeWhere((perm) => perm.tela.id == permissao.tela.id);
                          setState(() {
                            telaViewModel.getTelas.execute();
                          });
                        },
                      )),
                    ],
                  )).toList(),
                ),
              ],
            ],
          ),
        ),
      ),
      actions: [
        TextButton(
          child: Text("CANCELAR"),
          onPressed: () => Navigator.of(context).pop(),
        ),
        ElevatedButton(
          child: Text("SALVAR"),
          onPressed: _submitForm,
        ),
      ],
    );
  }

  Future<void> _submitForm() async {
    if (_formKey.currentState!.validate()) {
      final usuario = Usuario(
        id: widget.usuario?.id,
        nome: _nameController.text,
        login: _loginController.text,
        senha: _senhaController.text,
        permissoes: _permissoes,
      );

      if (widget.usuario == null) {
        await widget.usuarioViewModel.addUsuario.execute(usuario);
      } else {
        await widget.usuarioViewModel.updateUsuario.execute(usuario);
      }

      await widget.usuarioViewModel.getUsuarios.execute();
      Navigator.of(context).pop();
    }
  }
}