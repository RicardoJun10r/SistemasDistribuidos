package anel;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import util.ProcessosHandler;
import util.SocketUDP;

public class Processo implements Runnable {

    private SocketUDP socket;

    private List<ProcessosHandler> processos;

    private final Scanner scan;

    public Processo(String login, int PORTA, String ENDERECO) {
        try {
            this.socket = new SocketUDP(login, PORTA, ENDERECO);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        this.processos = new ArrayList<>();
        this.scan = new Scanner(System.in);
    }

    public Processo(String login, int PORTA_PROPRIA, String ENDERECO_PROPRIO, int PORTA_DESTINO,
            String ENDERECO_DESTINO) {
        try {
            this.socket = new SocketUDP(login, PORTA_PROPRIA, PORTA_DESTINO, ENDERECO_PROPRIO, ENDERECO_DESTINO);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        this.processos = new ArrayList<>();
        this.scan = new Scanner(System.in);
    }

    private void printMessage(String msg) {
        Arrays.stream(msg.split(";")).skip(2).forEach(p -> System.out.print(p + " "));
    }

    private void listarProcessos() {
        if (this.processos.isEmpty())
            return;
        else
            this.processos
                    .stream()
                    .forEach(p -> System.out.println(p.getLogin() + " => " + p.getAddress() + ":" + p.getPorta()));
    }

    private void menu() {
        System.out.println("############### MENU ###############");
        System.out.println("OPÇÕES");
        System.out.println("[0] > CONECTAR-SE A UM PROCESSO");
        System.err.println("[1] > MANDAR MENSAGEM A UM PROCESSO");
        System.out.println("[2] > MANDAR MENSAGEM PARA TODOS");
        System.out.println("[3] > SCANEAR REDE");
        System.out.println("[4] > SAIR");
        System.out.println("####################################");
    }

    private void unicast() {
        String msg = "unicast;";
        System.out.println("Qual o login ?");
        msg += this.scan.next();
        msg += ";" + sendMessage();
        send(msg);
    }

    private void broadcast() {
        send("broadcast;" + this.socket.getLogin() + ";" + sendMessage());
    }

    private void connectTo() {
        System.out.println("Porta destino (ex: 8080)");
        this.socket.setPORTA_DESTINO(this.scan.nextInt());
        System.out.println("Endereço destino (ex: 127.0.0.1)");
        this.socket.setAddress_destino(this.scan.next());
    }

    private String sendMessage() {
        this.scan.nextLine();
        System.out.println("Escreva a mensagem:");
        String mensagem = this.scan.nextLine();
        return mensagem;
    }

    private void scanerRede() {
        String ping = "ping" + ";" + this.socket.getAddress_proprio() + ";" + this.socket.getPORTA_PROPRIA() + ";"
                + this.socket.getLogin();
        send(ping);
    }

    private void send(String msg) {
        this.socket.setBuffer_entrada(msg.getBytes());
        this.socket.enviar();
    }

    private void buildNet(String msg) {
        String[] net = msg.split(";");
        for (int i = 4; i <= net.length; i += 3) {
            ProcessosHandler processosHandler = new ProcessosHandler(net[i - 1], Integer.parseInt(net[i - 2]),
                    net[i - 3]);
            if (!this.processos.contains(processosHandler))
                this.processos.add(processosHandler);
        }
    }

    @Override
    public void run() {
        while (true) {
            String res = this.socket.receber();
            if (res != null) {
                if (res.contains("ping")) {
                    if (res.split(";")[3].equals(this.socket.getLogin())) {
                        buildNet(res);
                    } else {
                        res += ";" + this.socket.getAddress_proprio() + ";" + this.socket.getPORTA_PROPRIA() + ";"
                                + this.socket.getLogin();
                        send(res);
                    }
                } else if (res.split(";")[0].equals("unicast")) {
                    if (res.split(";")[1].equals(this.socket.getLogin())) {
                        System.out.println("##########################");
                        System.out.println("Unicast:");
                        printMessage(res);
                        System.out.println("\n##########################");
                    } else {
                        send(res);
                    }
                } else if (res.split(";")[0].equals("broadcast")) {
                    if (!res.split(";")[1].equals(this.socket.getLogin())) {
                        System.out.println("##########################");
                        System.out.println("Broadcast:");
                        printMessage(res);
                        System.out.println("\n##########################");
                        send(res);
                    }
                }
            }
        }
    }

    private void serverLoop() {
        String opcao = "";
        do {
            try {
                Thread.sleep(300);
                listarProcessos();
                menu();
                opcao = this.scan.next();
                switch (opcao) {
                    case "0":
                        connectTo();
                        break;
                    case "1":
                        unicast();
                        break;
                    case "2":
                        broadcast();
                        break;
                    case "3":
                        scanerRede();
                        break;
                    case "4":
                        System.out.println("SAINDO");
                        this.scan.close();
                        this.socket.close();
                        break;
                    default:
                        System.out.println("Erro!");
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!opcao.equals("4"));
    }

    public void start() {
        System.out.println("INICIALIZANDO!!!");
        new Thread(this).start();
        new Thread(() -> serverLoop()).start();
    }

}