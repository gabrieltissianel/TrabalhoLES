import 'package:go_router/go_router.dart';
import 'package:les/presentation/view/fornecedor_view.dart';
import 'package:les/presentation/view/home.dart';

class AppRoutes {
  static const String home = '/';
  static const String fornecedores = '/fornecedores';
}

final GoRouter router = GoRouter(
  initialLocation: '/',
  routes: [
    GoRoute(path: AppRoutes.home, builder: (context, state) => Home()),
    GoRoute(path: AppRoutes.fornecedores, builder: (context, state) => FornecedorView())
  ],
);