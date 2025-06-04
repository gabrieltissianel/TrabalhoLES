import 'package:dio/dio.dart';
import 'package:intl/intl.dart';
import 'package:les/core/app_routes.dart';
import 'package:les/core/endpoints.dart';
import 'package:universal_html/html.dart' as html;

class RelatoriosService {
  Dio _dio;

  RelatoriosService(this._dio);

  void refreshToken() {
    String token = userProvider.user!.token!;
    _dio.options.headers['Authorization'] = 'Bearer $token';
  }

  Future<void> _downloadPdfReport(String nome, bool json, {Map<String, dynamic>? params, Object? body}) async {
    try {
      refreshToken();
      var endpoint = json ? "${Endpoints.baseUrl}/relatorios/$nome" : "${Endpoints.baseUrl}/relatorios/pdf/$nome";
      // Configurar opções para receber bytes
      final response = await _dio.get(
        endpoint,
        data: body,
        queryParameters: params,
        options: Options(
          responseType: ResponseType.bytes, // Importante para arquivos binários
          headers: {'Accept': 'application/pdf'},
        ),
      );

      String filename = _getFilenameFromHeaders(response.headers, nome);

      // Criar blob e disparar download
      final blob = html.Blob([response.data], 'application/pdf');
      final url = html.Url.createObjectUrlFromBlob(blob);
      final anchor = html.AnchorElement(href: url)
        ..setAttribute('download', filename)
        ..click();

      html.Url.revokeObjectUrl(url);

    } on DioException catch (e) {
      print('Erro Dio: ${e.message}');
      // Mostrar erro ao usuário
    } catch (e) {
      print('Erro geral: $e');
    }
  }

  String _getFilenameFromHeaders(Headers headers, String fallbackName) {
    // 1. Verifica se há um cabeçalho personalizado 'filename'
    if (headers.map['filename'] != null && headers.map['filename']!.isNotEmpty) {
      return headers.map['filename']!.first;
    }

    // 2. Verifica o 'Content-Disposition' (formato comum: "attachment; filename=arquivo.pdf")
    final contentDisposition = headers.map['content-disposition']?.first;
    if (contentDisposition != null) {
      final regex = RegExp('filename=["]?([^"]+)["]?');
      final match = regex.firstMatch(contentDisposition);
      if (match != null && match.group(1)!.isNotEmpty) {
        return match.group(1)!;
      }
    }

    // 3. Se não encontrar, usa um nome padrão baseado no parâmetro `nome`
    return 'relatorio_$fallbackName.pdf';
  }

  Future<void> aniversariantes(int mes, {bool json = false}) async {
    await _downloadPdfReport("aniversariantes", json, params: {"mes": mes});
  }

  Future<void> consumoDiarioHoje({bool json = false}) async {
    await _downloadPdfReport("diario", json);
  }

  Future<void> ultimasVendas({bool json = false}) async {
   await _downloadPdfReport("ultimasVendas", json);
  }

  Future<void> clientesDevedores({bool json = false}) async {
    await _downloadPdfReport("clientesEmAberto", json);
  }

  Future<void> vendaProdutos(DateTime dataInicio, DateTime dataFim, {bool json = false}) async {
    await _downloadPdfReport("produtosSerial", json, params: {
      "dataInicio": DateFormat('yyyy-MM-dd').format(dataInicio),
      "dataFim": DateFormat('yyyy-MM-dd').format(dataFim)
    });
  }

  Future<void> ticketMedio(DateTime dataInicio, DateTime dataFim, {bool json = false}) async {
    await _downloadPdfReport("ticketMedio", json, params: {
      "dataInicio": DateFormat('yyyy-MM-dd').format(dataInicio),
      "dataFim": DateFormat('yyyy-MM-dd').format(dataFim)
    });
  }

  Future<void> consumoData(DateTime data, {bool json = false}) async {
    await _downloadPdfReport("consumoData", json, params: {
      "data": DateFormat('yyyy-MM-dd').format(data)});
  }

  Future<void> ultimaCompraCliente(int id, {bool json = false}) async {
    await _downloadPdfReport("ultimasVendas/$id", json);
  }

  Future<void> dreDiario(DateTime dataInicio, DateTime dataFim, {bool json = false}) async {
    await _downloadPdfReport("dre-diario", json, params: {
      "dataInicio": DateFormat('yyyy-MM-dd').format(dataInicio),
      "dataFim": DateFormat('yyyy-MM-dd').format(dataFim)
    });
  }
}