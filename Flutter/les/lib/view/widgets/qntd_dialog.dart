
import 'package:flutter/material.dart';

class QuantityEditDialog {
  final BuildContext context;
  final int initialQuantity;
  final int minQuantity;
  final int? maxQuantity;
  final Function(int) onSave;

  QuantityEditDialog({
    required this.context,
    required this.onSave,
    this.initialQuantity = 1,
    this.minQuantity = 1,
    this.maxQuantity,
  });

  Future<void> show() async {
    int currentQuantity = initialQuantity;
    final TextEditingController controller = TextEditingController();

    await showDialog(
      context: context,
      builder: (BuildContext context) {
        return StatefulBuilder(
          builder: (context, setState) {
            controller.text = currentQuantity.toString();
            controller.selection = TextSelection.collapsed(
              offset: controller.text.length,
            );

            return AlertDialog(
              title: const Text('Editar quantidade'),
              content: Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  IconButton(
                    icon: const Icon(Icons.remove),
                    onPressed: () {
                      if (currentQuantity > minQuantity) {
                        setState(() => currentQuantity--);
                      }
                    },
                  ),
                  SizedBox(
                    width: 60,
                    child: TextField(
                      controller: controller,
                      textAlign: TextAlign.center,
                      keyboardType: TextInputType.number,
                      decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        contentPadding: EdgeInsets.all(8),
                      ),
                      onChanged: (value) {
                        final parsed = int.tryParse(value);
                        if (parsed != null &&
                            parsed >= minQuantity &&
                            (maxQuantity == null || parsed <= maxQuantity!)) {
                          setState(() => currentQuantity = parsed);
                        }
                      },
                    ),
                  ),
                  IconButton(
                    icon: const Icon(Icons.add),
                    onPressed: () {
                      if (maxQuantity == null || currentQuantity < maxQuantity!) {
                        setState(() => currentQuantity++);
                      }
                    },
                  ),
                ],
              ),
              actions: [
                TextButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text('Cancelar'),
                ),
                TextButton(
                  onPressed: () {
                    Navigator.pop(context);
                    onSave(currentQuantity);
                  },
                  child: const Text('Salvar'),
                ),
              ],
            );
          },
        );
      },
    );
  }
}