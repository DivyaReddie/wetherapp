package data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.StringBuffer;
import java.lang.String;

import Util.Utils;

public class WeatherHttpClient {

    public String getWeatherData(String place) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpURLConnection) (new URL(Utils.BASE_URL + place)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            StringBuffer stringBuffer=new StringBuffer();
            inputStream=connection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line=null;

            while ((line=bufferedReader.readLine()) !=null) {
                stringBuffer.append(line+"\r\n");

            }
            inputStream.close();
            connection.disconnect();
            return stringBuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
