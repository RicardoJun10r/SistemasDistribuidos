package anel;

public class MainProcesso4 {
    
    static int porta_propria = 1028;
    static int porta_destino = 1025;

    static String endereco = "127.0.0.1";

    static String login = "p4";

    public static void main(String[] args) {
        Processo processo = new Processo(login, porta_propria, endereco, porta_destino,
        endereco);
        processo.start();
    }

}
