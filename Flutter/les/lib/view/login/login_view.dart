import 'package:go_router/go_router.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/injector.dart';
import 'package:flutter/material.dart';
import 'package:les/model/usuario/usuario.dart';
import 'package:les/view/login/view_model/login_view_model.dart';
import 'package:les/view/widgets/toast.dart';
import 'package:result_command/result_command.dart';

import '../../model/usuario/credentials.dart';

class LoginView extends StatefulWidget {
  const LoginView({super.key});

  @override
  State<StatefulWidget> createState() => _LoginState();
}

class _LoginState extends State<LoginView>{
  final _loginViewModel = injector.get<LoginViewModel>();

  final _formKey = GlobalKey<FormState>();
  final _loginController = TextEditingController();
  final _passwordController = TextEditingController();

  _login(BuildContext context) async{
    if (_formKey.currentState!.validate()){
      await _loginViewModel.login.execute(Credentials(_loginController.text.trim(), _passwordController.text.trim()));
      if (_loginViewModel.login.isSuccess){
        final sucess = _loginViewModel.login.value as SuccessCommand;
        _loginViewModel.user = sucess.value as Usuario;
        context.go(AppRoutes.home);
      } else if (_loginViewModel.login.isFailure){
        final error = _loginViewModel.login.value as FailureCommand;
        MensagemAlerta(context, error.error.toString());
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Center(
      child: Container(
        constraints: const BoxConstraints(maxWidth: 400),
        padding: const EdgeInsets.all(20),
        child: Form(
            key: _formKey,
            child:
                Column(mainAxisAlignment: MainAxisAlignment.center, children: [
              TextFormField(
                controller: _loginController,
                decoration: const InputDecoration(
                  labelText: "Login",
                  border: OutlineInputBorder(),
                ),
                validator: (value) => value!.isEmpty ? "Login Vazio" : null,
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _passwordController,
                obscureText: true,
                decoration: const InputDecoration(
                  labelText: "Senha",
                  border: OutlineInputBorder(),
                ),
                validator: (value) => value!.isEmpty ? "Senha Vazia" : null,
              ),
              const SizedBox(height: 32),
              ElevatedButton(
                  onPressed: () async => _login(context),
                  child: ListenableBuilder(
                      listenable: _loginViewModel.login,
                      builder: (context, _) {
                        if (_loginViewModel.login.isRunning) {
                          return CircularProgressIndicator();
                        } else {
                          return Text("Login");
                        }
                      }))
            ])),
      ),
    ));
  }
}