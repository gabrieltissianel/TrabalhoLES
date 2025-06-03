import 'package:flutter/material.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/injector.dart';
import 'package:les/view/login/view_model/login_view_model.dart';



void main() async {
  setupDependencies();
  final userProvider = injector<LoginViewModel>();
  await userProvider.isLoggedIn();
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      routerConfig: router,
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.grey),
        useMaterial3: true,
        primaryColor: Colors.blueAccent,
      ),
    );
  }
}