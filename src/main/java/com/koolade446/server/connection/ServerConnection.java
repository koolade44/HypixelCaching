package com.koolade446.server.connection;

public class ServerConnection {
    String connectionID;
    String playerUUID;

    public ServerConnection (String connectionId, String playerUUID) {
        this.connectionID = connectionId;
        this.playerUUID = playerUUID;
    }

    public String getConnectionID() {
        return connectionID;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }
}
