package com.koolade446.server;

import com.koolade446.server.apikeys.ApiKeyConstants;
import com.koolade446.server.connection.ServerConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ServerMain extends Thread{

    @Override
    public void run() {
        try {
            List<ServerConnection> connectedClients = new LinkedList<>();
            ServerSocket ss = new ServerSocket(888);

            while (true) {
                try {
                    Socket socket = ss.accept();
                    System.out.println("connected");
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    String clientMsg = String.valueOf(ois.readObject());

                    //Information key requestconnection=id:ID&uuid:UUID
                    if (clientMsg.contains("requestconnection=")) {
                        String connectionID = clientMsg.split("requestconnection=id:")[1].split("&uuid")[0];
                        String playerUUID = clientMsg.split("&uuid:")[1];
                        if (idExists(connectionID, connectedClients)) {
                            oos.writeObject("null:alreadyexists");
                        } else {
                            connectedClients.add(new ServerConnection(connectionID, playerUUID));
                            oos.writeObject("success");
                        }
                    }
                    //Information key getplayerstatus=id:ID&key:KEY
                    else if (clientMsg.contains("getplayerstatus=")) {
                        System.out.println("made it here");
                        String id = clientMsg.split("getplayerstatus=id:")[1].split("&key")[0];
                        String key = clientMsg.split("&key:")[1];
                        if (getConnectionByConnectionId(id, connectedClients) != null) {
                            ApiFetcher api = new ApiFetcher(getConnectionByConnectionId(id, connectedClients).getPlayerUUID(), key);
                            oos.writeObject("Type:map%" + api.getStatus().toString());
                        } else {
                            oos.writeObject("null:connectioninvalid");
                        }
                    }

                    oos.writeObject("fix possibly");

                    if (clientMsg.equals("exit")) break;

                    ois.close();
                    oos.close();
                    socket.close();
                }
                catch (Exception ignored){}
            }
            ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean idExists(String id, List<ServerConnection> l) {
        for (ServerConnection sc : l) {
            if (sc.getConnectionID().equals(id)) return true;
        }
        return false;
    }

    public ServerConnection getConnectionByConnectionId(String connectionID, List<ServerConnection> l) {
        for (ServerConnection sc : l) {
            if (sc.getConnectionID().equals(connectionID)) return sc;
        }
        return null;
    }
}
