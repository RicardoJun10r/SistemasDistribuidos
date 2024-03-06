package server;

import java.io.IOException;

public class MainServer {
    
    public static void main(String[] args) {
        try {
            ServerConfig serverConfig = new ServerConfig();
            serverConfig.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Servidor finalizado!");
    }
}
