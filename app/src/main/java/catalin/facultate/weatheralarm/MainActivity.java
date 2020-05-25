package catalin.facultate.weatheralarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String HOUR = "catalin.facultate.weatheralarm.HOUR";
    public static final String MINUTES = "catalin.facultate.weatheralarm.MINUTES";
    public static final String TIMEZONE = "catalin.facultate.weatheralarm.TIMEZONE";
    public static final String ALARM = "catalin.facultate.weatheralarm.ALARM";
    PendingIntent pi;
    BroadcastReceiver br;
    AlarmManager am;
    public void AddAlarm(View view)
    {

    }

    public void EditAlarm(View view)
    {
        int buttonPressedID = view.getId();
        TextView hour = null;
        TextView min = null;
        TextView timezone = null;
        int nr = 0;
        switch (buttonPressedID)
        {
            case R.id.viewbtn1:
                hour = findViewById(R.id.hourtxt1);
                min = findViewById(R.id.mintxt1);
                timezone = findViewById(R.id.timezonetxt1);
                nr = 1;
                break;
            case R.id.viewbtn2:
                hour = findViewById(R.id.hourtxt2);
                min = findViewById(R.id.mintxt2);
                timezone = findViewById(R.id.timezonetxt2);
                nr=2;
                break;
            case R.id.viewbtn3:
                hour = findViewById(R.id.hourtxt3);
                min = findViewById(R.id.mintxt3);
                timezone = findViewById(R.id.timezonetxt3);
                nr=3;
                break;
            case R.id.viewbtn4:
                hour = findViewById(R.id.hourtxt4);
                min = findViewById(R.id.mintxt4);
                timezone = findViewById(R.id.timezonetxt4);
                nr=4;
                break;
            case R.id.viewbtn5:
                hour = findViewById(R.id.hourtxt5);
                min = findViewById(R.id.mintxt5);
                timezone = findViewById(R.id.timezonetxt5);
                nr=5;
                break;
        }
        String hourStr = hour.getText().toString();
        String minStr = min.getText().toString();
        String timezoneStr = timezone.getText().toString();
        Intent intent = new Intent(this, EditTimeActivity.class);
        intent.putExtra(HOUR, hourStr);
        intent.putExtra(MINUTES, minStr);
        intent.putExtra(TIMEZONE, timezoneStr);
        intent.putExtra(ALARM, String.valueOf(nr));
        startActivity(intent);
    }

    public void DeleteAlarm(View view)
    {
         int buttonPressedID = view.getId();
         TextView hour = null;
         TextView min = null;
         TextView timezone = null;
         switch (buttonPressedID)
         {
             case R.id.deletebtn1:
                 hour = findViewById(R.id.hourtxt1);
                 min = findViewById(R.id.mintxt1);
                 timezone = findViewById(R.id.timezonetxt1);
                 break;
             case R.id.deletebtn2:
                 hour = findViewById(R.id.hourtxt2);
                 min = findViewById(R.id.mintxt2);
                 timezone = findViewById(R.id.timezonetxt2);
                 break;
             case R.id.deletebtn3:
                 hour = findViewById(R.id.hourtxt3);
                 min = findViewById(R.id.mintxt3);
                 timezone = findViewById(R.id.timezonetxt3);
                break;
             case R.id.deletebtn4:
                 hour = findViewById(R.id.hourtxt4);
                 min = findViewById(R.id.mintxt4);
                 timezone = findViewById(R.id.timezonetxt4);
                 break;
             case R.id.deletebtn5:
                 hour = findViewById(R.id.hourtxt5);
                 min = findViewById(R.id.mintxt5);
                 timezone = findViewById(R.id.timezonetxt5);
                 break;
         }
         if(hour!=null && min!=null && timezone!=null)
         {
             hour.setText("00");
             min.setText("00");
             timezone.setText("AM");
         }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        InitializeAlarms();
        GetAlarms();

        Context context = getApplicationContext();
        InitializeNotifications(context);

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

    @Override
    protected void onDestroy() {
        am.cancel(pi);
        unregisterReceiver(br);
        super.onDestroy();
    }

    private void InitializeAlarms()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("alarms",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!sharedPreferences.contains("hour1"))
            editor.putString("hour1", "12");
        if(!sharedPreferences.contains("minute1"))
            editor.putString("minute1", "00");
        if(!sharedPreferences.contains("timezone1"))
            editor.putString("timezone1", "AM");
        if(!sharedPreferences.contains("hour2"))
            editor.putString("hour2", "12");
        if(!sharedPreferences.contains("minute2"))
            editor.putString("minute2", "00");
        if(!sharedPreferences.contains("timezone2"))
            editor.putString("timezone2", "AM");
        if(!sharedPreferences.contains("hour3"))
            editor.putString("hour3", "12");
        if(!sharedPreferences.contains("minute3"))
            editor.putString("minute3", "00");
        if(!sharedPreferences.contains("timezone3"))
            editor.putString("timezone3", "AM");
        if(!sharedPreferences.contains("hour4"))
            editor.putString("hour4", "12");
        if(!sharedPreferences.contains("minute4"))
            editor.putString("minute4", "00");
        if(!sharedPreferences.contains("timezone4"))
            editor.putString("timezone4", "AM");
        if(!sharedPreferences.contains("hour5"))
            editor.putString("hour5", "12");
        if(!sharedPreferences.contains("minute5"))
            editor.putString("minute5", "00");
        if(!sharedPreferences.contains("timezone5"))
            editor.putString("timezone5", "AM");
        editor.commit();
    }

    private void GetAlarms()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("alarms",MODE_PRIVATE);
        TextView hourtxt1 = findViewById(R.id.hourtxt1);
        hourtxt1.setText(sharedPreferences.getString("hour1", "12"));
        TextView minutetxt1 = findViewById(R.id.mintxt1);
        minutetxt1.setText(sharedPreferences.getString("minute1", "00"));
        TextView timezonetxt1 = findViewById(R.id.timezonetxt1);
        timezonetxt1.setText(sharedPreferences.getString("timezone1", "AM"));

        TextView hourtxt2 = findViewById(R.id.hourtxt2);
        hourtxt2.setText(sharedPreferences.getString("hour2", "12"));
        TextView minutetxt2 = findViewById(R.id.mintxt2);
        minutetxt2.setText(sharedPreferences.getString("minute2", "00"));
        TextView timezonetxt2 = findViewById(R.id.timezonetxt2);
        timezonetxt2.setText(sharedPreferences.getString("timezone2", "AM"));

        TextView hourtxt3 = findViewById(R.id.hourtxt3);
        hourtxt3.setText(sharedPreferences.getString("hour3", "12"));
        TextView minutetxt3 = findViewById(R.id.mintxt3);
        minutetxt3.setText(sharedPreferences.getString("minute3", "00"));
        TextView timezonetxt3 = findViewById(R.id.timezonetxt3);
        timezonetxt3.setText(sharedPreferences.getString("timezone3", "AM"));

        TextView hourtxt4 = findViewById(R.id.hourtxt4);
        hourtxt4.setText(sharedPreferences.getString("hour4", "12"));
        TextView minutetxt4 = findViewById(R.id.mintxt4);
        minutetxt4.setText(sharedPreferences.getString("minute4", "00"));
        TextView timezonetxt4 = findViewById(R.id.timezonetxt4);
        timezonetxt4.setText(sharedPreferences.getString("timezone4", "AM"));

        TextView hourtxt5 = findViewById(R.id.hourtxt5);
        hourtxt5.setText(sharedPreferences.getString("hour5", "12"));
        TextView minutetxt5 = findViewById(R.id.mintxt5);
        minutetxt5.setText(sharedPreferences.getString("minute5", "00"));
        TextView timezonetxt5 = findViewById(R.id.timezonetxt5);
        timezonetxt5.setText(sharedPreferences.getString("timezone5", "AM"));
    }

    private void InitializeNotifications(Context context)
    {
        Intent notifyIntent = new Intent(this,MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 22);
        calendar.set(Calendar.SECOND, 00);
        if (now.after(calendar)) {
            Log.d("Hey","Added a day");
            calendar.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  calendar.getTimeInMillis(),
                1000 * 60 * 60 * 24, pendingIntent);
    }
}




