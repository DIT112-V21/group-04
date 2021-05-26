// joystick adapted from: https://github.com/controlwear/virtual-joystick-android
// Speedometer adapted from: https://github.com/anastr/SpeedView
package com.example.medcarapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.github.anastr.speedviewlib.ProgressiveGauge;
import io.github.controlwear.virtual.joystick.android.JoystickView;
import mqttController.CarConnect;

public class ManualControl extends AppCompatActivity {
    
    private static final int QOS = 0;
    private static final String TURNING_TOPIC = "/smartcar/control/turning";
    private static final String SPEED_TOPIC = "/smartcar/control/speed";
    private static final String AUTO_TOPIC = "/smartcar/control/auto";
    private static final int IMPOSSIBLE_ANGLE_AND_SPEED = -1000;
    private static final int REVERSE_CAR_MOVEMENT = -1;
    private static int autoOptions = 0;
    private Button autoButton;
    private CarConnect carConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_control);
        TextView connectionText = (TextView)findViewById(R.id.connectionText);
        ImageView carCamera = findViewById(R.id.cameraView);
        TextView angleIndicator = (TextView)findViewById(R.id.angleIndicator);
        ProgressiveGauge speedometer = (ProgressiveGauge) findViewById(R.id.speedometer);
        createSpeedometer(speedometer);
        autoButton = findViewById(R.id.autonomousDrivingButton);
        boolean shouldSwitch = getIntent().getExtras().getBoolean("Switch server");
        String user= getIntent().getExtras().getString("userKey");
        String pass= getIntent().getExtras().getString("passKey");
        carConnect = new CarConnect(this.getApplicationContext(), carCamera, shouldSwitch, autoButton,user,pass);
        carConnect.connectToMqttBroker(connectionText);

        JoystickView joystick = (JoystickView) findViewById(R.id.joystickView2);

        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            int previousAngle = IMPOSSIBLE_ANGLE_AND_SPEED;
            int previousSpeed = IMPOSSIBLE_ANGLE_AND_SPEED;
            @Override
            public void onMove(int angle, int strength) {
                int adjustedAngle = adjustAngle(angle);
                int adjustedSpeed = adjustSpeed(strength, angle);
                int speedometerSpeed = speedometerSpeed(strength,angle);
                turnCar(adjustedSpeed, adjustedAngle, previousAngle, previousSpeed);
                previousAngle = adjustedAngle;
                previousSpeed = adjustedSpeed;
                String degreeSymbol = getString(R.string.degreeSymbol);
                angleIndicator.setText(adjustedAngle + degreeSymbol);
                speedometer.speedTo(speedometerSpeed,1);
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
            adjustedSpeed = strength*0.6;
        } else {
            adjustedSpeed = (strength*0.6)*REVERSE_CAR_MOVEMENT;

        }
        return (int) adjustedSpeed;
    }

    private int speedometerSpeed (int strength, int angle){
        double speed;
            speed = strength*0.6;
        return (int) speed;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        String disconnectFromCarMessage = getString(R.string.disconnectFromCarMessage);
        carConnect.feedbackMessage(disconnectFromCarMessage);

        String startingAutonomousButtonText = getString(R.string.startingAutonomousButtonText);
        autoButton.setText(startingAutonomousButtonText);

        carConnect.disconnect(null);
        Intent intent = new Intent(ManualControl.this,MainActivity.class);
        intent.putExtra("Restrict back", true);
        startActivity(intent);
    }

    private void turnCar(int adjustedSpeed, int adjustedAngle, int previousAngle, int previousSpeed){
        if (adjustedAngle != previousAngle || adjustedSpeed != previousSpeed){
            if (adjustedSpeed == 0)
                adjustedAngle = 0;
            carConnect.publish(TURNING_TOPIC, Integer.toString(adjustedAngle), QOS, null);
            carConnect.publish(SPEED_TOPIC, Integer.toString(adjustedSpeed), QOS, null);
        }
    }

    public void sendMessage(View view) {
        if (autoOptions == 0){
            autoOptions = 1;
            String autonomousDrivingOnButtonText = getString(R.string.autonomousDrivingOnButtonText);
            autoButton.setText(autonomousDrivingOnButtonText);
            autoButton.setBackgroundColor(Color.GREEN);
        } else {
            autoOptions = 0;
            String autonomousDrivingOffButtonText = getString(R.string.autonomousDrivingOffButtonText);
            autoButton.setText(autonomousDrivingOffButtonText);
            autoButton.setBackgroundColor(Color.RED);
        }
        carConnect.publish(AUTO_TOPIC, Integer.toString(autoOptions), QOS, null);
    }

    private void createSpeedometer(ProgressiveGauge speedometer) {
        speedometer.setMinSpeed(0);
        speedometer.setMaxSpeed(60);
        String percentageSymbol = getString(R.string.percentageSymbol);
        speedometer.setUnit(percentageSymbol);
        speedometer.setWithTremble(false);
    }
}
