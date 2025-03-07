import 'package:go_router/go_router.dart';
import 'package:les/presentation/view/fornecedor_view.dart';
import 'package:les/presentation/view/home.dart';

class AppRoutes {
  static const String HOME = '/';
  static const String FORNECEDORES = '/fornecedores';
}

final GoRouter router = GoRouter(
  initialLocation: '/',
  routes: [
    GoRoute(path: AppRoutes.HOME, builder: (context, state) => Home()),
    GoRoute(path: AppRoutes.FORNECEDORES, builder: (context, state) => FornecedorView())
  ],
);