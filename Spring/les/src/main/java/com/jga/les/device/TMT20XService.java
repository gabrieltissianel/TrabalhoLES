package com.jga.les.device;
import com.jga.les.repository.ClienteRepository;

import jakarta.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jga.les.model.Cliente;

import javax.print.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

@Service
public class TMT20XService {
    private final ClienteRepository clienteRepository;
    private static final Logger logger = LoggerFactory.getLogger(TMT20XService.class);
    private OutputStream outputStream;
    private TMT20XPrinter printer;

    public TMT20XService(ClienteRepository clienteRepository) throws IOException {
        if(outputStream != null) {
            outputStream.close();
        }
        try {
            this.outputStream = new FileOutputStream("/dev/usb/lp0");
            this.printer = new TMT20XPrinter(outputStream);
        } catch (Exception e) {
            logger.error("Erro ao inicializar TMT20XService: {}", e.getMessage());
            this.outputStream.close();
        }
        this.clienteRepository = clienteRepository;
    }

    public void listarImpressoras() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("Impressoras encontradas no sistema:");
        for (PrintService service : services) {
            System.out.println(" - " + service.getName());
        }
    }

    public void imprimirComprovanteSaldo(String cartao) {
        Optional<Cliente> clienteO;
        Cliente cliente;
        clienteO = clienteRepository.findByCartao(cartao);
        System.out.println(clienteO.get().getNome());
        if(clienteO.isPresent()){
            cliente = clienteO.get();
        } else {
            logger.error("Cliente não encontrado com o cartão: {}", cartao);
            throw new RuntimeException("Cliente não encontrado com o cartão: " + cartao);
        }
        try {
            printer.setAlignment(1); // Centralizado
            printer.setFontSize(2, 2); // Dobro de tamanho
            printer.setBold(true); // Negrito
            printer.printText("==COMPROVANTE DO SALDO==");

            printer.setFontSize(1, 1); // Volta ao normal
            printer.setAlignment(0); // Esquerda
            printer.setBold(false); // Tira o Negrito
            printer.printText("Cliente: " + cliente.getNome() + "\n");
            printer.printText("Codigo: " + cliente.getCartao() + "\n");
            printer.printText("Saldo: R$ " + String.format("%.2f", cliente.getSaldo()) + "\n");
            printer.printText("-------------------------\n");
            printer.cutPaper(); // Cortar o papel;
        } catch (Exception e) {
            logger.error("Erro ao imprimir informações do Cliente: {}", e.getMessage());
        } 
    }

    public void teste(){
        logger.info("Iniciando teste de impressão...");
        try{
    
            // Exemplo de impressão customizada
            printer.setAlignment(1); // Centralizado
            printer.setFontSize(2, 2); // Dobro de tamanho
            printer.setBold(true);
            printer.printText("TÍTULO GRANDE\n");
            
            printer.setFontSize(1, 1); // Volta ao normal
            printer.setBold(false);
            printer.setAlignment(0); // Alinhamento à esquerda
            printer.setUnderline(true);
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