package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

import util.ClientSocket;

public class ServerConfig {

    public final static int PORTA = 1025;

    private ServerSocket serverSocket;

    private final List<ClientSocket> USUARIOS = new LinkedList<>();

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORTA);
        System.out.println("Iniciando servidor na porta = " + PORTA);
        clientConnectionLoop();
    }

    private void clientConnectionLoop() throws IOException {
        while (true) {
            ClientSocket clientSocket = new ClientSocket(this.serverSocket.accept());
            USUARIOS.add(clientSocket);
            new Thread(() -> {
                try {
                    clientMessageLoop(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private ClientSocket getClient(String resolve){
        System.out.println("Resolve: " + resolve);
        return this.USUARIOS.stream().filter(user -> user.getSocketAddress().toString().equals(resolve)).findFirst().get();
    }

    private void clientMessageLoop(ClientSocket clientSocket) throws IOException {
        String mensagem;
        try {
            while ((mensagem = clientSocket.getMessage()) != null) {
                switch (mensagem.split(";")[0]) {
                    case "unicast":
                    System.out.println(
                        "Mensagem de " + clientSocket.getSocketAddress() + ": " + mensagem);
                        unicast(getClient(mensagem.split(";")[1]), clientSocket.getSocketAddress().toString() + ": " + mensagem.split(";")[2]);
                        break;
                    case "broadcast":
                        System.out.println(
                                "Mensagem de " + clientSocket.getSocketAddress() + ": " + mensagem);
                        broadcast(clientSocket, clientSocket.getSocketAddress().toString() + ": " + mensagem.split(";")[1]);
                        break;
                    case "sair":
                        break;
                    default:
                        break;
                }
                if ("sair".equalsIgnoreCase(mensagem))
                    return;
            }
        } finally {
            clientSocket.close();
        }
    }

    private void broadcast(ClientSocket emissor, String mensagem) {
        this.USUARIOS.stream()
                .filter(user -> !user.getSocketAddress().equals(emissor.getSocketAddress()))
                .forEach(user -> user.sendMessage(mensagem));
    }

    private void unicast(ClientSocket destinario, String mensagem) {
        ClientSocket emissor = this.USUARIOS.stream()
                .filter(user -> user.getSocketAddress().equals(destinario.getSocketAddress()))
                .findFirst().get();
        emissor.sendMessage(mensagem);
    }

}
