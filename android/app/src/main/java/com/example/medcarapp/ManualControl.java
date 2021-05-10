package com.example.medcarapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.os.PersistableBundle;

import android.widget.ImageView;

import android.view.Gravity;

import android.widget.TextView;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import mqttController.CarConnect;
import mqttController.MqttClient;

public class ManualControl extends AppCompatActivity {
    // joystick adapted from: https://github.com/controlwear/virtual-joystick-android

    private static final int QOS = 0;
    private static final String TURNING_TOPIC = "/smartcar/control/turning";
    private static final String SPEED_TOPIC = "/smartcar/control/speed";
    private static final int IMPOSSIBLE_ANGLE_AND_SPEED = -1000;
    private static final int REVERSE_CAR_MOVEMENT = -1;
    private static final String DISCONNECT_FROM_CAR_MESSAGE = "Disconnected from car.";
    private CarConnect carConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);
        TextView connectionText = (TextView)findViewById(R.id.connectionText);

        ImageView carCamera = findViewById(R.id.cameraView);

        TextView angleIndicator = (TextView)findViewById(R.id.angleIndicator);
        TextView speedIndicator = (TextView)findViewById(R.id.speedIndicator);


        carConnect = new CarConnect(this.getApplicationContext(), carCamera);
        carConnect.connectToMqttBroker(connectionText);

        JoystickView joystick = (JoystickView) findViewById(R.id.joystickView2);

        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            int previousAngle = IMPOSSIBLE_ANGLE_AND_SPEED;
            int previousSpeed = IMPOSSIBLE_ANGLE_AND_SPEED;
            @Override
            public void onMove(int angle, int strength) {
                int adjustedAngle = adjustAngle(angle);
                int adjustedSpeed = adjustSpeed(strength, angle);
                turnCar(adjustedSpeed, adjustedAngle, previousAngle, previousSpeed);
                previousAngle = adjustedAngle;
                previousSpeed = adjustedSpeed;
                speedIndicator.setText(adjustedSpeed + "%");
                angleIndicator.setText(adjustedAngle + "°");
            }
        });
    }

    private int adjustAngle(int angle){
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

    private int adjustSpeed(int strength, int angle){
        double adjustedSpeed;
        if (angle <= 180) {
            adjustedSpeed = strength*0.7;
        } else {
            adjustedSpeed = (strength*0.7)*REVERSE_CAR_MOVEMENT;

        }
        return (int) adjustedSpeed;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        carConnect.feedbackMessage(DISCONNECT_FROM_CAR_MESSAGE);
        carConnect.disconnect(null);
    }

    private void turnCar(int adjustedSpeed, int adjustedAngle, int previousAngle, int previousSpeed){
        if (adjustedAngle != previousAngle || adjustedSpeed != previousSpeed){
            if (adjustedSpeed == 0)
                adjustedAngle = 0;
            carConnect.publish(TURNING_TOPIC, Integer.toString(adjustedAngle), QOS, null);
            carConnect.publish(SPEED_TOPIC, Integer.toString(adjustedSpeed), QOS, null);
        }
    }
}