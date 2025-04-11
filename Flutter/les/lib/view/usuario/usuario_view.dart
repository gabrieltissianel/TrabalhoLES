
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/usuario/usuario.dart';
import 'package:les/view/usuario/usuario_form_dialog.dart';
import 'package:les/view/usuario/view_model/usuario_view_model.dart';
import 'package:les/view/widgets/custom_table.dart';
import 'package:les/view/widgets/widget_com_permissao.dart';
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
                            child: CustomTable<Usuario>(
                              title: "Usuarios",
                              data: usuarios,
                              columnHeaders: ['id', 'nome', 'login'],
                              getActions: (usuario) {
                                return [
                                  WidgetComPermissao(
                                    permission: "/usuario",
                                    edit: true,
                                    child: IconButton(
                                      icon: const Icon(Icons.edit),
                                      tooltip: 'Editar',
                                      onPressed: () {
                                        showDialog(
                                          context: context,
                                          builder: (context) =>
                                              UsuarioFormDialog(
                                                  usuario: usuario),
                                        );
                                      },
                                    ),
                                  ),
                                  WidgetComPermissao(
                                    permission: '/usuario',
                                    delete: true,
                                    child: IconButton(
                                        icon: const Icon(Icons.delete),
                                        tooltip: 'Excluir',
                                        onPressed: () async {
                                          await _usuarioViewModel.deleteUsuario
                                              .execute(usuario.id!);
                                          _usuarioViewModel.getUsuarios
                                              .execute();
                                        }),
                                  )
                                ];
                              },
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


