
import 'package:flutter/material.dart';

import '../../core/injector.dart';
import '../../services/relatorios_service.dart';

class RelatoriosView extends StatelessWidget {

  const RelatoriosView({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Relatorios")),
      body: _body()
    );
  }
  
  _body(){
    return Padding(
        padding: const EdgeInsets.all(20.0),
        child:
        Row(
          children: [
            SizedBox(width: 20),
            Expanded(
                child: Column(children: [
              // Botão 1
              SizedBox(
                width: double.infinity, // Largura total
                height: 100, // Altura fixa
                child: ElevatedButton(
                  onPressed: () {
                    injector.get<RelatoriosService>().aniversariantes();
                  },
                  style: ElevatedButton.styleFrom(
                    foregroundColor: Colors.lightBlue,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(15),
                    ),
                  ),
                  child: Text('🎂 Aniversariantes',
                      style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
                ),
              ),

              SizedBox(height: 20),
            ])),
            SizedBox(width: 20),
            Expanded(
                child: Column(children: [
                  // Botão 1
                  SizedBox(
                    width: double.infinity, // Largura total
                    height: 100, // Altura fixa
                    child: ElevatedButton(
                      onPressed: () {
                        injector.get<RelatoriosService>().consumoCliente();
                      },
                      style: ElevatedButton.styleFrom(
                        foregroundColor: Colors.lightBlue,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(15),
                        ),
                      ),
                      child: Text('Consumo por cliente',
                          style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
                    ),
                  ),

                  SizedBox(height: 20),
                ])),
            SizedBox(width: 20),
            Expanded(
                child: Column(children: [
                  // Botão 1
                  SizedBox(
                    width: double.infinity, // Largura total
                    height: 100, // Altura fixa
                    child: ElevatedButton(
                      onPressed: () {
                        injector.get<RelatoriosService>().clientesDevedores();
                      },
                      style: ElevatedButton.styleFrom(
                        foregroundColor: Colors.lightBlue,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(15),
                        ),
                      ),
                      child: Text('Cliente devedores',
                          style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
                    ),
                  ),

                  SizedBox(height: 20),
                ])),
            SizedBox(width: 20),
          ],
        ));
  }
}