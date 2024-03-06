package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import server.ServerConfig;
import util.ClientSocket;

public class ClientConfig implements Runnable {

    private final String ENDERECO_SERVER = "localhost";

    private ClientSocket clientSocket;

    private Scanner scan;

    public ClientConfig(){
        this.scan = new Scanner(System.in);
    }

    @Override
    public void run(){
        String mensagem;
        while ((mensagem = this.clientSocket.getMessage()) != null) {
            System.out.println(
                "Mensagem do servidor: " + mensagem
            );
        }
    }

    private void messageLoop(){
        String mensagem;
        try {
            do {
                Thread.sleep(300);
                System.out.print("Digite mensagem: ");
                mensagem = scan.nextLine();
                this.clientSocket.sendMessage(mensagem);
            } while (!mensagem.equalsIgnoreCase("sair"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException, UnknownHostException{
        try {
            clientSocket = new ClientSocket(
                new Socket(ENDERECO_SERVER, ServerConfig.PORTA)
            );
            System.out.println("Cliente conectado ao servidor de endereço = " + ENDERECO_SERVER + " na porta = " + ServerConfig.PORTA);
            new Thread(this).start();
            messageLoop();
        } finally {
            clientSocket.close();
        }
    }
    
}