package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import util.ClientSocket;

public class ServerConfig {
    
    public final static int PORTA = 1025;

    private ServerSocket serverSocket;

    private final List<ClientSocket> USUARIOS = new LinkedList<>();

    public void start() throws IOException{
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

    private void clientMessageLoop(ClientSocket clientSocket) throws IOException{
        String mensagem;
        try {
            while ((mensagem = clientSocket.getMessage()) != null) {
                if("sair".equalsIgnoreCase(mensagem)) return;
                System.out.println(
                    "Mensagem de "  + clientSocket.getSocketAddress() + ": " + mensagem
                );
                broadcast(clientSocket, mensagem);
            }
        } finally {
            clientSocket.close();
        }
    }

    private void broadcast(ClientSocket emissor, String mensagem){
        Iterator<ClientSocket> iterator = this.USUARIOS.iterator();
        while (iterator.hasNext()){
            ClientSocket i = iterator.next();
            if(!emissor.equals(i)){
                if(!i.sendMessage(emissor.getSocketAddress() + ": " + mensagem))
                    iterator.remove();
            }
        }
    }

}
