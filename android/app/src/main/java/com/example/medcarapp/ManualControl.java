package com.example.medcarapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import mqttController.CarConnect;
import mqttController.MqttClient;

public class ManualControl extends AppCompatActivity {
    // joystick adapted from: https://github.com/controlwear/virtual-joystick-android
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);

        JoystickView joystick = (JoystickView) findViewById(R.id.joystickView2);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                    /*System.out.println("joystick angle: " + angle);
                    System.out.println("adjusted angle: " + adjustAngle(angle));
                    System.out.println("joystick speed: " + strength);
                    System.out.println("adjusted speed: " + adjustSpeed(strength, angle));*/
                    //CarConnect test=new CarConnect();
                    //test.moveForwardLeft();
            }
        });
    }

   int adjustAngle(int angle){
        int adjustedAngle;
        if (angle >= 90 && angle <= 180) { // go left
            adjustedAngle = 90 - angle;
        } else if (angle < 90 && angle >= 0) { // go right
            adjustedAngle = 90 - angle;
        } else if (angle > 0 && angle >= 270){ // go back right
            adjustedAngle = angle - 270;
        } else { // go back left
            adjustedAngle = angle - 270;
        }
        return adjustedAngle;
    }

    int adjustSpeed(int strength, int angle){
        int adjustedSpeed;
        if (angle <= 180) {
            adjustedSpeed = strength;
        } else {
            adjustedSpeed = strength*-1;
        }
        return adjustedSpeed;
    }
}