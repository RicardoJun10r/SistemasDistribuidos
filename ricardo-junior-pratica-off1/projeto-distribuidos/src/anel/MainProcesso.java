package anel;

public class MainProcesso {

    static int porta_propria = 1025;
    static int porta_destino = 1026;

    static String endereco = "127.0.0.1";

    static String login = "p1";

    public static void main(String[] args) {
        Processo processo = new Processo(login, porta_propria, endereco, porta_destino,
        endereco);
        processo.start();
    }
}
