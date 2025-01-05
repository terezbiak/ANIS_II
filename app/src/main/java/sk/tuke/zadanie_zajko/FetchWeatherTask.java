package sk.tuke.zadanie_zajko;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.BreakIterator;

public class FetchWeatherTask extends AsyncTask<Void, Void, String> {
    private Context context;
    private double latitude;
    private double longitude;
    private String apiKey = "ab6796ccdf591c1e56ca2539268032bc"; // Nahraďte YOUR_API_KEY vaším API kľúčom
    private WeatherFetchListener listener;

    public FetchWeatherTask(Context context, double latitude, double longitude, WeatherFetchListener listener) {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        this.listener = listener;
    }


    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + this.latitude + "&lon=" + this.longitude + "&appid=" + apiKey + "&units=metric");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder json = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        json.append(line);
                    }
                    reader.close();
                    return json.toString();
                } else {
                    // Log response code and message for non-OK responses
                    Log.e("FetchWeatherTask", "HTTP response code: " + responseCode + " - " + connection.getResponseMessage());
                    return null;
                }
            } finally {
                connection.disconnect(); // Always disconnect HTTPURLConnection
            }
        } catch (Exception e) {
            Log.e("FetchWeatherTask", "Error during HTTP request", e);
            return null;
        }
    }



    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);
        if (json == null || json.isEmpty()) {
            if (listener != null) {
                listener.onWeatherFetched("Default"); // Predvolený stav, keď nedôjde k spojeniu
            }
        } else {
            // Parsovanie JSON a volanie listener.onWeatherFetched ako predtým
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                if (weatherArray.length() > 0) {
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    String mainWeather = weatherObject.getString("main");

                    if (listener != null) {
                        listener.onWeatherFetched(mainWeather);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onWeatherFetched("Default");
                }
            }
        }
    }

}


