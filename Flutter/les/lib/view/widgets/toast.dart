import 'package:flutter/material.dart';

void MensagemAlerta(BuildContext context, String msg){
  ScaffoldMessenger.of(context).showSnackBar(SnackBar(
    content: Text(
      msg,
      style: TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold),
    ),
    backgroundColor: Colors.red,
  ));

}

void Mensagem(BuildContext context, String msg){
  ScaffoldMessenger.of(context).showSnackBar(SnackBar(
    content: Text(
      msg,
      style: TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold),
    ),
    backgroundColor: Colors.green,
  ));

}