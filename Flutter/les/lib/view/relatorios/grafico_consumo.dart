import 'package:flutter/material.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:intl/intl.dart';
import 'package:result_command/result_command.dart';

import '../../core/injector.dart';
import '../../services/relatorios_service.dart';

class GraficoConsumo extends StatefulWidget {
  // Dados de exemplo (substitua pelos seus dados reais)

  DateTime selectedDate = DateTime.now();

  GraficoConsumo({super.key});

  @override
  State<StatefulWidget> createState() => _GraficoConsumoState();
  
}

class _GraficoConsumoState extends State<GraficoConsumo>{
  final _service = injector.get<RelatoriosService>();

  @override
  void initState() {
    super.initState();
    _service.consumoGrafico.execute(widget.selectedDate);
  }

  @override
  Widget build(BuildContext context) {
    return ListenableBuilder(
        listenable: _service.consumoGrafico,
        builder: (context, _) {
          if (_service.consumoGrafico.isRunning) {
            return Center(child: CircularProgressIndicator());
          } else if (_service.consumoGrafico.isFailure) {
            final error = _service.consumoGrafico.value as FailureCommand;
            return Center(child: Text(error.error.toString()));
          } else {
            final success = _service.consumoGrafico
                .value as SuccessCommand;
            final clientsData = success.value as List<Map<String, dynamic>>;
            return Scaffold(
              appBar: AppBar(
                title: Row(
                  children: [
                    Text('Consumo Diário - ${_formatDate(widget.selectedDate)}'),
                    _dataButton(context)
                  ],
                ) ,
              ),
              body: Padding(
                padding: const EdgeInsets.all(16.0),
                child: Column(
                  children: [
                    Expanded(
                      child:
                      BarChart(
                        BarChartData(
                          barTouchData: BarTouchData(
                            enabled: true,
                            touchTooltipData: BarTouchTooltipData(
                              getTooltipItem: (group, groupIndex, rod, rodIndex) {
                                final client = clientsData[group.x.toInt()];
                                return BarTooltipItem(
                                  '${client['cliente']}\n R\$ ${NumberFormat("#,##0.00", "pt_BR").format(client['consumo'])}',
                                  const TextStyle(color: Colors.white),
                                );
                              },
                            ),
                          ),
                          titlesData: FlTitlesData(
                            show: true,
                            bottomTitles: AxisTitles(
                              sideTitles: SideTitles(
                                showTitles: true,
                                getTitlesWidget: (value, meta) {
                                  final client = clientsData[value.toInt()];
                                  return Padding(
                                    padding: const EdgeInsets.only(top: 8.0),
                                    child: SizedBox(
                                      width: 60,
                                      child: Text(
                                        client['cliente'],
                                        style: const TextStyle(
                                          fontSize: 10,
                                          fontWeight: FontWeight.bold,
                                        ),
                                        maxLines: 2,
                                        overflow: TextOverflow.ellipsis,
                                        textAlign: TextAlign.center,
                                      ),
                                    ),
                                  );
                                },
                                reservedSize: 50,
                              ),
                            ),
                            leftTitles: AxisTitles(
                              sideTitles: SideTitles(
                                showTitles: true,
                                reservedSize: 40,
                                getTitlesWidget: (value, meta) {
                                  return Text(
                                    value.toInt().toString(),
                                    style: const TextStyle(fontSize: 10),
                                  );
                                },
                              ),
                            ),
                            topTitles: AxisTitles(sideTitles: SideTitles(showTitles: false)),
                            rightTitles: AxisTitles(sideTitles: SideTitles(showTitles: false)),
                          ),
                          gridData: FlGridData(
                            show: true,
                            drawVerticalLine: false,
                            getDrawingHorizontalLine: (value) {
                              return FlLine(
                                color: Colors.grey.withOpacity(0.3),
                                strokeWidth: 1,
                              );
                            },
                          ),
                          borderData: FlBorderData(show: false),
                          barGroups: clientsData.asMap().entries.map((entry) {
                            final index = entry.key;
                            final client = entry.value;
                            final consumption = client['consumo'] as double;

                            return BarChartGroupData(
                              x: index,
                              barRods: [
                                BarChartRodData(
                                  toY: consumption,
                                  gradient: _getBarGradient(consumption, clientsData),
                                  width: 20,
                                  borderRadius: BorderRadius.circular(4),
                                ),
                              ],
                              showingTooltipIndicators: [0],
                            );
                          }).toList(),
                          alignment: BarChartAlignment.spaceAround,
                          maxY: _getMaxConsumption(clientsData) * 1.4, // Adiciona 20% de margem
                        ),
                      ),
                    ),
                    const SizedBox(height: 20),
                    _buildConsumptionLegend(),
                  ],
                ),
              ),
            );
          }
        });
      
  }

  _dataButton(BuildContext context) {
    return IconButton(
        onPressed: () async {
          var newDate = await showDatePicker(
              context: context,
              firstDate: DateTime(2020),
            lastDate: DateTime.now(),
            initialDate: DateTime.now());
          if (newDate != null){
            widget.selectedDate = newDate;
            _service.consumoGrafico.execute(newDate);
          }
        },
        icon: Icon(Icons.date_range));
  }

  double _getMaxConsumption(List<Map<String, dynamic>> clientsData) {
    double max = 0;
    for (var client in clientsData) {
      if (client['consumo'] > max) max = client['consumo'];
    }
    return max;
  }

  LinearGradient _getBarGradient(double consumption, List<Map<String, dynamic>> clientsData) {
    final max = _getMaxConsumption(clientsData);
    final ratio = consumption / max;

    return LinearGradient(
      colors: [
        Color.lerp(Colors.green, Colors.orange, ratio)!,
        Color.lerp(Colors.greenAccent, Colors.orangeAccent, ratio)!,
      ],
      begin: Alignment.bottomCenter,
      end: Alignment.topCenter,
    );
  }

  Widget _buildConsumptionLegend() {
    return Container(
      padding: const EdgeInsets.all(8),
      decoration: BoxDecoration(
        color: Colors.grey[200],
        borderRadius: BorderRadius.circular(8),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          _buildLegendItem(Colors.green, 'Baixo consumo'),
          _buildLegendItem(Colors.orange, 'Alto consumo'),
        ],
      ),
    );
  }

  Widget _buildLegendItem(Color color, String text) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(
          width: 16,
          height: 16,
          decoration: BoxDecoration(
            color: color,
            borderRadius: BorderRadius.circular(4),
          ),
        ),
        const SizedBox(width: 4),
        Text(
          text,
          style: const TextStyle(fontSize: 12),
        ),
      ],
    );
  }

  String _formatDate(DateTime date) {
    return '${date.day.toString().padLeft(2, '0')}/${date.month.toString().padLeft(2, '0')}/${date.year}';
  }
  
}
