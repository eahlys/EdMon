package org.edraens.monitoring;

import java.io.IOException;
import java.net.Socket;

/**
* Service
*/
public class Service {
    
    private String host;
    private String name;
    private int port;
    
    public Service(String host, String name, int port) {
        this.host = host;
        this.port = port;
        this.name = name;
    }
    
    public boolean getStatus() {
        boolean result = false;
        try {
            (new Socket(host, port)).close();
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