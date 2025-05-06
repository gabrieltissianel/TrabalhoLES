
import 'package:flutter/material.dart';
import 'package:les/core/injector.dart';
import 'package:les/view/login/view_model/login_view_model.dart';

class WidgetComPermissao extends StatelessWidget{
  final userProvider = injector<LoginViewModel>();
  final String permission;
  final Widget child;

  final bool add;
  final bool edit;
  final bool delete;

  WidgetComPermissao({required this.permission, required this.child, super.key, this.add = false, this.edit = false, this.delete = false});

  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
      listenable: userProvider.login,
      builder: (context, _) {
        if (userProvider.user!.hasPermission(permission, add: add, edit: edit, delete: delete)){
          return child;
        } else {
          return Container();
        }
      }
    );
  }

}