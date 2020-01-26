package com.example.theweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import Util.Utils;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;


    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (TextView) findViewById(R.id.cityText);
        iconView = (ImageView) findViewById(R.id.thumbnailIcon);
        temp = (TextView) findViewById(R.id.tempText);
        description = (TextView) findViewById(R.id.cloudText);
        humidity = (TextView) findViewById(R.id.humidText);
        pressure = (TextView) findViewById(R.id.pressureText);
        wind = (TextView) findViewById(R.id.windText);
        sunrise = (TextView) findViewById(R.id.riseText);
        sunset = (TextView) findViewById(R.id.setText);
        updated = (TextView) findViewById(R.id.updateText);

        renderWeatherData("london");

    }

    public void renderWeatherData(String city) {

        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=metric"});

    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        @Override

        protected void onPostExecute(Bitmap bitmap) {
            iconView.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImage(params[0]);
        }

        private Bitmap downloadImage(String code) {
            final DefaultHttpClient client = new DefaultHttpClient();

            // final HttpGet getRequest = new HttpGet(Utils.ICON_URL + code + ".png");
            final HttpGet getRequest = new HttpGet("http://www.9o.ri.com/store/media/images/8ab579a656.jpg");
            try {
                HttpResponse response = client.execute(getRequest);

                final int statusCode = response.getStatusLine().getStatusCode();


                if (statusCode != HttpStatus.SC_OK) {
                    Log.e("DownloadImage", "Error:" + statusCode);
                    return null;
                }
                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream inputStream = null;
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }


        } catch(Exception e)

        {
            e.printStackTrace();
        }
            return null;
    }

}
    private class WeatherTask extends AsyncTask<String, Void, Weather> {


        @SuppressLint("WrongThread")
        @Override
        protected Weather doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));
            weather.iconData = weather.currentCondition.getIcon();
            weather = JSONWeatherParser.getWeather(data);
            Log.v("Data:", weather.currentCondition.getDescription());
           new DownloadImageAsyncTask().execute(weather.iconData);
            return weather;
        }

        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            DateFormat df = DateFormat.getTimeInstance();
            String sunriseDate = df.format(new Date(weather.place.getSunrise()));
            String sunsetDate = df.format(new Date(weather.place.getSunset()));
            String updateDate = df.format(new Date(weather.place.getLastupdate()));

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());


            cityName.setText(weather.place.getCity() + "," + weather.place.getCountry());
            temp.setText("" + tempFormat + "C");
            humidity.setText("Humidity:" + weather.currentCondition.getHumidity() + "%");
            pressure.setText(("Pressure:" + weather.currentCondition.getPressure() + "hPa"));
            wind.setText("Wind:" + weather.wind.getSpeed() + "mps");
            sunrise.setText("Sunrise:" + sunriseDate);
            sunset.setText("Sunset:" + sunsetDate);
            updated.setText("Last Updated:" + updateDate);
            description.setText("Condition:" + weather.currentCondition.getCondition() + "(" +
                    weather.currentCondition.getDescription() + ")");


        }


    }


}
