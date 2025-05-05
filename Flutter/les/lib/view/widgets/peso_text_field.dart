import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class PesoTextField extends StatefulWidget {
  final ValueChanged<double>? onChanged;
  final double? initialValue;
  final String? label;

  const PesoTextField({
    super.key,
    this.onChanged,
    this.initialValue,
    this.label = 'Peso (kg)',
  });

  @override
  State<PesoTextField> createState() => _PesoTextFieldState();
}

class _PesoTextFieldState extends State<PesoTextField> {
  final TextEditingController _controller = TextEditingController();
  final FocusNode _focusNode = FocusNode();

  @override
  void initState() {
    super.initState();
    // Define o valor inicial se fornecido
    if (widget.initialValue != null) {
      _controller.text = _formatPeso(widget.initialValue!);
    }
    
    // Configura o listener para formatar quando perde o foco
    _focusNode.addListener(_handleFocusChange);
  }

  @override
  void dispose() {
    _controller.dispose();
    _focusNode.removeListener(_handleFocusChange);
    _focusNode.dispose();
    super.dispose();
  }

  void _handleFocusChange() {
    if (!_focusNode.hasFocus && _controller.text.isNotEmpty) {
      // Formata o valor quando o campo perde o foco
      final value = _parsePeso(_controller.text);
      _controller.text = _formatPeso(value);
    }
  }

  double _parsePeso(String text) {
    // Remove caracteres não numéricos e converte para double
    final cleanText = text.replaceAll(RegExp(r'[^0-9.,]'), '').replaceAll(',', '.');
    return double.tryParse(cleanText) ?? 0.0;
  }

  String _formatPeso(double value) {
    // Formata com 3 casas decimais e substitui . por , se preferir
    return value.toStringAsFixed(3).replaceAll('.', ',');
  }

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: _controller,
      focusNode: _focusNode,
      keyboardType: TextInputType.numberWithOptions(decimal: true),
      inputFormatters: [
        // Permite apenas números e vírgula/ponto decimal
        FilteringTextInputFormatter.allow(RegExp(r'^\d*\,?\d*')),
      ],
      decoration: InputDecoration(
        labelText: widget.label,
        suffixText: 'kg',
        border: const OutlineInputBorder(),
      ),
      onChanged: (text) {
        if (text.isNotEmpty) {
          final value = _parsePeso(text);
          widget.onChanged?.call(value);
        }
      },
      validator: (value) {
        if (value == null || value.isEmpty) {
          return 'Informe o peso';
        }
        final peso = _parsePeso(value);
        if (peso <= 0) {
          return 'Peso deve ser maior que zero';
        }
        return null;
      },
    );
  }
}
