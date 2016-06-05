package com.harsh.quotamatic;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    LinearLayout layout;
    Button button;
    TextView text, text2, text3;

    int i = 0;
    int m, n;
    int currentm, currentn;

    Random rand = new Random();

    public String[] quotes, colors;
    String singleQuote;
    List<String> quoteDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(), "Shake phone to get Quotes", Toast.LENGTH_LONG).show();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        layout = (LinearLayout) findViewById(R.id.layout);
        button = (Button) findViewById(R.id.button);
        text = (TextView) findViewById(R.id.text);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);

        quotes = getResources().getStringArray(R.array.quotes);
        colors = getResources().getStringArray(R.array.colors);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeValues();
            }
        });

    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if (mAccel > 10) {
                changeValues();
                //Toast.makeText(getApplicationContext(), "Device has shaken.", Toast.LENGTH_SHORT).show();
            }

            //Toast.makeText(getApplicationContext(), String.valueOf(mAccelCurrent), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    public void changeValues() {
        n = rand.nextInt(quotes.length);
        m = rand.nextInt(colors.length);
        singleQuote = quotes[n];
        quoteDetail = Arrays.asList(singleQuote.split("#"));
        Log.d("Current number: ", String.valueOf(n));
        Log.d("Quote length number: ", String.valueOf(quotes.length));
        if (n < quotes.length) {
            text.setText("\"" + quoteDetail.get(0));
            text2.setText("- " + quoteDetail.get(1));
            text3.setText(colors[m]);
            //text.setTextColor(Color.parseColor(colors[m]));
            //text2.setTextColor(Color.parseColor(colors[m]));
            //text3.setTextColor(Color.parseColor(colors[m]));
            layout.setBackgroundColor(Color.parseColor(colors[m]));
            //button.setTextColor(Color.parseColor(colors[m]));
            button.setBackgroundColor(Color.parseColor(colors[m]));
        } else {
            Toast.makeText(getApplicationContext(), "No More Quotes", Toast.LENGTH_LONG).show();
        }
        i += 1;
    }

}
