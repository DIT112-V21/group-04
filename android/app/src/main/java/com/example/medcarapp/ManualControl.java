package com.example.medcarapp;

import android.graphics.Color;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import mqttController.CarConnect;

public class ManualControl extends AppCompatActivity {
    // joystick adapted from: https://github.com/controlwear/virtual-joystick-android

    private static final int QOS = 1;
    private static final String TURNING_TOPIC = "/smartcar/control/turning";
    private static final String SPEED_TOPIC = "/smartcar/control/speed";
    private static final String AUTO_TOPIC = "/smartcar/control/auto";
    private static final int IMPOSSIBLE_ANGLE_AND_SPEED = -1000;
    private static final int REVERSE_CAR_MOVEMENT = -1;
    private static final String DISCONNECT_FROM_CAR_MESSAGE = "Disconnected from car.";
    private static final String AUTONOMOUS_DRIVING_ON = "Auto-on";
    private static final String AUTONOMOUS_DRIVING_OFF = "Auto-off";
    private static int auto = 0;
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
        int adjustedSpeed;
        if (angle <= 180) {
            adjustedSpeed = strength;
        } else {
            adjustedSpeed = strength*REVERSE_CAR_MOVEMENT;
        }
        return adjustedSpeed;
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

    /** Called when the user touches the button */
    public void sendMessage(View view) {
        Button button = findViewById(R.id.button2);
        if (auto == 0){
            auto = 1;
            button.setText(AUTONOMOUS_DRIVING_ON);
            button.setBackgroundColor(Color.GREEN);
        } else {
            auto = 0;
            button.setText(AUTONOMOUS_DRIVING_OFF);
            button.setBackgroundColor(Color.RED);
        }
        carConnect.publish(AUTO_TOPIC, Integer.toString(auto), QOS, null);
    }
}
