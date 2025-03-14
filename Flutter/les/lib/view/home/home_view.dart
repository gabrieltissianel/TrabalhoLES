
import 'package:flutter/material.dart';

class HomeView extends StatefulWidget{
  const HomeView({super.key});

  @override
  State<StatefulWidget> createState() => _HomeViewState();

}

class _HomeViewState extends State<HomeView>{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          Expanded(
            child: Center(child: Text('Página Inicial', style: TextStyle(fontSize: 24))),
          ),
        ],
      ),
    );
  }
}