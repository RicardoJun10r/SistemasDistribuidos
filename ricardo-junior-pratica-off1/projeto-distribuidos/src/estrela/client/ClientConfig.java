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

    public ClientConfig() {
        this.scan = new Scanner(System.in);
    }

    @Override
    public void run() {
        String mensagem;
        while ((mensagem = this.clientSocket.getMessage()) != null) {
            System.out.println(
                    "Mensagem do servidor: " + mensagem);
        }
    }

    private void menu() {
        System.out.println("\\> 1 Enviar mensagem para todos\n\\> 2 Enviar mensagem para alguém\n\\> 3 sair");
    }

    private void processOption(String op) {
        String mensagem;
        switch (op) {
            case "1":
                this.scan.nextLine();
                System.out.println("\\> Escreva:");
                System.out.print("\\> ");
                mensagem = this.scan.nextLine();
                enviar("broadcast;" + mensagem);
                break;
            case "2":
                System.out.println("\\> Endereco (ex: 127.0.0.1)");
                System.out.print("\\> ");
                String endereco = this.scan.next();
                System.out.println("\\> Porta (ex: 8080)");
                System.out.print("\\> ");
                String porta = this.scan.next();
                System.out.println("\\> Escreva");
                this.scan.nextLine();
                System.out.print("\\> ");
                mensagem = this.scan.nextLine();
                enviar("unicast;/" + endereco + ":" + porta + ";" + mensagem);
                break;
            case "3":
                enviar("sair");
                System.out.println("SAINDO");
                break;
            default:
                System.out.println("COMANDO NÃO RECONHECIDO");
                break;
        }
    }

    private void messageLoop() {
        String opcao = "";
        try {
            do {
                Thread.sleep(300);
                menu();
                System.out.print("\\> ");
                opcao = scan.next();
                processOption(opcao);
            } while (!opcao.equalsIgnoreCase("3"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void enviar(String msg) {
        this.clientSocket.sendMessage(msg);
    }

    public void start() throws IOException, UnknownHostException {
        try {
            clientSocket = new ClientSocket(
                    new Socket(ENDERECO_SERVER, ServerConfig.PORTA));
            System.out.println("Cliente conectado ao servidor de endereço = " + ENDERECO_SERVER + " na porta = "
                    + ServerConfig.PORTA);
            new Thread(this).start();
            messageLoop();
        } finally {
            clientSocket.close();
        }
    }

}