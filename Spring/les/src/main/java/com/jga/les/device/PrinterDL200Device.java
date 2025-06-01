package com.jga.les.device;

import com.fazecast.jSerialComm.*;
import com.jga.les.service.ProdutoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import java.io.FileOutputStream;
import java.io.OutputStream;

@Component
public class PrinterDL200Device {
    private OutputStream printerStream;
    private static final String NOME_IMPRESSORA = "DL-200Z"; // Nome da impressora, se necessário
    @Autowired
    private ProdutoService produtoService;
    private SerialPort serialPort;
    private PrintService impressora = null;

    private static final String PORTA_SERIAL = "/dev/usb/lp0"; // Altere conforme necessário
    private static final Logger logger = LoggerFactory.getLogger(PrinterDL200Device.class);

    @PostConstruct
    public void init() {
        try {
            String nomeImpressora = NOME_IMPRESSORA;

            // Busca a impressora instalada
            PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

            for (PrintService service : services) {
                System.out.println("Impressora encontrada: " + service.getName());
                if (service.getName().equalsIgnoreCase(nomeImpressora)) {
                    impressora = service;
                    break;
                }
            }

            if (impressora == null) {
                System.out.println("Impressora não encontrada!");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // try {
        //     printerStream = new FileOutputStream(PORTA_SERIAL);
        //     System.out.println("Conectado à impressora: " + PORTA_SERIAL);
            // serialPort = SerialPort.getCommPort(PORTA_SERIAL);
            // serialPort.setBaudRate(9600);
            // serialPort.setNumDataBits(8);
            // serialPort.setNumStopBits(1);
            // serialPort.setParity(SerialPort.NO_PARITY);

            // if (serialPort.openPort(1000)) { // Timeout de 1 segundo
            //     logger.info("Conectado à impressora DL-200 na porta: {}", PORTA_SERIAL);
            // } else {
            //     logger.error("Falha ao conectar na DL-200 na porta: {}", PORTA_SERIAL);
            // }
        // } catch (Exception e) {
        //     System.err.println("Erro na inicialização da impressora: " + e.getMessage());
        // }
    }

    private void sendCommand(byte[] command) throws Exception {
        printerStream.write(command);
        printerStream.flush();
    }

    public void imprimirCodigoBarras(String codigo, Integer quantidade) {
        // Ajustando os códigos para caberem nas 3 etiquetas por linha
        String nomeProduto = "teste";//produtoService.findByCodigo(codigo).getNome(); //DESCOMENTAR QUANDO FOR APLICAR
        StringBuilder zpl = new StringBuilder("^XA\n");

        zpl.append("^MD30  ; Aumenta a temperatura\n");
        zpl.append("^PR2,2,2  ; Diminui a velocidade para evitar falhas\n");

        // Configuração de posição
        int posicaoX = 40;
        int posicaoY = -15;
        int larguraEtiqueta = 280; // Distância entre etiquetas
        int alturaLinha = 0; // Distância entre linhas

        for (int i = 0; i < quantidade; i++) {
            int coluna = i % 3; // 0, 1 ou 2 (posição da etiqueta na linha)
            int linha = i / 3;  // Muda para nova linha quando necessário

            int x = posicaoX + (coluna * larguraEtiqueta);
            int y = posicaoY + (linha * alturaLinha); // Distância ajustada para evitar sobreposição

            // Nome do produto
            zpl.append("^FO").append(x).append(",").append(y)
                    .append("^A0N,40,40^FD").append(nomeProduto).append("^FS\n");

            // Código de barras com largura reduzida
            zpl.append("^FO").append(x).append(",").append(y + 70)
                    .append("^BY1.5^BCN,60,Y,N,N^FD").append(codigo).append("^FS\n");
        }

        zpl.append("^XZ");

        // Criando o documento para impressão
        DocPrintJob job = impressora.createPrintJob();
        Doc doc = new SimpleDoc(zpl.toString().getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
        try {
            job.print(doc, null);
        } catch (PrintException e) {
            e.printStackTrace();
        }
        System.out.println("Impressão enviada com sucesso!");

        // if (serialPort == null || !serialPort.isOpen()) {
        //     System.err.println("Impressora não disponível");
        //     return;
        // }

        // try (OutputStream os = serialPort.getOutputStream()) {
        //     // Reset da impressora
        //     os.write(new byte[]{0x1B, 0x40});
            
        //     // Configuração do código de barras (Code128)
        //     os.write(new byte[]{0x1D, 0x48, 0x02}); // Exibir valor legível abaixo
        //     os.write(new byte[]{0x1D, 0x68, 0x64}); // Altura 100
        //     os.write(new byte[]{0x1D, 0x77, 0x03}); // Largura 3
            
        //     // Comando de impressão de código de barras (Code128)
        //     os.write(new byte[]{0x1D, 0x6B, 0x49}); // GS k 73 (Code128)
        //     os.write((byte) (codigo.length() + 2)); // Tamanho dos dados + 2
        //     os.write('{'); // Iniciar com '{C'
        //     os.write('C');
        //     os.write(codigo.getBytes()); // Dados do código
            
        //     // Cortar papel (opcional - requer faca acoplada)
        //     os.write(new byte[]{0x1D, 0x56, 0x41, 0x00}); // Cortar parcial
            
        //     os.flush();
        //     System.out.println("Código de barras enviado para impressão: " + codigo);
        // } catch (Exception e) {
        //     System.err.println("Erro ao imprimir: " + e.getMessage());
        // }
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (printerStream != null) {
                printerStream.close();
                System.out.println("Conexão fechada");
            }
        } catch (Exception e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        } finally {
            printerStream = null;
        }
    }

}
