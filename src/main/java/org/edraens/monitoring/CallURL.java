package org.edraens.monitoring;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
* CallURL
*/
public class CallURL {
    
    public CallURL(String message){
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("config.json"));
            JSONObject jsonObject = (JSONObject) obj;
            boolean sendNotification = true;
            // Check if we are a slave server and if master responds to ping
            if (jsonObject.containsKey("master")) {
                String address = (String) jsonObject.get("master");
                Ping master = new Ping(address, "Master check", 2);
                if (master.getStatus()) {
                    sendNotification = false;
                    System.out.println("[INFO] Master server is available, not sending notification");
                }
                else System.out.println("[INFO] Master server is down, sending notification");
            }

            // If URL is set in config.json, we have to notify
            if (jsonObject.containsKey("notifyURL") && sendNotification) {
                // Creating URL
                String URL = (String) jsonObject.get("notifyURL");
                URL myurl = new URL(URL+URLEncoder.encode(message, "UTF-8"));
                // Sending GET request
                HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
                con.setRequestMethod("GET");
                new BufferedReader(new InputStreamReader(con.getInputStream()));

            }
		} catch (Exception e) {
			System.err.println(e);
		}
        
    }
}