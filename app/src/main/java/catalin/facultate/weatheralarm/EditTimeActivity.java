package catalin.facultate.weatheralarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class EditTimeActivity extends AppCompatActivity {
    private String HOUR;
    private String MINUTES;
    private String TIMEZONE;
    private String ALARM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_time);
        Intent intent = getIntent();
        HOUR = intent.getStringExtra(MainActivity.HOUR);
        MINUTES = intent.getStringExtra(MainActivity.MINUTES);
        TIMEZONE = intent.getStringExtra(MainActivity.TIMEZONE);

        TextView txtOldHour = findViewById(R.id.hourtxtOld);
        TextView txtOldMinutes = findViewById(R.id.mintxtOld);
        TextView txtOldTimezone = findViewById(R.id.timezonetxtOld);

        txtOldHour.setText(HOUR);
        txtOldMinutes.setText(MINUTES);
        txtOldTimezone.setText(TIMEZONE);

        Spinner spinnerHour = (Spinner) findViewById(R.id.spinnerHour);
        ArrayAdapter<CharSequence> adapterHour = ArrayAdapter.createFromResource(this,
                R.array.hours, android.R.layout.simple_spinner_item);
        adapterHour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHour.setAdapter(adapterHour);

        Spinner spinnerMinutes = (Spinner) findViewById(R.id.spinnerMinutes);
        ArrayAdapter<CharSequence> adapterMinutes = ArrayAdapter.createFromResource(this,
                R.array.minutes, android.R.layout.simple_spinner_item);
        adapterMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMinutes.setAdapter(adapterMinutes);

        Spinner spinnerTimezone = (Spinner) findViewById(R.id.spinnerTimezone);
        ArrayAdapter<CharSequence> adapterTimezone = ArrayAdapter.createFromResource(this,
                R.array.timezone, android.R.layout.simple_spinner_item);
        adapterTimezone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimezone.setAdapter(adapterTimezone);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void SetAlarm(View view)
    {
        Spinner spinnerHour = (Spinner) findViewById(R.id.spinnerHour);
        Spinner spinnerMinutes = (Spinner) findViewById(R.id.spinnerMinutes);
        Spinner spinnerTimezone = (Spinner) findViewById(R.id.spinnerTimezone);
        TextView txtOldHour = findViewById(R.id.hourtxtOld);
        TextView txtOldMinutes = findViewById(R.id.mintxtOld);
        TextView txtOldTimezone = findViewById(R.id.timezonetxtOld);
        txtOldHour.setText(spinnerHour.getSelectedItem().toString());
        txtOldMinutes.setText(spinnerMinutes.getSelectedItem().toString());
        txtOldTimezone.setText(spinnerTimezone.getSelectedItem().toString());
        Intent intent = getIntent();
        ALARM = intent.getStringExtra(MainActivity.ALARM);
        SharedPreferences sharedPreferences = getSharedPreferences("alarms",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("hour"+ALARM, txtOldHour.getText().toString());
        editor.putString("minute"+ALARM, txtOldMinutes.getText().toString());
        editor.putString("timezone"+ALARM, txtOldTimezone.getText().toString());
        editor.putString("switcher"+ALARM, "OFF");
        editor.commit();

    }
}

