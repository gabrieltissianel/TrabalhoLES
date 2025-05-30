package com.jga.les.device;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import com.jga.les.repository.ClienteRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TerminalServer {
    private static final int PORTA = 5000;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final ClienteRepository clienteRepository;

    public TerminalServer(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @PostConstruct
    public void iniciar() {
        threadPool.execute(() -> {
            try (ServerSocket server = new ServerSocket(PORTA)) {
                System.out.println("Servidor de terminais iniciado na porta " + PORTA);

                while (!Thread.currentThread().isInterrupted()) {
                    Socket socket = server.accept();
                    System.out.println("Novo terminal conectado: " + socket.getInetAddress());
                    threadPool.execute(new TerminalSession(socket, clienteRepository));
                }
            } catch (IOException e) {
                System.err.println("Erro no servidor: " + e.getMessage());
            }
        });
    }
}