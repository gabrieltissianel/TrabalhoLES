import 'package:flutter/cupertino.dart';
import 'package:go_router/go_router.dart';
import 'package:les/core/injector.dart';
import 'package:les/model/usuario/permissao.dart';
import 'package:les/view/cliente/cliente_view.dart';
import 'package:les/view/fornecedor/fornecedor_view.dart';
import 'package:les/view/fornecedor/pagamento_view.dart';
import 'package:les/view/home/home_view.dart';
import 'package:les/view/login/login_view.dart';
import 'package:les/view/login/view_model/login_view_model.dart';
import 'package:les/view/produto/produto_view.dart';
import 'package:les/view/usuario/tela_view.dart';
import 'package:les/view/usuario/usuario_view.dart';
import 'package:les/view/widgets/app_layout.dart';


class AppRoutes {
  static const String home = '/';
  static const String fornecedores = '/fornecedor';
  static const String pagamentos = '/pagamento';
  static const String login = '/login';
  static const String usuarios = '/usuario';
  static const String tela = '/tela';
  static const String produtos = '/produto';
  static const String clientes = '/cliente';
  static const String historicoProduto = '/historico_produto';
}

const List<String> freeRoutes = [ AppRoutes.login, AppRoutes.home ];

final _rootNavigatorKey = GlobalKey<NavigatorState>();
final _shellNavigatorKey = GlobalKey<NavigatorState>();

final userProvider = injector<LoginViewModel>();

final GoRouter router = GoRouter(
  refreshListenable: userProvider,
  redirect: (BuildContext context, GoRouterState state) {
    if (!userProvider.isLoggedIn) {
      return AppRoutes.login;
    }

    final uri = state.uri.toString();

    if (freeRoutes.contains(uri)) {
      return null;
    }

    List<Permissao> permissoes = userProvider.user!.permissoes;

    for (Permissao permissao in permissoes) {
      if (uri.startsWith(permissao.tela.nome)) {
        return null;
      }
    }

    return AppRoutes.home;
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
            path: AppRoutes.clientes,
            builder: (context, state) => const ClienteView()),
        GoRoute(
            path: AppRoutes.home,
            builder: (context, state) => const HomeView()),
        GoRoute(
            path: AppRoutes.fornecedores,
            builder: (context, state) => const FornecedorView()),
        GoRoute(
            path: AppRoutes.produtos,
            builder: (context, state) => const ProdutoView()),
        GoRoute(
            path: '${AppRoutes.pagamentos}/:fornecedorId',
            builder: (context, state) {
              final fornecedorId = state.pathParameters['fornecedorId'];
              return PagamentoView(fornecedorId: int.parse(fornecedorId!));
            },
        ),
        GoRoute(
            path: AppRoutes.tela,
            builder: (context, state) => const TelaView()),
      ]
    ),
    GoRoute(
      path: AppRoutes.login,
      builder: (context, state) => const LoginView(),
    ),
  ]

);