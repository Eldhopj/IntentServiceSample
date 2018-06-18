package com.example.eldho.intentservicesample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void updateCount1(View view) {
        Intent myIntent = new Intent(getApplicationContext(),MyIntentService.class);
        myIntent.setAction(Values.KEY_COUNT_1);
        startService(myIntent);
    }

    public void updateCount2(View view) {
        Intent myIntent = new Intent(getApplicationContext(),MyIntentService.class);
        myIntent.setAction(Values.KEY_COUNT_2);
        startService(myIntent);
    }
}
