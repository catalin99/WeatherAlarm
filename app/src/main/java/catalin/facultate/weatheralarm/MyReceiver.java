package catalin.facultate.weatheralarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, MyNewIntentService.class);
        Toast.makeText(context, "Alarm running", Toast.LENGTH_SHORT).show();
        context.startService(intent1);


    }
}
