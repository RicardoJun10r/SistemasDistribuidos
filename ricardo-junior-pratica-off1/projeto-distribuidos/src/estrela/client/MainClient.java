package client;

public class MainClient {
    
    public static void main(String[] args) {
        try {
            ClientConfig clientConfig = new ClientConfig();
            clientConfig.start();            
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Cliente finalizado!");
    }
}
