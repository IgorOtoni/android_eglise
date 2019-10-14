package com.example.eu7340.egliseteste.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyJSONObject {

    private JSONObject json;

    public MyJSONObject(JSONObject json){
        this.json = json;
    }

    public boolean isValid(){
        return !(json == null);
    }

    public MyJSONObject(String string){
        try {
            json = new JSONObject(string);
        }
        catch (JSONException e){
            e.printStackTrace();
            json = null;
        }
    }

    public JSONArray getArray(String name){
        try{
            return json.getJSONArray(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getObjetc(String name){
        try{
            return json.getJSONObject(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object get(String name){
        try{
            return json.get(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getString(String name){
        try{
            return json.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getInt(String name){
        try{
            return json.getInt(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }

    public Double getDouble(String name){
        try{
            return json.getDouble(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return Double.MIN_VALUE;
        }
    }

    public Boolean getBoolean(String name){
        try{
            return json.getBoolean(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

}
