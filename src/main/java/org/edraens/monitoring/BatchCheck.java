package org.edraens.monitoring;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.cedarsoftware.util.io.JsonWriter;

/**
* BatchCheck
*/
// @SuppressWarnings("unchecked")
public class BatchCheck {
    
    private static void Ping(){
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("config.json"));
            JSONObject jsonObject = (JSONObject) obj;
            String servername = (String)jsonObject.get("servername");
            int timeout = ((Long)jsonObject.get("timeout")).intValue();
            
            // Parsing host list
            JSONArray hostsList = (JSONArray) jsonObject.get("ping");
            for (Object host : hostsList) {
                JSONObject jsonHost = (JSONObject) host;
                // Checking host status
                Ping item = new Ping((String) jsonHost.get("host"), (String) jsonHost.get("name"), timeout);
                if (item.getStatus()){
                    // Host is up
                    System.out.println("[OK] Ping up : " + item.getName() + " (" + item.getHost() + ")");
                    // Removing offline flag
                    if (jsonHost.containsKey("offline")) {
                        jsonHost.remove("offline");
                        new CallURL("[EdMon on "+servername+"] UP : "+item.getName()+" ("+item.getHost()+") ICMP Ping)");
                    }
                }
                else{
                    // Host is down
                    if (!jsonHost.containsKey("offline")){
                        // Offline flag not set, setting
                        System.err.println("[!!] Ping down : " + item.getName() + " (" + item.getHost() + ")");
                        jsonHost.put("offline", 1);
                        new CallURL("[EdMon on "+servername+"] DOWN : "+item.getName()+" ("+item.getHost()+") ICMP Ping)");
                    }
                    else 
                        // Offline flag already set, host is still down
                        System.err.println("[XX] Ping still down : " + item.getName() + " (" + item.getHost() + ")");
                }
            }
            FileWriter filew = new FileWriter("config.json");
            filew.write(JsonWriter.formatJson(jsonObject.toString()));
            filew.flush();
            filew.close();

        } catch (IOException e) {
            System.err.println("[ERROR] Unable to find config.json");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] config.json is not a valid JSON file or has been altered");
            System.exit(2);
        }
    }

    private static void Service() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("config.json"));
            JSONObject jsonObject = (JSONObject) obj;
            String servername = (String) jsonObject.get("servername");
            int timeout = ((Long) jsonObject.get("timeout")).intValue();

            // Parsing host list
            JSONArray hostsList = (JSONArray) jsonObject.get("services");
            for (Object host : hostsList) {
                JSONObject jsonHost = (JSONObject) host;
                // Checking host status
                Service item = new Service((String) jsonHost.get("host"), (String) jsonHost.get("name"), ((Long)jsonHost.get("port")).intValue(), timeout);
                if (item.getStatus()) {
                    // Host is up
                    System.out.println("[OK] Service up : " + item.getName() + " (" + item.getHost() + " TCP " + item.getPort() + ")");
                    // Removing offline flag
                    if (jsonHost.containsKey("offline")) {
                        jsonHost.remove("offline");
                        new CallURL("[EdMon on " + servername + "] UP : " + item.getName() + " (" + item.getHost()+" TCP "+item.getPort()+")");
                    }
                } else {
                    // Host is down
                    if (!jsonHost.containsKey("offline")) {
                        // Offline flag not set, setting
                        System.err.println("[!!] Service down : " + item.getName() + " (" + item.getHost() +" TCP "+item.getPort() +")");
                        jsonHost.put("offline", 1);
                        new CallURL("[EdMon on " + servername + "] DOWN : " + item.getName() + " (" + item.getHost() +" TCP " + item.getPort() + ")");
                    } else
                        // Offline flag already set, host is still down
                        System.err.println("[XX] Service still down : " + item.getName() + " (" + item.getHost() +" TCP "+item.getPort() +")");
                }
            }
            FileWriter filew = new FileWriter("config.json");
            filew.write(JsonWriter.formatJson(jsonObject.toString()));
            filew.flush();
            filew.close();

        } catch (IOException e) {
            System.err.println("[ERROR] Unable to find config.json");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[ERROR] config.json is not a valid JSON file or has been altered");
            System.exit(2);
        }
    }

    public BatchCheck(){
        System.out.println("[INFO] Running ICMP tests");
        Ping();
        System.out.println("[INFO] Running service tests");
        Service();
        // Service serv = new Service("163.172.84.166", "Emmy HTTP", "TCP", 80, 3);
        // if (serv.getStatus()) 
        //     System.out.println("[OK] Service up : " + serv.getName() + " (" + serv.getHost() +" "+ serv.getProtocol() +" "+serv.getPort() +")");
        // else System.out.println("[OK] Service down : " + serv.getName() + " (" + serv.getHost() +" "+ serv.getProtocol() +" "+serv.getPort() +")");
    }
}