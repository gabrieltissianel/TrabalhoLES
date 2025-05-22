package com.jga.les.device;

import java.io.OutputStream;

public class TMT20XPrinter {
    private OutputStream outputStream;

    public TMT20XPrinter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    // Método básico para enviar comandos brutos
    private void sendCommand(byte[] command) throws Exception {
        outputStream.write(command);
        outputStream.flush();
    }

    // Método para enviar texto simples
    public void printText(String text) throws Exception {
        sendCommand(text.getBytes("ISO-8859-1"));
    }

    // Método para pular linhas
    public void feedLines(int lines) throws Exception {
        sendCommand(new byte[]{0x1B, 0x64, (byte) lines});
    }

    // Método para cortar o papel
    public void cutPaper() throws Exception {
        // Cortar papel (comando completo para cortar)
        sendCommand(new byte[]{0x1D, 0x56, 0x41, 0x10});
        feedLines(5); // Pular algumas linhas antes de cortar
    }

    // Método para definir o tamanho da fonte
    public void setFontSize(int width, int height) throws Exception {
        // 0x00 = tamanho normal
        // Valores de 1 a 8 para width e height (0x11 a 0x88)
        byte size = (byte) (((width - 1) << 4) | (height - 1));
        sendCommand(new byte[]{0x1D, 0x21, size});
    }

    // Método para texto em negrito
    public void setBold(boolean bold) throws Exception {
        sendCommand(new byte[]{0x1B, 0x45, (byte) (bold ? 1 : 0)});
    }

    // Método para texto sublinhado
    public void setUnderline(boolean underline) throws Exception {
        sendCommand(new byte[]{0x1B, 0x2D, (byte) (underline ? 1 : 0)});
    }

    // Método para alinhamento do texto
    public void setAlignment(int alignment) throws Exception {
        // 0 = esquerda, 1 = centro, 2 = direita
        sendCommand(new byte[]{0x1B, 0x61, (byte) alignment});
    }

    // Método para inverter cores (texto branco em fundo preto)
    public void setInverseColors(boolean inverse) throws Exception {
        sendCommand(new byte[]{0x1D, 0x42, (byte) (inverse ? 1 : 0)});
    }

    // Método para rotacionar texto 90 graus
    public void setRotate90(boolean rotate) throws Exception {
        sendCommand(new byte[]{0x1B, 0x56, (byte) (rotate ? 1 : 0)});
    }

    // Método para definir espaçamento entre linhas
    public void setLineSpacing(int spacing) throws Exception {
        sendCommand(new byte[]{0x1B, 0x33, (byte) spacing});
    }

    // Método para imprimir código de barras
    public void printBarcode(String data, int barcodeType) throws Exception {
        // Tipos comuns: 0x41 = UPC-A, 0x42 = UPC-E, 0x43 = EAN13, 0x44 = EAN8
        sendCommand(new byte[]{0x1D, 0x6B, (byte) barcodeType});
        sendCommand(data.getBytes("ISO-8859-1"));
        sendCommand(new byte[]{0x00}); // Terminador
    }

    // Método para imprimir QR Code
    public void printQRCode(String data) throws Exception {
        // Definir tamanho do QR (1-16)
        sendCommand(new byte[]{0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x43, 0x08});
        
        // Definir correção de erro (L=0x30, M=0x31, Q=0x32, H=0x33)
        sendCommand(new byte[]{0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x45, 0x31});
        
        // Enviar dados
        int len = data.length() + 3;
        sendCommand(new byte[]{0x1D, 0x28, 0x6B, (byte) (len % 256), (byte) (len / 256), 0x31, 0x50, 0x30});
        sendCommand(data.getBytes("ISO-8859-1"));
        
        // Imprimir QR
        sendCommand(new byte[]{0x1D, 0x28, 0x6B, 0x03, 0x00, 0x31, 0x51, 0x30});
    }

    // Método para imprimir imagem (bitmap monocromático)
    public void printImage(byte[] imageData, int width) throws Exception {
        // Largura em bytes (width deve ser múltiplo de 8)
        int widthBytes = (width + 7) / 8;
        int height = imageData.length / widthBytes;
        
        // Comando para imagem bitmap
        sendCommand(new byte[]{0x1D, 0x76, 0x30, 0x00, 
                              (byte) (widthBytes % 256), (byte) (widthBytes / 256),
                              (byte) (height % 256), (byte) (height / 256)});
        
        sendCommand(imageData);
    }
}
