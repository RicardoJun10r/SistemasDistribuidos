package util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class SocketUDP {
    
    private final int BUFFER_SIZE = 1024;
    
    private DatagramSocket socket;

    private int PORTA_PROPRIA;

    private int PORTA_DESTINO;

    private InetAddress address_proprio;

    private InetAddress address_destino;

    private byte[] buffer_entrada;

    private String login;

    public SocketUDP(String login, int PORTA_PROPRIA, int PORTA_DESTINO, String address_proprio, String address_destino) throws SocketException, UnknownHostException{
        this.login = login;
        this.PORTA_PROPRIA = PORTA_PROPRIA;
        this.PORTA_DESTINO = PORTA_DESTINO;
        this.socket = new DatagramSocket(PORTA_PROPRIA);
        this.address_proprio = InetAddress.getByName(address_proprio);
        this.address_destino = InetAddress.getByName(address_destino);
        this.buffer_entrada = new byte[this.BUFFER_SIZE];
    }

    public SocketUDP(String login, int PORTA_PROPRIA, String address_proprio) throws SocketException, UnknownHostException{
        this.login = login;
        this.PORTA_PROPRIA = PORTA_PROPRIA;
        this.socket = new DatagramSocket(PORTA_PROPRIA);
        this.address_proprio = InetAddress.getByName(address_proprio);
    }

    public void enviar(){
        DatagramPacket datagramPacket = new DatagramPacket(this.buffer_entrada, this.buffer_entrada.length, this.address_destino, this.PORTA_DESTINO);
        try {
            this.socket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receber(){
        DatagramPacket packet = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
        try {
            this.socket.receive(packet);
            String response = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
            packet.setData(new byte[BUFFER_SIZE]);
            packet.setLength(BUFFER_SIZE);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public int getPORTA_PROPRIA() {
        return PORTA_PROPRIA;
    }

    public void setPORTA_PROPRIA(int pORTA_PROPRIA) {
        this.PORTA_PROPRIA = pORTA_PROPRIA;
    }

    public int getPORTA_DESTINO() {
        return PORTA_DESTINO;
    }

    public void setPORTA_DESTINO(int pORTA_DESTINO) {
        this.PORTA_DESTINO = pORTA_DESTINO;
    }

    public byte[] getBuffer_entrada() {
        return buffer_entrada;
    }

    public void setBuffer_entrada(byte[] buffer_entrada) {
        this.buffer_entrada = buffer_entrada;
    }

    public InetAddress getAddress_proprio() {
        return address_proprio;
    }

    public void setAddress_proprio(String address_proprio) {
        try {
            this.address_proprio = InetAddress.getByName(address_proprio);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getAddress_destino() {
        return address_destino;
    }

    public void setAddress_destino(String address_destino) {
        try {
            this.address_destino = InetAddress.getByName( address_destino );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void close(){
        this.socket.close();
    }

}
