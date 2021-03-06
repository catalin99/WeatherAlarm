package catalin.facultate.weatheralarm;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.renderscript.RenderScript;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.internal.http2.Header;

import static android.renderscript.RenderScript.*;


public class MainActivity extends AppCompatActivity {
    public static final String HOUR = "catalin.facultate.weatheralarm.HOUR";
    public static final String MINUTES = "catalin.facultate.weatheralarm.MINUTES";
    public static final String TIMEZONE = "catalin.facultate.weatheralarm.TIMEZONE";
    public static final String ALARM = "catalin.facultate.weatheralarm.ALARM";
    FusedLocationProviderClient fusedLocationProviderClient;
    public static TextToSpeech textToSpeech;


    public void EditAlarm(View view) {
        int buttonPressedID = view.getId();
        TextView hour = null;
        TextView min = null;
        TextView timezone = null;
        Switch switchAlarm = null;
        int nr = 0;
        switch (buttonPressedID) {
            case R.id.viewbtn1:
                hour = findViewById(R.id.hourtxt1);
                min = findViewById(R.id.mintxt1);
                timezone = findViewById(R.id.timezonetxt1);
                switchAlarm = findViewById(R.id.switch1);
                nr = 1;
                break;
            case R.id.viewbtn2:
                hour = findViewById(R.id.hourtxt2);
                min = findViewById(R.id.mintxt2);
                timezone = findViewById(R.id.timezonetxt2);
                switchAlarm = findViewById(R.id.switch2);
                nr = 2;
                break;
            case R.id.viewbtn3:
                hour = findViewById(R.id.hourtxt3);
                min = findViewById(R.id.mintxt3);
                timezone = findViewById(R.id.timezonetxt3);
                switchAlarm = findViewById(R.id.switch3);
                nr = 3;
                break;
            case R.id.viewbtn4:
                hour = findViewById(R.id.hourtxt4);
                min = findViewById(R.id.mintxt4);
                timezone = findViewById(R.id.timezonetxt4);
                switchAlarm = findViewById(R.id.switch4);
                nr = 4;
                break;
            case R.id.viewbtn5:
                hour = findViewById(R.id.hourtxt5);
                min = findViewById(R.id.mintxt5);
                timezone = findViewById(R.id.timezonetxt5);
                switchAlarm = findViewById(R.id.switch5);
                nr = 5;
                break;
        }
        String hourStr = hour.getText().toString();
        String minStr = min.getText().toString();
        String timezoneStr = timezone.getText().toString();
        Intent intent = new Intent(this, EditTimeActivity.class);
        switchAlarm.setChecked(false);
        intent.putExtra(HOUR, hourStr);
        intent.putExtra(MINUTES, minStr);
        intent.putExtra(TIMEZONE, timezoneStr);
        intent.putExtra(ALARM, String.valueOf(nr));
        startActivity(intent);
    }

