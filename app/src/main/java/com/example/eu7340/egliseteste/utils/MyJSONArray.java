package com.example.eu7340.egliseteste.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyJSONArray {

    private JSONArray json;

    public MyJSONArray(JSONArray json){
        this.json = json;
    }

    public MyJSONArray(String string){
        try {
            json = new JSONArray(string);
        }
        catch (JSONException e){
            e.printStackTrace();
            json = null;
        }
    }

    public int size(){
        return json.length();
    }

    public JSONArray getArray(int index){
        try{
            return json.getJSONArray(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getObjetc(int index){
        try{
            return json.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object get(int index){
        try{
            return json.get(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getString(int index){
        try{
            return json.getString(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getInt(int index){
        try{
            return json.getInt(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return Integer.MIN_VALUE;
        }
    }

    public Double getDouble(int index){
        try{
            return json.getDouble(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return Double.MIN_VALUE;
        }
    }

    public Boolean getBoolean(int index){
        try{
            return json.getBoolean(index);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

}
