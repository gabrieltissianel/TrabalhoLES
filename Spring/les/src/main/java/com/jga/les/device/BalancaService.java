package com.jga.les.device;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import jakarta.annotation.PreDestroy;

@Component
public class BalancaService {
    private static SerialPort serialPort;
    private static String portName = "/dev/ttyUSB"; 
    private static boolean portaAberta = false;
    private static final Logger logger = LoggerFactory.getLogger(BalancaService.class);
    private static Double ultimoPeso; // Armazena o último peso

    static {
        init();
    }

    /*init class clancadatalistener */
    private class BalancaDataListener implements SerialPortDataListener {
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) return;

            System.out.println(serialPort.bytesAvailable());
            byte[] newData = new byte[serialPort.bytesAvailable()];
            int numRead = serialPort.readBytes(newData, newData.length);
            if (numRead > 0) {
                buffer.write(newData, 0, numRead);
                processBuffer();
            }else {
                ultimoPeso = 0.0;
            }

        }

        private void processBuffer() {
            byte[] data = buffer.toByteArray();
            int i = 0;
            int dataLength = data.length;

            while (i < dataLength) {
                if (data[i] == 0x02) { // STX encontrado
                    if (i + 7 < dataLength) { // Verifica se há bytes suficientes
                        if (data[i + 7] == 0x0D) { // Verifica CR
                            // Extrai os 6 bytes do peso
                            byte[] weightBytes = Arrays.copyOfRange(data, i + 1, i + 7);
                            String weightStr = new String(weightBytes, StandardCharsets.US_ASCII);
                            try {
                                double weight = Double.parseDouble(weightStr);
                                logger.info("Peso lido: {}", weight);
                                ultimoPeso = weight; // Atualiza a variável da classe SerialService
                            } catch (NumberFormatException e) {
                                logger.error("Formato inválido do peso: {}", weightStr, e);
                            }
                            i += 8; // Avança para o próximo pacote
                            continue;
                        } else {
                            i++; // CR inválido, avança
                        }
                    } else {
                        break; // Dados insuficientes, aguarda mais
                    }
                }
                i++;
            }

            // Mantém bytes não processados para a próxima leitura
            byte[] remaining = Arrays.copyOfRange(data, i, dataLength);
            buffer.reset();
            buffer.write(remaining, 0, remaining.length);
        }
    }
    /*end class clancadatalistener */

    private static boolean openningPort(String port){
        try {
            System.out.println("Inicializando BalancaService...");
            serialPort = SerialPort.getCommPort(port);

            serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
            portaAberta = serialPort.openPort();
            logger.info("Porta {} aberta com sucesso.", port);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean init() {
        for(int i=0; i<4; i++){// testa 4 portas seriais
            if(openningPort(portName+i)){
                return true;
            }
        }
        logger.error("Nenhuma porta serial disponível.");
        return false;
    }

    // Método para fechar a porta manualmente
    public void closePort() {
        if (serialPort != null && serialPort.isOpen()) {
            boolean closed = serialPort.closePort();
            if (closed) {
                logger.info("Porta {} fechada com sucesso.", serialPort.getSystemPortName());
            } else {
                logger.error("Falha ao fechar a porta {}", serialPort.getSystemPortName());
            }
        }
    }

    // Fecha automaticamente ao destruir o bean Spring
    @PreDestroy
    public void onShutdown() {
        closePort();
    }

    public Double getUltimoPeso() {
        BalancaDataListener listener = new BalancaDataListener();
        listener.serialEvent(new SerialPortEvent(serialPort, SerialPort.LISTENING_EVENT_DATA_AVAILABLE));
        return ultimoPeso;
    }

}
