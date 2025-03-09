
import 'package:flutter/material.dart';
import 'package:les/presentation/widgets/side_menu.dart';

class AppLayout extends StatelessWidget {
  const AppLayout({required this.child, super.key});

  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          SideMenu(),
          Expanded(
            child: child,
          ),
        ],
      ),
    );
  }

}