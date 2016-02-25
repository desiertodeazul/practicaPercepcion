package com.example.orlando.practica1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orlando.arduinobt.Arduino;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener{

    Arduino arduino;
    Button btnConectar;
    Button btnSend;
    Button btnApagar;
    TextView xVal;
    TextView yVal;
    TextView zVal;
    TextView estado;
    TextView sensor;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        arduino = new Arduino(this);
        btnConectar = (Button)findViewById(R.id.btnConect);
        btnSend = (Button)findViewById(R.id.btnSend);
        btnApagar = (Button)findViewById(R.id.btnApagar);
        xVal = (TextView)findViewById(R.id.xLabel);
        yVal = (TextView)findViewById(R.id.yLabel);
        zVal = (TextView)findViewById(R.id.zLabel);
        estado = (TextView)findViewById(R.id.estado);
        sensor = (TextView)findViewById(R.id.sensorLabel);

        btnConectar.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnApagar.setOnClickListener(this);



        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
    public void onClick(View v) {
        Button btn = (Button)v;
        if (btn.getId() == R.id.btnConect){
            arduino.connect();
        }else if(btn.getId() == R.id.btnSend){
            arduino.write("1");
            Toast.makeText(this, "Envio 1",Toast.LENGTH_SHORT).show();
        }else if(btn.getId() == R.id.btnApagar ){
            arduino.write("0");
            Toast.makeText(this, "Envio 0", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {

                }

                last_x = x;
                last_y = y;
                last_z = z;

                xVal.setText(String.valueOf(last_x));
                yVal.setText(String.valueOf(last_y));
                zVal.setText(String.valueOf(last_z));

                if (y >= 5){
                    tipoEstaParado();
                }else{
                    tipoSeCayo();
                }

            }
        }
    }

    public void tipoEstaParado(){
        arduino.write("1");
        estado.setText("parado");
    }

    public void tipoSeCayo(){
        arduino.write("0");
        estado.setText("se cayo");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
