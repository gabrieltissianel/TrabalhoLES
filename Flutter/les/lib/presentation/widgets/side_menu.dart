
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:les/core/app_routes.dart';

class SideMenu extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Container(
      width: 80, // Largura do menu lateral
      decoration: BoxDecoration(
        color: Colors.blue.shade900,
        boxShadow: [BoxShadow(color: Colors.black26, blurRadius: 5)],
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          _buildMenuItem(context, Icons.home, 'Home', AppRoutes.HOME),
          _buildMenuItem(context, Icons.account_balance, 'Fornecedores', AppRoutes.FORNECEDORES)
        ],
      ),
    );
  }

  Widget _buildMenuItem(BuildContext context, IconData icon, String label, String route) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 15),
      child: IconButton(
        icon: Icon(icon, color: Colors.white, size: 30),
        tooltip: label, // Mostra o nome ao passar o mouse
        onPressed: () => context.go(route), // Navegação com GoRouter
      ),
    );
  }

}