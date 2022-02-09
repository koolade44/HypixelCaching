package com.koolade446.server;

import com.koolade446.server.apikeys.ApiKeyConstants;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.spec.ECField;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Client {
    static Map<Class<?>, Predicate<String>> parsableAs = new HashMap<>();

    static {
        parsableAs.put(Boolean.TYPE, s -> {if (s.contains("true") || s.contains("false")) {return true;}else{return false;}});
    }
    public static void main(String[] args) {
        try {
            Socket socket;
            ObjectOutputStream oos;
            ObjectInputStream ois;
            while (true) {
                //establish socket connection to server
                socket = new Socket("localhost", 888);

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                String msg = br.readLine();
                //write to socket using ObjectOutputStream
                oos = new ObjectOutputStream(socket.getOutputStream());

                oos.writeObject(msg);

                System.out.println("Sending request to Socket Server");

                //read the server response message
                ois = new ObjectInputStream(socket.getInputStream());
                Object oisObj = ois.readObject();


                if (oisObj.toString().contains("Type:map")) {
                    String typeRemoved = ((String) oisObj).split("%")[1];
                    System.out.println(typeRemoved);
                    HashMap<String, Object> hm = new HashMap<>();
                    for (String s : typeRemoved.split(",")) {
                        String[] pair = s.replace(" ", "").split("=");
                        if (parsableAs.get(Boolean.TYPE).test(pair[1])) {
                            hm.put(pair[0].replace("=", "").replace("{", ""), Boolean.parseBoolean(pair[1]));
                        }
                        else {
                            hm.put(pair[0].replace("=", "").replace("{", ""), pair[1]);
                        }
                    }
                    System.out.println("\n\n" + hm.get(ApiKeyConstants.MODE.getRawValue()) + " " + hm.get(ApiKeyConstants.GAME_TYPE.getRawValue()));
                }
                else {
                    System.out.println(oisObj);
                }
                ois.close();
                oos.close();
            }
        }
        catch (Exception ignored){
            ignored.printStackTrace();
        }
    }
}
