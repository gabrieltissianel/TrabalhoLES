import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';
import 'package:les/presentation/view/fornecedor_view.dart';
import 'package:les/presentation/view/home_view.dart';
import 'package:les/presentation/widgets/app_layout.dart';

class AppRoutes {
  static const String home = '/';
  static const String fornecedores = '/fornecedores';
}

final _rootNavigatorKey = GlobalKey<NavigatorState>();
final _shellNavigatorKey = GlobalKey<NavigatorState>();

final GoRouter router = GoRouter(
  navigatorKey: _rootNavigatorKey,
  initialLocation: '/',
  routes: [
    ShellRoute(
      navigatorKey: _shellNavigatorKey,
      builder: (context, state, child) {
        return AppLayout(child: child);
      },
      routes:[
        GoRoute(
            path: AppRoutes.home,
            builder: (context, state) => const HomeView()),
        GoRoute(
            path: AppRoutes.fornecedores,
            builder: (context, state) => const FornecedorView())
      ]
    )
  ]

);