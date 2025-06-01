
import 'package:flutter/material.dart';
import 'package:les/model/usuario/credentials.dart';
import 'package:les/model/usuario/usuario.dart';
import 'package:les/services/auth_service.dart';
import 'package:result_command/result_command.dart';
import 'package:result_dart/result_dart.dart';

import '../../../core/prefs.dart';

class LoginViewModel extends ChangeNotifier {
  final AuthService _authService;

  Usuario? user;

  Future<void> isLoggedIn() async {
    final login = await Prefs.getString("login");
    final senha = await Prefs.getString("senha");
    if (login.isEmpty || senha.isEmpty) {
      return;
    }

    var res = await _login(Credentials(login, senha));
    if (res.isSuccess()) {
      user = res.getOrNull();
    }
  }

  LoginViewModel(this._authService);

  late final login = Command1(_login);

  AsyncResult<Usuario> _login(Credentials credentials) async {
    return _authService.login(credentials).onSuccess((s) {
      Prefs.setString("login", credentials.login);
      Prefs.setString("senha", credentials.senha);
    });
  }

  void logout() {
    user = null;
    Prefs.setString("login", "");
    Prefs.setString("senha", "");
  }
}