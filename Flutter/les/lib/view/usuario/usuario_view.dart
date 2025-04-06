
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/usuario/usuario.dart';
import 'package:les/view/usuario/usuario_form_dialog.dart';
import 'package:les/view/usuario/view_model/usuario_view_model.dart';
import 'package:result_command/result_command.dart';

class UsuarioView extends StatefulWidget{
  const UsuarioView({super.key});

  @override
  State<StatefulWidget> createState() => _UsuarioViewState();
}

class _UsuarioViewState extends State<UsuarioView> {
  final UsuarioViewModel _usuarioViewModel = injector.get<UsuarioViewModel>();

  @override
  void initState() {
    super.initState();
    _usuarioViewModel.getUsuarios.execute();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          Expanded(
              child: Center(
                  child: ListenableBuilder(
                      listenable: _usuarioViewModel.getUsuarios,
                      builder: (context, child) {
                        if (_usuarioViewModel.getUsuarios.isRunning) {
                          return CircularProgressIndicator();
                        } else if (_usuarioViewModel.getUsuarios.isFailure) {
                          final error = _usuarioViewModel.getUsuarios
                              .value as FailureCommand;
                          return Text(error.error.toString());
                        } else {
                          final success = _usuarioViewModel.getUsuarios
                              .value as SuccessCommand;
                          final usuarios = success.value as List<Usuario>;
                          return SizedBox.expand(
                            child: SingleChildScrollView(
                              scrollDirection: Axis.vertical,
                              child: _table(usuarios),
                            ),
                          );
                        }
                      })
              )
          ),
        ],
      ),
      floatingActionButton: _buttons(_usuarioViewModel, context),
    );
  }

  Widget _table(List<Usuario> usuarios){
    return DataTable(
        columns: [
          DataColumn(label: Text("ID", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
          DataColumn(label: Text("Nome", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
          DataColumn(label: Text("Login", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
          DataColumn(label: Text("Açoes", style: TextStyle(fontStyle: FontStyle.italic, fontWeight: FontWeight.bold))),
        ],
        rows: usuarios.map((usuario) {
          return DataRow(cells: [
            DataCell(Text(usuario.id.toString())),
            DataCell(Text(usuario.nome)),
            DataCell(Text(usuario.login)),
            DataCell(Row(
              children: [
                IconButton(
                        icon: Icon(Icons.edit),
                        onPressed: () {
                          showDialog(
                            context: context,
                            builder: (context) =>
                                UsuarioFormDialog(usuario: usuario),
                          );
                        }),
                IconButton(
                        icon: Icon(Icons.delete),
                        onPressed: () async {
                          await _usuarioViewModel.deleteUsuario
                              .execute(usuario.id!);
                          _usuarioViewModel.getUsuarios.execute();
                        })
              ],
            )),
          ]);
        }).toList());
  }

  Widget _buttons(UsuarioViewModel usuarioViewModel, BuildContext context){
    return Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          FloatingActionButton(
            onPressed: () {
              context.go(AppRoutes.tela);
            },
            child: Icon(Icons.screenshot_monitor),
          ),
           FloatingActionButton(
                onPressed: () {
                  showDialog(
                    context: context,
                    builder: (context) => UsuarioFormDialog(),
                  );
                },
                child: Icon(Icons.add),
              )
        ]
    );
  }
}


