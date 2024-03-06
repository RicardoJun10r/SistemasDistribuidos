package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ProcessosHandler {

    private String login;

    private int porta;

    private InetAddress address;

    public ProcessosHandler(String login, int porta, String address) {
        this.login = login;
        this.porta = porta;
        try {
            if (address.charAt(0) == '/')
                address = address.substring(1);
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public String getLogin() {
        return login;
    }

    public int getPorta() {
        return porta;
    }

    public InetAddress getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "ProcessosHandler [login=" + login + ", porta=" + porta + ", address=" + address + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        result = prime * result + porta;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProcessosHandler other = (ProcessosHandler) obj;
        if (login == null) {
            if (other.login != null)
                return false;
        } else if (!login.equals(other.login))
            return false;
        if (porta != other.porta)
            return false;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        return true;
    }

}
