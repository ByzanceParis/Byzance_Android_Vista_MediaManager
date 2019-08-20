package world.byzance.vista_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MyBroadcastReceiver MyReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter intentFilter = new IntentFilter("world.byzance.VistaApi.complete");
        MyReceiver = new MyBroadcastReceiver();
        if(intentFilter != null)
        {
            Log.d("MainActivity","Register");

            registerReceiver(MyReceiver, intentFilter);
        }
        VistaManager.getInstance().update();

    }


    public class MyBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivity","Application ready");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(MyReceiver != null)
            unregisterReceiver(MyReceiver);
    }
}
