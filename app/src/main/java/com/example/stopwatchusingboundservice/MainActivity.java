package com.example.stopwatchusingboundservice;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.ComponentName;
        import android.content.Context;
        import android.content.Intent;
        import android.content.ServiceConnection;
        import android.os.Bundle;
        /*import android.os.Handler;*/
        import android.os.IBinder;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        /*import android.widget.Toast;*/

public class MainActivity extends AppCompatActivity {

    boundservice BoundService;
    boolean ServiceBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onStart();
        final TextView timestampText = (TextView) findViewById(R.id.timestamp_text);
        Button printTimestampButton = (Button) findViewById(R.id.print_timestamp);
        Button stopServiceButon = (Button) findViewById(R.id.stop_service);
        printTimestampButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                if (ServiceBound) {

                    timestampText.setText(BoundService.getTimestamp());
                }
            }
        });

        stopServiceButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ServiceBound) {

                    unbindService(mServiceConnection);
                    ServiceBound = false;
                }
                Intent intent = new Intent(MainActivity.this,
                        boundservice.class);
                stopService(intent);
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();
        Intent intent = new Intent(this, boundservice.class);
        startService(intent);
        ServiceBound=true;
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (ServiceBound) {
            unbindService(mServiceConnection);
            ServiceBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            boundservice.MyBinder myBinder = (boundservice.MyBinder) service;
            BoundService = myBinder.getService();
            ServiceBound = true;
        }
    };
}



