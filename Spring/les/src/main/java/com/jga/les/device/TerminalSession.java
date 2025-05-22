package com.jga.les.device;

import java.io.*;
import java.net.Socket;
import java.util.Optional;

import com.jga.les.model.Cliente;
import com.jga.les.repository.ClienteRepository;

public class TerminalSession implements Runnable {

    private final Socket socket;
    private final ClienteRepository clienteRepository;
    private final TMT20XService printerService;

    public TerminalSession(Socket socket, ClienteRepository clienteRepository, TMT20XService printerService) {
        this.socket = socket;
        this.clienteRepository = clienteRepository;
        this.printerService = printerService;
    }

    @Override
    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter saida = new PrintWriter(socket.getOutputStream(), true)
        ) {
            limparTela(saida);

            saida.println("Digite o codigo do cliente:");
            saida.println("=CONSULTA DE SALDO=");
            String codigo = entrada.readLine();

            limparTela(saida);

            Optional<Cliente> cliente = clienteRepository.findByCartao(codigo);

            if (cliente != null) {
                saida.println("Cliente encontrado:");
                saida.println("Nome: " + cliente.get().getNome());
                saida.println("Saldo: R$ " + String.format("%.2f", cliente.get().getSaldo()));
                System.out.println("Cliente " + cliente.get().getNome() + " encontrado com saldo: R$ " + cliente.get().getSaldo());

                saida.println();
                saida.println("Deseja imprimir o comprovante? (1/0)");
                String resposta = entrada.readLine();

                if (resposta != null && resposta.trim().equalsIgnoreCase("1")) {
                    printerService.imprimirComprovanteSaldo(cliente);
                    saida.println("Comprovante enviado para impressão.");
                } else {
                    saida.println("Operação finalizada.");
                }
            } else {
                saida.println("Cliente não encontrado.");
            }

        } catch (IOException e) {
            System.err.println("Erro na sessão do terminal: " + e.getMessage());
        }
    }

    private void limparTela(PrintWriter saida) {
        // Envia múltiplos caracteres de nova linha ou form feed (dependendo do terminal)
        saida.print("\u001B[2J\u001B[H"); // ANSI escape code (caso o terminal suporte)
        for (int i = 0; i < 50; i++) {
            saida.println();
        }
        saida.flush();
    }
}
