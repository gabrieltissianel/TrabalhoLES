package com.jga.les.device;
import com.jga.les.model.Cliente;
import com.jga.les.model.Compra;
import com.jga.les.model.CompraProduto;
import com.jga.les.repository.ClienteRepository;
import com.jga.les.service.ClienteService;
import com.jga.les.service.CompraProdutoService;

import jakarta.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

@Service    
public class TMT20XService {
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private CompraProdutoService compraProdutoService;

    private static final byte[] CUT_PAPER = new byte[] { 0x1D, 0x56, 0x41, 0x10 };

    private static final Logger logger = LoggerFactory.getLogger(TMT20XService.class);
    private OutputStream outputStream;
    private TMT20XPrinter printer;
    private NumberFormat formatter = new DecimalFormat("#0.00");     
    private static final String PORTA_SERIAL = "USB-001"; // Altere conforme necessário
    private static final String NOME_IMPRESSORA = "EPSON TM-T20X Receipt6"; // Nome exato da impressora no Windows
    
    private static final int COLUNA_TAMANHO = 60;
    private static final String LINHA = "-".repeat(COLUNA_TAMANHO) + "\n";

    public TMT20XService(ClienteRepository clienteRepository){
        try {
            outputStream.close();
        } catch (Exception e) {
            // Nao faz nada
        }
        try {
            this.outputStream = new FileOutputStream(PORTA_SERIAL);
            this.printer = new TMT20XPrinter(outputStream);
            logger.info("TMT20xPrinter inicializada na porta: {}", PORTA_SERIAL);
        } catch (Exception e) {
            logger.error("Erro ao inicializar TMT20XService: {}", e.getMessage());  
        }
    }

