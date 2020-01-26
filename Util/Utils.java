package Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class Utils {

    public static final String BASE_URL = "https://org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22";
    public static final String ICON_URL = "http://api.openweathermap.org/img/w/";

    public static JSONObject getObject(String tagName, JSONObject jsonObject) throws JSONException {
        JSONObject jobj=jsonObject.getJSONObject(tagName);
        return jobj;
    }

    public static String getString(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(tagName);
    }

    public static float getFloat(String tagName, JSONObject jsonObject) throws JSONException {
        return (float) jsonObject.getDouble(tagName);
    }

    public static double getDouble(String tagName, JSONObject jsonObject) throws JSONException {
        return (float) jsonObject.getDouble(tagName);
    }
    public static int getInt(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt(tagName);
    }}