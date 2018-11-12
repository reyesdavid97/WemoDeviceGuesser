package com.example.david.wemoapp;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivityWemo extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wemo);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final TextView resultTxt = findViewById(R.id.resultTxt);

        Button getStateBtn = findViewById(R.id.getStateBtn);
        getStateBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    resultTxt.setText(Discovery.checkState());
                }
                catch (Exception e)
                {
                    resultTxt.setText("Exception caught: " + e);
                }
            }
        });

        Button onBtn = findViewById(R.id.onBtn);
        onBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    resultTxt.setText(Discovery.turnOn());
                }
                catch (Exception e)
                {
                    resultTxt.setText("Exception caught: " + e);
                }
            }
        });

        Button offBtn = findViewById(R.id.offBtn);
        offBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    resultTxt.setText(Discovery.turnOff());
                }
                catch (Exception e)
                {
                    resultTxt.setText("Exception caught: " + e);
                }
            }
        });

        Button guessBtn = findViewById(R.id.guessBtn);
        guessBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    double power = Discovery.getPower(); // current power in milliwatts
                    if (power == -1.0) // error
                    {
                        resultTxt.setText("Error: Switch did not respond.");
                    }
                    else
                    {
                        String device = "Unknown"; // default case
                        power = power / 1000; // convert to watts

                        if (power <= 2)
                            device = "None plugged in / Device powered off";
                        else if (7 <= power && power <= 10)
                            device = "Lamp";
                        else if (35 <= power && power <= 45)
                            device = "Electric fan";
                        else if (230 <= power && power <= 240)
                            device = "Vacuum cleaner";
                        else if (245 <= power && power <= 260)
                            device = "Blender";

                        resultTxt.setText("Current power draw: " + power + " Watts" + "\nPlugged-in device: " + device);
                    }
                }
                catch (Exception e)
                {
                    resultTxt.setText("Exception caught: " + e);
                }
            }
        });
    }
}