    public void listarImpressoras() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("Impressoras encontradas no sistema:");
        for (PrintService service : services) {
            System.out.println(" - " + service.getName());
        }
    }

    public String testeWindows() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("================================\n");
        sb.append(String.format("| %-28s |\n", "COMPROVANTE DE VENDA"));
        sb.append("================================\n");
        sb.append(String.format("| %-10s %18s |\n", "Teste do Comprovante"));
        sb.append("================================\n");
        sb.append("\n\n\n"); // Espaço para corte
        
        return sb.toString();
    }
    
    public String CriarComprovanteWindows(String cartao) {
        StringBuilder sb = new StringBuilder();
        Compra compra;
        try {
            compra = clienteService.findCompraAberta(cartao).getBody();
        } catch (Exception e) {
            logger.error("Erro ao buscar cliente: {}");
            throw new RuntimeException("Erro ao buscar cliente: " + cartao);
        }

        List<CompraProduto> produtos = compra.getCompraProdutos();

        // Cabeçalho
        sb.append(LINHA);
        sb.append("COMPROVANTE\n");
        sb.append(LINHA);
        
        // Informações do cliente e data
        sb.append(String.format("Cliente: %s\n", compra.getCliente().getNome()));
        sb.append(String.format("Data: %s\n", java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        sb.append(LINHA);
        
        // Cabeçalho dos produtos
        sb.append("ITENS:\n");
        sb.append("Nome\t\t\tQuantidade   Preco   Valor\n");
        // Lista de produtos
        for (CompraProduto produto : produtos) {
            // Nome do produto (linha superior)
            String nomeProduto = produto.getProduto().getNome();
            sb.append(String.format("  %-" + (COLUNA_TAMANHO-2) + "s\n", truncate(nomeProduto, COLUNA_TAMANHO-2)));            
            // Detalhes do produto (linha inferior)
            String detalhes;
            if (produto.getProduto().isUnitario()) {
                detalhes = String.format("   %.2f UN X R$ %.2f = R$ %.2f", 
                    produto.getQntd(), 
                    produto.getPreco(), 
                    produto.getPreco()*produto.getQntd());
            } else {
                detalhes = String.format("   %.3f KG X R$ %.2f = R$ %.2f", 
                    produto.getQntd()/1000.0, 
                    produto.getProduto().isUnitario(), 
                    produto.getPreco()*produto.getQntd());
            }
            
            // Alinha os detalhes à direita
            sb.append(String.format("%" + COLUNA_TAMANHO + "s\n", detalhes));
        }
        
        // Total
        sb.append(LINHA);
        sb.append(String.format("%" + COLUNA_TAMANHO + "s\n", 
            String.format("TOTAL: R$ %.2f", compra.getTotal())));
        sb.append(LINHA);
        
        // Rodapé
        sb.append(centralizar("Obrigado pela preferência!") + "\n");
        sb.append(centralizar("Volte sempre!") + "\n");
        sb.append("\n\n\n"); // Espaço para corte
        
        return sb.toString();
    }
  
    public void imprimirComprovanteWindows(String cartao) {
        String texto = CriarComprovanteWindows(cartao);
        
        InputStream stream = new ByteArrayInputStream(texto.getBytes(StandardCharsets.UTF_8));
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();

        // Procurar a impressora pelo nome
        PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, null);
        PrintService impressoraSelecionada = null;

        for (PrintService service : services) {
            if (service.getName().equalsIgnoreCase(NOME_IMPRESSORA)) {
                impressoraSelecionada = service;
                break;
            }
        }

        if (impressoraSelecionada != null) {
            DocPrintJob job = impressoraSelecionada.createPrintJob();
            Doc doc = new SimpleDoc(stream, flavor, null);
            try {
                job.print(doc, attrs);
                System.out.println("Comprovante enviado para a impressora: " + impressoraSelecionada.getName());
            } catch (PrintException e) {
                System.err.println("Erro ao imprimir: " + e.getMessage());
            }
            DocFlavor flav = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            doc = new SimpleDoc(CUT_PAPER, flav, null);
            job = impressoraSelecionada.createPrintJob();
            try {
                job.print(doc, null);
            } catch (PrintException e) {
                e.printStackTrace();
            }   
        } else {
            System.err.println("Impressora '" + NOME_IMPRESSORA + "' não encontrada.");
        }
    }
    
    public void imprimirComprovanteCompra(Compra compra) {

        List<CompraProduto> produtos = compra.getCompraProdutos();
        try {
            printer.setUnderline(false);
            printer.setBold(false); // Sem negrito
            //titulo
            printer.setAlignment(1); // Alinhamento à esquerda
            printer.setFontSize(2, 2); // Tamanho normal
            printer.setInverseColors(true);
            printer.printText(" Comprovante da Compra \n\n");

            //corpo
            printer.setInverseColors(false);
            printer.setAlignment(0); // Alinhamento à esquerda
            printer.setFontSize(1, 1); // Tamanho normal
            printer.setBold(false); // Sem negrito
            printer.printText("Cliente: " + compra.getCliente().getNome() + "\n");
            printer.printText("Produtos:\n");

            if(produtos.isEmpty()){
                printer.printText("N/A.\n");
            }else{
                for (CompraProduto produto : produtos) {
                    printer.printText(" - " + produto.getProduto().getNome() + ": \tR$" + produto.getPreco() + "\t"
                            + produto.getQntd()
                            + (produto.getProduto().isUnitario() ? "\t" : "Kg\t")
                            + "R$" + formatter.format(produto.getPreco() * produto.getQntd()) + "\n");
                }
                // Total
                printer.setBold(true); // Negrito
            }
            printer.printText("\nTotal: R$" + formatter.format(compra.getTotal()) + "\n");
            printer.cutPaper();

        } catch (Exception e) {
            logger.error("Nenhuma compra encontrada para o cartão: {}", compra.getCliente().getCartao());
        }
    }

    public void imprimirComprovanteCompra(String cartao) {
        Cliente cliente;
        Compra compra;
        List<CompraProduto> produtos;

        try {
            cliente = clienteService.findByCartao(cartao);
        } catch (Exception e) {
            logger.error("Erro ao buscar cliente: {}");
            throw new RuntimeException("Erro ao buscar cliente: " + cartao);
        }
        
        compra = clienteService.findCompraAberta(cartao).getBody();
        if (compra == null) {
            logger.error("Nenhuma compra encontrada para o cartão: {}", cartao);
            throw new RuntimeException("Nenhuma compra encontrada para o cartão: " + cartao);
        }

        produtos = compraProdutoService.findByCompra(compra);
        try {
            printer.setUnderline(false);
            printer.setBold(false); // Sem negrito
            //titulo
            printer.setAlignment(1); // Alinhamento centralizado
            printer.setFontSize(2, 2); // Tamanho normal
            printer.setInverseColors(true);
            printer.printText(" Comprovante da Compra \n\n");

            //corpo
            printer.setInverseColors(false);
            printer.setAlignment(0); // Alinhamento à esquerda
            printer.setFontSize(1, 1); // Tamanho normal
            printer.setBold(false); // Sem negrito
            
            printer.printText("Cliente: " + cliente.getNome() + "\n");
            printer.printText("Produtos:\n");

            printer.setAlignment(1); // Alinhamento à esquerda
            printer.printText("Nome\tPreco\tQntd\tTotal");
            printer.setAlignment(0); // Alinhamento à esquerda
            if(produtos.isEmpty()){
                printer.printText("N/A.\n");
            }else{
                for (CompraProduto produto : produtos) {
                    printer.printText(" - " + produto.getProduto().getNome() + ": \tR$" + produto.getPreco() + "\t"
                    + produto.getQntd()
                    + (produto.getProduto().isUnitario() ? "\t" : "Kg\t")
                    + "R$" + formatter.format(produto.getPreco() * produto.getQntd()) + "\n");
                }
                // Total
                printer.setBold(true); // Negrito
            }
            printer.printText("\nTotal: R$" + formatter.format(compra.getTotal()) + "\n");
            printer.cutPaper();

        } catch (Exception e) {
            logger.error("Nenhuma compra encontrada para o cartão: {}", cartao);
        }
        return; 
    }

    public void teste(){
        logger.info("Iniciando teste de impressão...");
        try{
            printer.setAlignment(1); // Centralizado
            printer.setFontSize(2, 2); // Dobro de tamanho
            printer.setBold(true); // Negrito
            printer.printText("TÍTULO GRANDE\n");
            
            printer.setFontSize(1, 1); // Volta ao normal
            printer.setBold(false); // Sem Negrito
            printer.setAlignment(0); // Alinhamento à esquerda
            printer.setUnderline(true); //Sublinhado
            printer.printText("Texto normal\n");
            
            printer.setInverseColors(true); 
            printer.printText("Texto invertido\n");
            printer.setInverseColors(false);
            
            // Cortar o papel
            printer.cutPaper();
        } catch (Exception e) {
            logger.error("Erro ao imprimir: {}", e.getMessage());
        }
    }//teste

    @PreDestroy
    public void closeOutputStream() {
        try {
            outputStream.close();
        } catch (IOException e) {
            logger.error("Erro ao fechar o OutputStream: {}", e.getMessage());
        }
    }
    
    private static String centralizar(String texto) {
        if (texto.length() >= COLUNA_TAMANHO) {
            return texto.substring(0, COLUNA_TAMANHO);
        }
        
        int espacos = (COLUNA_TAMANHO - texto.length()) / 2;
        return " ".repeat(espacos) + texto;
    }

    private String truncate(String text, int length) {
        return text.length() > length ? text.substring(0, length) : text;
    }
}