import 'package:flutter/material.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/presentation/viewModel/fornecedor_view_model.dart';
import 'package:provider/provider.dart';

void main() {
  runApp( MultiProvider(
    providers: [
      ChangeNotifierProvider(create: (_) => FornecedorViewModel()),
    ],
    child: MyApp()),
  );
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      routerConfig: router,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.grey),
        useMaterial3: true,
        primaryColor: Colors.blueAccent,
      ),
    );
  }
}