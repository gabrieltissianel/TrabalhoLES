
import 'package:flutter/material.dart';
import 'package:les/presentation/widgets/side_menu.dart';

class Home extends StatefulWidget{
  const Home({super.key});

  @override
  State<StatefulWidget> createState() => _HomeState();

}

class _HomeState extends State<Home>{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          SideMenu()
        ],
      ),
    );
  }
}