    public void DeleteAlarm(View view) {
        int buttonPressedID = view.getId();
        TextView hour = null;
        TextView min = null;
        TextView timezone = null;
        Switch switchAlarm = null;
        int nr = 0;
        switch (buttonPressedID) {
            case R.id.deletebtn1:
                hour = findViewById(R.id.hourtxt1);
                min = findViewById(R.id.mintxt1);
                timezone = findViewById(R.id.timezonetxt1);
                switchAlarm = findViewById(R.id.switch1);
                nr = 1;
                break;
            case R.id.deletebtn2:
                hour = findViewById(R.id.hourtxt2);
                min = findViewById(R.id.mintxt2);
                timezone = findViewById(R.id.timezonetxt2);
                switchAlarm = findViewById(R.id.switch2);
                nr = 2;
                break;
            case R.id.deletebtn3:
                hour = findViewById(R.id.hourtxt3);
                min = findViewById(R.id.mintxt3);
                timezone = findViewById(R.id.timezonetxt3);
                switchAlarm = findViewById(R.id.switch3);
                nr = 3;
                break;
            case R.id.deletebtn4:
                hour = findViewById(R.id.hourtxt4);
                min = findViewById(R.id.mintxt4);
                timezone = findViewById(R.id.timezonetxt4);
                switchAlarm = findViewById(R.id.switch4);
                nr = 4;
                break;
            case R.id.deletebtn5:
                hour = findViewById(R.id.hourtxt5);
                min = findViewById(R.id.mintxt5);
                timezone = findViewById(R.id.timezonetxt5);
                switchAlarm = findViewById(R.id.switch5);
                nr = 5;
                break;
        }
        if (hour != null && min != null && timezone != null) {
            hour.setText("00");
            min.setText("00");
            timezone.setText("AM");
            switchAlarm.setText("OFF");
            switchAlarm.setChecked(false);
            SharedPreferences sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("hour" + nr, hour.getText().toString());
            editor.putString("minute" + nr, min.getText().toString());
            editor.putString("timezone" + nr, timezone.getText().toString());
            editor.putString("switcher" + nr, "OFF");
            editor.commit();
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AndroidNetworking.initialize(getApplicationContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        InitializeAlarms();
        GetAlarms();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void InitializeAlarms() {
        SharedPreferences sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!sharedPreferences.contains("hour1"))
            editor.putString("hour1", "12");
        if (!sharedPreferences.contains("minute1"))
            editor.putString("minute1", "00");
        if (!sharedPreferences.contains("timezone1"))
            editor.putString("timezone1", "AM");
        if (!sharedPreferences.contains("switcher1"))
            editor.putString("switcher1", "OFF");
        if (!sharedPreferences.contains("hour2"))
            editor.putString("hour2", "12");
        if (!sharedPreferences.contains("minute2"))
            editor.putString("minute2", "00");
        if (!sharedPreferences.contains("timezone2"))
            editor.putString("timezone2", "AM");
        if (!sharedPreferences.contains("switcher2"))
            editor.putString("switcher2", "OFF");
        if (!sharedPreferences.contains("hour3"))
            editor.putString("hour3", "12");
        if (!sharedPreferences.contains("minute3"))
            editor.putString("minute3", "00");
        if (!sharedPreferences.contains("timezone3"))
            editor.putString("timezone3", "AM");
        if (!sharedPreferences.contains("switcher3"))
            editor.putString("switcher3", "OFF");
        if (!sharedPreferences.contains("hour4"))
            editor.putString("hour4", "12");
        if (!sharedPreferences.contains("minute4"))
            editor.putString("minute4", "00");
        if (!sharedPreferences.contains("timezone4"))
            editor.putString("timezone4", "AM");
        if (!sharedPreferences.contains("switcher4"))
            editor.putString("switcher4", "OFF");
        if (!sharedPreferences.contains("hour5"))
            editor.putString("hour5", "12");
        if (!sharedPreferences.contains("minute5"))
            editor.putString("minute5", "00");
        if (!sharedPreferences.contains("timezone5"))
            editor.putString("timezone5", "AM");
        if (!sharedPreferences.contains("switcher5"))
            editor.putString("switcher5", "OFF");
        editor.commit();
    }

    private void GetAlarms() {
        SharedPreferences sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE);
        TextView hourtxt1 = findViewById(R.id.hourtxt1);
        hourtxt1.setText(sharedPreferences.getString("hour1", "12"));
        TextView minutetxt1 = findViewById(R.id.mintxt1);
        minutetxt1.setText(sharedPreferences.getString("minute1", "00"));
        TextView timezonetxt1 = findViewById(R.id.timezonetxt1);
        timezonetxt1.setText(sharedPreferences.getString("timezone1", "AM"));
        Switch switcher1 = findViewById(R.id.switch1);
        if (sharedPreferences.getString("switcher1", "OFF").toString().equals("ON")) {
            switcher1.setChecked(true);
            switcher1.setText("ON");
        } else {
            switcher1.setChecked(false);
            switcher1.setText("OFF");
        }

        TextView hourtxt2 = findViewById(R.id.hourtxt2);
        hourtxt2.setText(sharedPreferences.getString("hour2", "12"));
        TextView minutetxt2 = findViewById(R.id.mintxt2);
        minutetxt2.setText(sharedPreferences.getString("minute2", "00"));
        TextView timezonetxt2 = findViewById(R.id.timezonetxt2);
        timezonetxt2.setText(sharedPreferences.getString("timezone2", "AM"));
        Switch switcher2 = findViewById(R.id.switch2);
        if (sharedPreferences.getString("switcher2", "OFF").toString().equals("ON")) {
            switcher2.setChecked(true);
            switcher2.setText("ON");
        } else {
            switcher2.setChecked(false);
            switcher2.setText("OFF");
        }


        TextView hourtxt3 = findViewById(R.id.hourtxt3);
        hourtxt3.setText(sharedPreferences.getString("hour3", "12"));
        TextView minutetxt3 = findViewById(R.id.mintxt3);
        minutetxt3.setText(sharedPreferences.getString("minute3", "00"));
        TextView timezonetxt3 = findViewById(R.id.timezonetxt3);
        timezonetxt3.setText(sharedPreferences.getString("timezone3", "AM"));
        Switch switcher3 = findViewById(R.id.switch3);
        if (sharedPreferences.getString("switcher3", "OFF").toString().equals("ON")) {
            switcher3.setChecked(true);
            switcher3.setText("ON");
        } else {
            switcher3.setChecked(false);
            switcher3.setText("OFF");
        }


        TextView hourtxt4 = findViewById(R.id.hourtxt4);
        hourtxt4.setText(sharedPreferences.getString("hour4", "12"));
        TextView minutetxt4 = findViewById(R.id.mintxt4);
        minutetxt4.setText(sharedPreferences.getString("minute4", "00"));
        TextView timezonetxt4 = findViewById(R.id.timezonetxt4);
        timezonetxt4.setText(sharedPreferences.getString("timezone4", "AM"));
        Switch switcher4 = findViewById(R.id.switch4);
        if (sharedPreferences.getString("switcher4", "OFF").toString().equals("ON")) {
            switcher4.setChecked(true);
            switcher4.setText("ON");
        } else {
            switcher4.setChecked(false);
            switcher4.setText("OFF");
        }


        TextView hourtxt5 = findViewById(R.id.hourtxt5);
        hourtxt5.setText(sharedPreferences.getString("hour5", "12"));
        TextView minutetxt5 = findViewById(R.id.mintxt5);
        minutetxt5.setText(sharedPreferences.getString("minute5", "00"));
        TextView timezonetxt5 = findViewById(R.id.timezonetxt5);
        timezonetxt5.setText(sharedPreferences.getString("timezone5", "AM"));
        Switch switcher5 = findViewById(R.id.switch5);
        if (sharedPreferences.getString("switcher5", "OFF").toString().equals("ON")) {
            switcher5.setChecked(true);
            switcher5.setText("ON");
        } else {
            switcher5.setChecked(false);
            switcher5.setText("OFF");
        }

    }


    public void EventStart1(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Switch switcher1 = findViewById(R.id.switch1);
        if (switcher1.isChecked()) {
            editor.putString("switcher1", "ON");
            switcher1.setText("ON");
            TextView hour = findViewById(R.id.hourtxt1);
            TextView minutes = findViewById(R.id.mintxt1);
            TextView timezone = findViewById(R.id.timezonetxt1);
            AddAlarm(hour.getText().toString(), minutes.getText().toString(), timezone.getText().toString());
        } else {
            editor.putString("switcher1", "OFF");
            switcher1.setText("OFF");
        }
        editor.commit();

    }

    public void EventStart2(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Switch switcher2 = findViewById(R.id.switch2);
        if (switcher2.isChecked()) {
            editor.putString("switcher2", "ON");
            switcher2.setText("ON");
            TextView hour = findViewById(R.id.hourtxt2);
            TextView minutes = findViewById(R.id.mintxt2);
            TextView timezone = findViewById(R.id.timezonetxt2);
            AddAlarm(hour.getText().toString(), minutes.getText().toString(), timezone.getText().toString());
        } else {
            editor.putString("switcher2", "OFF");
            switcher2.setText("OFF");
        }
        editor.commit();
    }

    public void EventStart3(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Switch switcher3 = findViewById(R.id.switch3);
        if (switcher3.isChecked()) {
            editor.putString("switcher3", "ON");
            switcher3.setText("ON");
            TextView hour = findViewById(R.id.hourtxt3);
            TextView minutes = findViewById(R.id.mintxt3);
            TextView timezone = findViewById(R.id.timezonetxt3);
            AddAlarm(hour.getText().toString(), minutes.getText().toString(), timezone.getText().toString());
        } else {
            editor.putString("switcher3", "OFF");
            switcher3.setText("OFF");
        }
        editor.commit();
    }

    public void EventStart4(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Switch switcher4 = findViewById(R.id.switch4);
        if (switcher4.isChecked()) {
            editor.putString("switcher4", "ON");
            switcher4.setText("ON");
            TextView hour = findViewById(R.id.hourtxt4);
            TextView minutes = findViewById(R.id.mintxt4);
            TextView timezone = findViewById(R.id.timezonetxt4);
            AddAlarm(hour.getText().toString(), minutes.getText().toString(), timezone.getText().toString());
        } else {
            editor.putString("switcher4", "OFF");
            switcher4.setText("OFF");
        }
        editor.commit();
    }

    public void EventStart5(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("alarms", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Switch switcher5 = findViewById(R.id.switch5);
        if (switcher5.isChecked()) {
            editor.putString("switcher5", "ON");
            switcher5.setText("ON");
            TextView hour = findViewById(R.id.hourtxt5);
            TextView minutes = findViewById(R.id.mintxt5);
            TextView timezone = findViewById(R.id.timezonetxt5);
            AddAlarm(hour.getText().toString(), minutes.getText().toString(), timezone.getText().toString());
        } else {
            editor.putString("switcher5", "OFF");
            switcher5.setText("OFF");
        }
        editor.commit();
    }

    public void AddAlarm(String hour, String minutes, String timezone) {
        int realHour = timezone.equals("PM") ? Integer.parseInt(hour) + 12 : Integer.parseInt(hour);
        int realMinutes = Integer.parseInt(minutes);
        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            CreateNotification(realHour, realMinutes);
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            CreateNotification(realHour, realMinutes);
        }
    }


    private void CreateNotification(int hour, int minutes)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                hour,
                minutes,
                0
        );
        setAlarm(calendar.getTimeInMillis());
    }

    private void setAlarm(long timeInMillis)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(this,"Alarm is set", Toast.LENGTH_SHORT).show();
    }

}




