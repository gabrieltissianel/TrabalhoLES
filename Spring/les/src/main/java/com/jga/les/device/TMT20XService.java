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

import javax.print.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

@Component
public class TMT20XService {
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private CompraProdutoService compraProdutoService;

    private static final Logger logger = LoggerFactory.getLogger(TMT20XService.class);
    private OutputStream outputStream;
    private TMT20XPrinter printer;
    private NumberFormat formatter = new DecimalFormat("#0.00");     

    public TMT20XService(ClienteRepository clienteRepository){
        try {
            outputStream.close();
        } catch (Exception e) {
            // Nao faz nada
        }
        
        try {
            this.outputStream = new FileOutputStream("/dev/usb/lp0");
            this.printer = new TMT20XPrinter(outputStream);
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

    public void imprimirComprovanteCompra(Compra compra) throws Exception {
        Cliente cliente = compra.getCliente();
        List<CompraProduto> produtos = compra.getCompraProdutos();

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
        printer.printText("Cliente: " + cliente.getNome() + "\n");
        printer.printText("Produtos:\n");

        if(produtos.isEmpty()){
            printer.printText("N/A.\n");
        }else{
            for (CompraProduto produto : produtos) {
                printer.printText(" - " + produto.getProduto().getNome() + ": \tR$" + produto.getPreco() + "\t" + produto.getQntd() + (produto.getProduto().isUnitario() ? "\n" : "Kg\n"));
            }
            // Total
            printer.setBold(true); // Negrito
        }
        printer.printText("Total: " + formatter.format(compra.getTotal()) + "\n");
        printer.cutPaper();
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
            printer.setAlignment(1); // Alinhamento à esquerda
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
            
            if(produtos.isEmpty()){
                printer.printText("N/A.\n");
            }else{
                for (CompraProduto produto : produtos) {
                    printer.printText(" - " + produto.getProduto().getNome() + ": \tR$" + produto.getPreco() + "\t" + produto.getQntd() + (produto.getProduto().isUnitario() ? "\n" : "Kg\n"));
                }
                // Total
                printer.setBold(true); // Negrito
            }
            printer.printText("Total: " + formatter.format(compra.getTotal()) + "\n");
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
}