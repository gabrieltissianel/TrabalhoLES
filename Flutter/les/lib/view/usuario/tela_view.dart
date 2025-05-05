

import 'package:flutter/material.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/usuario/tela.dart';
import 'package:les/view/usuario/tela_form_dialog.dart';
import 'package:les/view/usuario/view_model/tela_view_model.dart';
import 'package:les/view/widgets/custom_table.dart';
import 'package:result_command/result_command.dart';

class TelaView extends StatefulWidget{
  const TelaView({super.key});

  @override
  State<StatefulWidget> createState() => _TelaViewState();
}

class _TelaViewState extends State<TelaView> {
  final TelaViewModel _telaViewModel = injector.get<TelaViewModel>();

  @override
  void initState() {
    super.initState();
    _telaViewModel.getTelas.execute();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          Expanded(
              child: Center(
                  child: ListenableBuilder(
                      listenable: _telaViewModel.getTelas,
                      builder: (context, child) {
                        if (_telaViewModel.getTelas.isRunning) {
                          return CircularProgressIndicator();
                        } else if (_telaViewModel.getTelas.isFailure) {
                          final error = _telaViewModel.getTelas
                              .value as FailureCommand;
                          return Text(error.error.toString());
                        } else {
                          final success = _telaViewModel.getTelas
                              .value as SuccessCommand;
                          final telas = success.value as List<Tela>;
                          return SizedBox.expand(
                              child: CustomTable(
                                title: "Telas",
                                data: telas,
                                columnHeaders: ["id", "nome"],
                                getActions: (tela) {
                                  return [
                                    IconButton(
                                        icon: Icon(Icons.edit),
                                        onPressed: () {
                                          showDialog(
                                            context: context,
                                            builder: (context) =>
                                                TelaFormDialog(tela: tela),
                                          );
                                        }),
                                    IconButton(
                                        icon: Icon(Icons.delete),
                                        onPressed: () async {
                                          await _telaViewModel.deleteTela
                                              .execute(tela.id!);
                                          _telaViewModel.getTelas.execute();
                                        })
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
      floatingActionButton: _buttons(_telaViewModel, context),
    );
  }


  Widget _buttons(TelaViewModel telaViewModel, BuildContext context){
    return Column(
        mainAxisAlignment: MainAxisAlignment.end,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          FloatingActionButton(
            onPressed: () {
              showDialog(
                context: context,
                builder: (context) => TelaFormDialog(),
              );
            },
            child: Icon(Icons.add),
          )
        ]
    );
  }
}


