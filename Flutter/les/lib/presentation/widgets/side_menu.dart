
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:les/core/app_routes.dart';

class SideMenu extends StatefulWidget{
  const SideMenu({super.key});

  @override
  State<StatefulWidget> createState() => _SideMenuState();
}

class _SideMenuState extends State<SideMenu> {

  void _onDestinationSelected(int index) {
    switch (index) {
      case 0:
        context.go(AppRoutes.home);
        break;
      case 1:
        context.go(AppRoutes.fornecedores);
        break;
    }
  }

  @override
  Widget build(BuildContext context) {
    String currentRoute = GoRouterState.of(context).uri.toString();

    int selectedIndex = 0;
    if (currentRoute.startsWith(AppRoutes.fornecedores)) {
      selectedIndex = 1;
    }

    return NavigationRail(
      selectedIndex: selectedIndex,
      onDestinationSelected: _onDestinationSelected,
      labelType: NavigationRailLabelType.all,
      groupAlignment: 0,
      destinations: [
        NavigationRailDestination(
          icon: Icon(Icons.home_outlined),
          selectedIcon: Icon(Icons.home),
          label: Text('Home'),
        ),
        NavigationRailDestination(
          icon: Icon(Icons.account_balance_outlined),
          selectedIcon: Icon(Icons.account_balance),
          label: Text('Fornecedores'),
        ),
      ],
    );
  }
}