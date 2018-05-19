package org.edraens.monitoring;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
* Ping
* Check ping connectivity to a host
*/
public class Ping {
    
    private int timeout;
    private String host;
    private String name;
    
    public Ping(String host, String name, int timeout){
        this.host = host;
        this.name = name;
        this.timeout = timeout;
    }
    
    public boolean getStatus(){
        try {
            InetAddress ip = InetAddress.getByName(host);
            if(ip.isReachable(timeout*1000)) return true;
            else return false;
		} catch (UnknownHostException e) {
            // System.err.println(host+ " : DNS query failed");
			return false;
        } catch (IOException e) {
            e.printStackTrace();
			return false;
		}
    }
    
    public String getName(){
        return name;
    }
    
    public String getHost(){
        return host;
    }
    
}