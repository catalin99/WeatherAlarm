package catalin.facultate.weatheralarm;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.AlarmClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyAlarm extends BroadcastReceiver {

    FusedLocationProviderClient fusedLocationProviderClient;
    Context contextFin;

    @Override
    public void onReceive(Context context, Intent intent) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        Log.i("ALARM", "Alarm started");
        contextFin = context;

        GetGpsData(intent);

    }

    public void GetGpsData(final Intent intent) {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location != null){
                    Geocoder geocoder = new Geocoder(contextFin, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        Log.i("gps", "Lat:" + addresses.get(0).getLatitude() + "; Long:"+addresses.get(0).getLongitude());
                        GetWeather(addresses.get(0).getLatitude(), addresses.get(0).getLongitude(), addresses.get(0).getLocality(), intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void GetWeather(double latitude, double longitude, final String address, final Intent intent)
    {
        String KEY = "decff345052161600a68df9e762f7c08";
        String API = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude + "&lon=" + longitude + "&units=metric&exclude=hourly,daily&appid=" + KEY;
        AndroidNetworking.get(API)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("weather", response.toString());
                        JSONObject currentWeather = null;
                        try {
                            currentWeather = response.getJSONObject("current");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONArray description = currentWeather.getJSONArray("weather");
                            String details = "The weather in " + address + " is " + description.getJSONObject(0).get("description").toString() + " and the temperature is " + currentWeather.get("temp").toString() + " celsius degrees";

                            MainActivity.textToSpeech.speak(details, TextToSpeech.QUEUE_FLUSH, null ,null);
                            Log.i("details", details);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.i("Error", anError.getMessage());
                    }
                });


    }
}
