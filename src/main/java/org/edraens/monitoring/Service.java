package org.edraens.monitoring;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.validator.routines.InetAddressValidator;

/**
* Service
*/
public class Service {
    
    private String host;
    private String name;
    private int port;
    private int timeout;
    
    public Service(String host, String name, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.timeout = timeout;
    }
    
    public boolean getStatus() {
        boolean result = false;
        try {
            InetAddress ip = InetAddress.getByName(host);
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip.getHostAddress(), port), timeout*1000);
            socket.close();
            result = true;
        } catch (IOException e) {
            result = false;
        }
        return result;
    }
    
    public String getName() {
        return name;
    }
    
    public int getPort() {
        return port;
    }
    
    public String getHost() {
        return host;
    }
    
}