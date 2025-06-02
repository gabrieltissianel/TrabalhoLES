package com.jga.les.device;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;

import org.springframework.http.ResponseEntity;

import com.jga.les.model.Cliente;
import com.jga.les.repository.ClienteRepository;

public class TerminalSession implements Runnable {

    private final Socket socket;
    private final ClienteRepository clienteRepository;

    public TerminalSession(Socket socket, ClienteRepository clienteRepository) {
        this.socket = socket;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run() {
       try (
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true)
        ) {
            limparTela(saida);
            saida.println("Codigo do cliente:  ");

            StringBuilder codigo = new StringBuilder();
            Reader leitorCaracteres = new InputStreamReader(socket.getInputStream());
            
            // Ler caractere por caractere
            while (true) {
                int caractereInt = leitorCaracteres.read();
                if (caractereInt == -1) break; // Fim do stream
                
                char c = (char) caractereInt;
                
                // Quebra ao pressionar Enter
                if (c == '\r' || c == '\n') {
                    saida.println(); // Nova linha após entrada
                    break;
                }
                // Tratar backspace (ASCII 8 ou 127)
                else if (caractereInt == 8 || caractereInt == 127) {
                    if (codigo.length() > 0) {
                        // Remove último caractere
                        codigo.deleteCharAt(codigo.length() - 1);
                        // Apaga da tela: Backspace + Espaço + Backspace
                        saida.print("\b \b");
                    }
                }
                // Caracteres normais
                else if (!Character.isISOControl(c)) {
                    codigo.append(c);
                    saida.print('*'); // Exibe asterisco (ou substitua por c para mostrar o caractere)
                }
                saida.flush(); // Força o envio imediato
            }

            limparTela(saida);
            Cliente cliente = clienteRepository.findByCartao(codigo.toString());
            printSaldoCliente(cliente, saida);
        } catch (IOException e) {
            System.err.println("Erro na sessão do terminal: " + e.getMessage());
        } 
    }


    public ResponseEntity<String> exibeSaldo(String cartao){
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter saida = new PrintWriter(socket.getOutputStream(), true)
        ) {
            limparTela(saida);

            Cliente cliente = clienteRepository.findByCartao(cartao);
            if (cliente != null) {
                saida.println("Cliente encontrado:\n");
                quebraLinha(saida);
                saida.println("Nome: " + cliente.getNome()+"\n");
                quebraLinha(saida);
                saida.println("Saldo: R$ " + String.format("%.2f", cliente.getSaldo()));
                System.out.println("Cliente " + cliente.getNome() + " encontrado com saldo: R$ " + cliente.getSaldo());
            } else {
                saida.println("Cliente inexistente.");
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok("OK");
        } catch (IOException e) {
            System.err.println("Erro na sessão do terminal: " + e.getMessage());
        }
        return ResponseEntity.badRequest().body("Erro na sessao do terminal");
    }

    private void printSaldoCliente(Cliente cliente, PrintWriter saida){
        if (cliente != null) {
            saida.println("Cliente encontrado:\n");
            quebraLinha(saida);
            saida.println("Nome: " + cliente.getNome()+"\n");
            quebraLinha(saida);
            saida.println("Saldo: R$ " + String.format("%.2f", cliente.getSaldo()));
            System.out.println("Cliente " + cliente.getNome() + " encontrado com saldo: R$ " + cliente.getSaldo());
        } else {
            saida.println("Cliente inexistente.");
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void limparTela(PrintWriter saida) {
        // Envia múltiplos caracteres de nova linha ou form feed (dependendo do terminal)
        saida.print("\u001B[2J\u001B[H"); // ANSI escape code (caso o terminal suporte)
        // for (int i = 0; i < 50; i++) {
        //     saida.println();
        // }
        saida.flush();
    }

    private void quebraLinha(PrintWriter saida) {
        saida.print("\r\n");       // Quebra de linha (CRLF)
    }
}
