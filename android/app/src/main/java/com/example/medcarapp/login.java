package com.example.medcarapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttClient;

import mqttController.CarConnect;

public class login extends AppCompatActivity {

    String User;
    String password;
        EditText inputUser;
        EditText inputPassword;
        Button loginButton;
        CarConnect carConnect;
        Context context;
        //TextView connectionText = (TextView)findViewById(R.id.connectionText);
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    Boolean status=false;
        @Override
        protected void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);

            inputUser = (EditText) findViewById(R.id.inputUser);
            inputPassword = (EditText) findViewById(R.id.inputPassword);
            loginButton = (Button) findViewById(R.id.loginButton);

            loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    User = inputUser.getText().toString();
                    password = inputPassword.getText().toString();
                if(User.equals("user")&&password.equals("password")){
                    Toast.makeText(login.this,"Succesfull Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(login.this, MainActivity.class);
                    boolean shouldSwitch = getIntent().getExtras().getBoolean("Switch server");
                    intent.putExtra("userKey", User);
                    intent.putExtra("passKey", password);
                    intent.putExtra("Switch server", shouldSwitch);
                    startActivity(intent);
                }else{
                    Toast.makeText(login.this,"Wrong Username/Password", Toast.LENGTH_SHORT).show();
                }




                    //carConnect.connectToMqttBroker(connectionText);


                }});

        }
        public void Switch(){
            Intent intent = new Intent(login.this, MainActivity.class);
            boolean shouldSwitch = getIntent().getExtras().getBoolean("Switch server");
            intent.putExtra("userKey", User);
            intent.putExtra("passKey", password);
            intent.putExtra("Switch server", shouldSwitch);
            startActivity(intent);
        }
    }
