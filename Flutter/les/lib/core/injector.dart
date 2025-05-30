
import 'package:auto_injector/auto_injector.dart';
import 'package:dio/dio.dart';
import 'package:les/services/auth_service.dart';
import 'package:les/services/balanca_service.dart';
import 'package:les/services/cliente_service.dart';
import 'package:les/services/compra_produto_service.dart';
import 'package:les/services/compra_service.dart';
import 'package:les/services/fornecedor_service.dart';
import 'package:les/services/historico_produto_service.dart';
import 'package:les/services/pagamento_service.dart';
import 'package:les/services/produto_service.dart';
import 'package:les/services/recarga_service.dart';
import 'package:les/services/relatorios_service.dart';
import 'package:les/services/tela_service.dart';
import 'package:les/services/usuario_service.dart';
import 'package:les/view/cliente/view_model/cliente_view_model.dart';
import 'package:les/view/cliente/view_model/historico_recarga_view_model.dart';
import 'package:les/view/compra/view_model/compra_produto_view_model.dart';
import 'package:les/view/compra/view_model/compra_view_model.dart';
import 'package:les/view/fornecedor/view_model/fornecedor_view_model.dart';
import 'package:les/view/fornecedor/view_model/pagamento_view_model.dart';
import 'package:les/view/home/view_model/home_view_model.dart';
import 'package:les/view/login/view_model/login_view_model.dart';
import 'package:les/view/produto/view_model/historico_produto_view_model.dart';
import 'package:les/view/produto/view_model/produto_view_model.dart';
import 'package:les/view/usuario/view_model/tela_view_model.dart';
import 'package:les/view/usuario/view_model/usuario_view_model.dart';

final injector = AutoInjector();

void setupDependencies(){
  injector.addInstance(Dio());
  injector.addSingleton(FornecedorService.new);
  injector.addSingleton(PagamentoService.new);
  injector.addSingleton(FornecedorViewModel.new);
  injector.addSingleton(PagamentoViewModel.new);
  injector.addSingleton(AuthService.new);
  injector.addSingleton(LoginViewModel.new);
  injector.addSingleton(UsuarioViewModel.new);
  injector.addSingleton(UsuarioService.new);
  injector.addSingleton(TelaViewModel.new);
  injector.addSingleton(TelaService.new);
  injector.addSingleton(ClienteViewModel.new);
  injector.addSingleton(ClienteService.new);
  injector.addSingleton(ProdutoViewModel.new);
  injector.addSingleton(ProdutoService.new);
  injector.addSingleton(HistoricoProdutosService.new);
  injector.addSingleton(HistoricoProdutoViewModel.new);
  injector.addSingleton(CompraViewModel.new);
  injector.addSingleton(CompraService.new);
  injector.addSingleton(CompraProdutoViewModel.new);
  injector.addSingleton(CompraProdutoService.new);
  injector.addSingleton(RelatoriosService.new);
  injector.addSingleton(BalancaService.new);
  injector.addSingleton(HomeViewModel.new);
  injector.addSingleton(HistoricoRecargaViewModel.new);
  injector.addSingleton(RecargaService.new);
}