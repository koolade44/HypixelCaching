package com.koolade446.server;

import com.koolade446.server.apikeys.ApiKeyConstants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ApiFetcher {

    public HashMap<String, HashMap<String, Object>> statusMap;
    public String uuid;

    public ApiFetcher(String playerUUID, String playerApiKey) throws IOException {
        this.statusMap = new HashMap<>();
        this.uuid = playerUUID;
        HashMap<String, Object> ol = new HashMap<>();
        URL url = new URL("https://api.hypixel.net/status?uuid=" + playerUUID + "&key=" + playerApiKey);

        System.out.println(url);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

        StringBuffer sb = new StringBuffer();

        String s;

        while ((s = br.readLine()) != null) {
            sb.append(s);
        }

        JSONObject jso = new JSONObject(sb.toString());


        ol.put(ApiKeyConstants.SUCCESS.getRawValue(), jso.getBoolean(ApiKeyConstants.SUCCESS.getRawValue()));
        ol.put(ApiKeyConstants.UUID.getRawValue(), jso.getString(ApiKeyConstants.UUID.getRawValue()));

        JSONObject session = jso.getJSONObject(ApiKeyConstants.SESSION.getRawValue());
        ol.put(ApiKeyConstants.ONLINE.getRawValue(), session.getBoolean(ApiKeyConstants.ONLINE.getRawValue()));
        if (session.getBoolean(ApiKeyConstants.ONLINE.getRawValue())) {
            ol.put(ApiKeyConstants.GAME_TYPE.getRawValue(), session.getString(ApiKeyConstants.GAME_TYPE.getRawValue()));
            ol.put(ApiKeyConstants.MODE.getRawValue(), session.getString(ApiKeyConstants.MODE.getRawValue()));
        }

        statusMap.put(playerUUID, ol);

        br.close();
        con.disconnect();
    }

    public HashMap<String, Object> getStatus() {
        return statusMap.get(uuid);
    }
}
