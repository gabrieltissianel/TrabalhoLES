
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
    if (userProvider.user!.hasPermission("FORNECEDOR"))
    AppRoutes.fornecedores: NavigationRailDestination(
      icon: Icon(Icons.account_balance_outlined),
      selectedIcon: Icon(Icons.account_balance),
      label: Text('Fornecedores'),
    ),
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
    if (currentRoute.startsWith(AppRoutes.fornecedores)) {
      selectedIndex = _destinations.keys.toList().indexOf(AppRoutes.fornecedores);
    } else if (currentRoute.startsWith(AppRoutes.usuarios)) {
      selectedIndex = _destinations.keys.toList().indexOf(AppRoutes.usuarios);
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