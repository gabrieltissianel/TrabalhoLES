
import 'package:flutter/material.dart';
import 'package:les/model/usuario/credentials.dart';
import 'package:les/services/auth_service.dart';
import 'package:result_command/result_command.dart';
import 'package:result_dart/result_dart.dart';

class LoginViewModel extends ChangeNotifier {
  final AuthService _authService;

  LoginViewModel(this._authService);

  late final login = Command1(_login);

  AsyncResult<String> _login(Credentials credentials) async {
    return _authService.login(credentials);
  }

}