/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class ParseJson {
    
    public static JsonObject getJson(String json) {
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        //JsonArray array = object.getAsJsonArray("content");
        return object;
    }
    
    public static JsonObject getByKey(JsonObject json, String key) {
       return json.get(key).getAsJsonObject();
    }
}
