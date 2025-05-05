import 'package:flutter/material.dart';
import 'package:les/model/entity.dart';

class CustomTable<T extends Entity> extends StatefulWidget {
  final String title;
  final List<T> data;
  final List<String> columnHeaders;
  final List<Widget> Function(T)? getActions;
  final Map<String, String Function(dynamic)> Function(T)? formatters;

  const CustomTable({
    super.key,
    required this.title,
    required this.data,
    required this.columnHeaders,
    this.getActions,
    this.formatters,
  });

  @override
  State<CustomTable<T>> createState() => _CustomTableState<T>();
}

class _CustomTableState<T extends Entity> extends State<CustomTable<T>> {
  List<T> _filteredData = [];
  final TextEditingController _searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _filteredData = widget.data;
  }

  void _filterData(String searchText) {
    setState(() {
      if (searchText.isEmpty) {
        _filteredData = widget.data;
      } else {
        _filteredData = widget.data.where((item) {
          final rowData = item.toJson();
          return rowData.values.any((value) =>
          value?.toString().toLowerCase().contains(searchText.toLowerCase()) ??
              false);
        }).toList();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final hasActions = widget.getActions != null;
    final List<String> headersWithActions = [...widget.columnHeaders];
    if (hasActions) {
      headersWithActions.add('Ações');
    }

    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        Padding(
          padding: const EdgeInsets.only(left: 16.0, right: 16.0, top: 16.0),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                widget.title,
                style: Theme.of(context).textTheme.titleLarge,
              ),
              SizedBox(
                width: 300.0, // Aumentei a largura da barra de pesquisa para web
                child: TextField(
                  controller: _searchController,
                  decoration: InputDecoration(
                    hintText: 'Pesquisar...',
                    prefixIcon: const Icon(Icons.search),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(8.0),
                      borderSide: BorderSide.none,
                    ),
                    filled: true,
                    fillColor: Colors.grey[200],
                    contentPadding: const EdgeInsets.symmetric(horizontal: 12.0, vertical: 8.0),
                  ),
                  onChanged: _filterData,
                ),
              ),
            ],
          ),
        ),
        const SizedBox(height: 8.0),
        Expanded(
          child: _filteredData.isEmpty
              ? Center(child: Text('Nenhum dado encontrado.', style: Theme.of(context).textTheme.bodyMedium))
              : SingleChildScrollView(
            scrollDirection: Axis.vertical,
            child: Card(
              margin: const EdgeInsets.all(16.0),
              child: DataTable(
                headingRowColor: WidgetStateProperty.all(Colors.grey[300]),
                dataRowColor: WidgetStateProperty.resolveWith<Color?>((Set<WidgetState> states) {
                  if (states.contains(WidgetState.selected)) {
                    return Theme.of(context).colorScheme.primary.withOpacity(0.08);
                  }
                  if (states.contains(WidgetState.hovered)) {
                    return Colors.grey[100];
                  }
                  return null;
                }),
                columns: headersWithActions
                    .map((header) => DataColumn(
                  label: Text(
                    header.toUpperCase(),
                    style: const TextStyle(
                      fontStyle: FontStyle.italic,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ))
                    .toList(),
                rows: _filteredData.map((item) {
                  final rowData = item.toJson();
                  List<DataCell> cells = headersWithActions.map((header) {
                    if (header == 'Ações' && hasActions) {
                      final actions = widget.getActions!(item);
                      return DataCell(Row(mainAxisSize: MainAxisSize.min, children: actions));
                    } else {
                      if (widget.formatters != null){
                        Map<String, String Function(dynamic)> formatters = widget.formatters!(item);
                        final formatter = formatters[header];
                        if (formatter != null) {
                          return DataCell(Text(rowData[header] != null ? formatter(rowData[header]) : ''));
                        }
                      }
                      return DataCell(Text(rowData[header]?.toString() ?? ''));
                    }
                  }).toList();
                  return DataRow(cells: cells);
                }).toList(),
              ),
            ),
          ),
        ),
      ],
    );
  }
}