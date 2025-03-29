import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';
import 'package:les/core/injector.dart';
import 'package:les/view/fornecedor/fornecedor_view.dart';
import 'package:les/view/fornecedor/pagamento_view.dart';
import 'package:les/view/home/home_view.dart';
import 'package:les/view/login/login_view.dart';
import 'package:les/view/login/view_model/login_view_model.dart';
import 'package:les/view/usuario/usuario_view.dart';
import 'package:les/view/widgets/app_layout.dart';

import '../view/usuario/view_model/usuario_view_model.dart';

class AppRoutes {
  static const String home = '/';
  static const String fornecedores = '/fornecedores';
  static const String pagamentos = '/pagamentos';
  static const String login = '/login';
  static const String usuarios = '/usuarios';
}

final _rootNavigatorKey = GlobalKey<NavigatorState>();
final _shellNavigatorKey = GlobalKey<NavigatorState>();

final userProvider = injector<LoginViewModel>();

final GoRouter router = GoRouter(
  refreshListenable: userProvider,
  redirect: (BuildContext context, GoRouterState state) {
    final uri = state.uri.toString();

    if (!userProvider.isLoggedIn) {
      return AppRoutes.login;
    }

    switch (uri) {
      case AppRoutes.login:
        if (userProvider.isLoggedIn){
          return AppRoutes.home;
        }
        break;
      case AppRoutes.fornecedores:
      case AppRoutes.pagamentos:
        if (!userProvider.user!.hasPermission("FORNECEDOR_VIEW")) {
          return AppRoutes.home;
        }
        break;
      default:
        return null;
    }

  },
  navigatorKey: _rootNavigatorKey,
  initialLocation: AppRoutes.login,
  routes: [
    ShellRoute(
      navigatorKey: _shellNavigatorKey,
      builder: (context, state, child) {
        return AppLayout(child: child);
      },
      routes:[
        GoRoute(
            path: AppRoutes.usuarios,
            builder: (context, state) => const UsuarioView()),
        GoRoute(
            path: AppRoutes.home,
            builder: (context, state) => const HomeView()),
        GoRoute(
            path: AppRoutes.fornecedores,
            builder: (context, state) => const FornecedorView()),
        GoRoute(
            path: '${AppRoutes.fornecedores}/:fornecedorId/${AppRoutes.pagamentos}',
            builder: (context, state) {
              final fornecedorId = state.pathParameters['fornecedorId'];
              return PagamentoView(fornecedorId: int.parse(fornecedorId!));
            },
        ),
      ]
    ),
    GoRoute(
      path: AppRoutes.login,
      builder: (context, state) => const LoginView(),
    ),
  ]

);