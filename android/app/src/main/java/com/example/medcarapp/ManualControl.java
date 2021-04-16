package com.example.medcarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ManualControl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);

        JoystickView joystick = (JoystickView) findViewById(R.id.joystickView2);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                System.out.println(angle);
            }
        });
    }
}