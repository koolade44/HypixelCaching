package com.koolade446.server.apikeys;

public enum ApiKeyConstants {
    SUCCESS("success"),
    UUID("uuid"),
    SESSION("session"),
    ONLINE("online"),
    GAME_TYPE("gameType"),
    MODE("mode");



    String rawValue;
    int arrayIndex;

    ApiKeyConstants(String rawValue){
        this.rawValue = rawValue;
    }

    public String getRawValue() {
        return this.rawValue;
    }
}
