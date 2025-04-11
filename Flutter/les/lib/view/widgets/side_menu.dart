
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:les/core/app_routes.dart';

class SideMenu extends StatefulWidget{
  const SideMenu({super.key});


  @override
  State<StatefulWidget> createState() => _SideMenuState();
}

class _SideMenuState extends State<SideMenu> {

  final Map<String, NavigationRailDestination> _destinations = {
    AppRoutes.home: NavigationRailDestination(
      icon: Icon(Icons.home_outlined),
      selectedIcon: Icon(Icons.home),
      label: Text('Home'),
    ),
    if (userProvider.user!.hasPermission("/cliente"))
    AppRoutes.clientes : NavigationRailDestination(
      icon: Icon(Icons.person_outline),
      selectedIcon: Icon(Icons.person),
      label: Text('Clientes'),
    ),
    if (userProvider.user!.hasPermission("/produto"))
    AppRoutes.produtos: NavigationRailDestination(
      icon: Icon(Icons.shopping_cart_outlined),
      selectedIcon: Icon(Icons.shopping_cart),
      label: Text('Produtos'),
    ),
    if (userProvider.user!.hasPermission("/fornecedor"))
    AppRoutes.fornecedores: NavigationRailDestination(
      icon: Icon(Icons.account_balance_outlined),
      selectedIcon: Icon(Icons.account_balance),
      label: Text('Fornecedores'),
    ),
    if (userProvider.user!.hasPermission("/usuario"))
    AppRoutes.usuarios: NavigationRailDestination(
        icon: Icon(Icons.account_circle_outlined),
        selectedIcon: Icon(Icons.account_circle),
        label: Text('Usuarios')
    ),
    AppRoutes.login: NavigationRailDestination(
        icon: Icon(Icons.logout_outlined),
        label: Text('Logout')
    ),

  };

  void _onDestinationSelected(int index) {
    _destinations.keys.toList()[index];
    if (_destinations.keys.toList()[index] == AppRoutes.login) {
      userProvider.logout();
    }
    context.go(_destinations.keys.toList()[index]);
  }

  @override
  Widget build(BuildContext context) {
    String currentRoute = GoRouterState.of(context).uri.toString();

    int selectedIndex = 0;
    if (currentRoute.startsWith(AppRoutes.fornecedores) || currentRoute.startsWith(AppRoutes.pagamentos)) {
      selectedIndex = _destinations.keys.toList().indexOf(AppRoutes.fornecedores);
    } else if (currentRoute.startsWith(AppRoutes.usuarios) || currentRoute.startsWith(AppRoutes.tela)) {
      selectedIndex = _destinations.keys.toList().indexOf(AppRoutes.usuarios);
    } else if (currentRoute.startsWith(AppRoutes.clientes)) {
      selectedIndex = _destinations.keys.toList().indexOf(AppRoutes.clientes);
    } else if (currentRoute.startsWith(AppRoutes.produtos)) {
      selectedIndex = _destinations.keys.toList().indexOf(AppRoutes.produtos);
    }

    return NavigationRail(
      selectedIndex: selectedIndex,
      onDestinationSelected: _onDestinationSelected,
      labelType: NavigationRailLabelType.all,
      groupAlignment: 0,
      destinations: _destinations.values.toList()
    );
  }
}