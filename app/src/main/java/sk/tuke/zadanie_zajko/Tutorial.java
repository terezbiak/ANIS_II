package sk.tuke.zadanie_zajko;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Tutorial extends AppCompatActivity implements WeatherFetchListener{
    private GameView gameView;
    double longitude;
    double latitude;
    String weather = "Clear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ApiWeather apiWeather = new ApiWeather(this);
        LocationCoordinates coordinates = apiWeather.getLocation();
        if (coordinates != null) {
            latitude = coordinates.getLatitude();
            longitude = coordinates.getLongitude();

        }
        else {
            //Kosice
            latitude = 48.716385;
            longitude = 21.261074;
        }

        new FetchWeatherTask(this, latitude, longitude, this).execute();

    }

    public void startGame(View view) {
        GameView gameView = new GameView(this);
        setContentView(gameView);


    }
    @Override
    public void onWeatherFetched(String mainWeather) {
        //Toast.makeText(this, "Current Weather: " + mainWeather, Toast.LENGTH_LONG).show();
        weather = mainWeather;
        //weather = "Storm";
        SharedPreferences preferences = getSharedPreferences("weather_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("weather", weather);
        editor.apply();
    }
}
