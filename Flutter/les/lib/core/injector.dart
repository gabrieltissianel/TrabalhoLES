
import 'package:auto_injector/auto_injector.dart';
import 'package:dio/dio.dart';
import 'package:les/services/fornecedor_service.dart';
import 'package:les/services/pagamento_service.dart';
import 'package:les/view/fornecedor/view_model/fornecedor_view_model.dart';
import 'package:les/view/fornecedor/view_model/pagamento_view_model.dart';

final injector = AutoInjector();

void setupDependencies(){
  injector.addInstance(Dio());
  injector.addSingleton(FornecedorService.new);
  injector.addSingleton(PagamentoService.new);
  injector.addSingleton(FornecedorViewModel.new);
  injector.addSingleton(PagamentoViewModel.new);
}