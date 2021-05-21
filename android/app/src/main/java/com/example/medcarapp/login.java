package com.example.medcarapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {
        String User, password;
        EditText inputUser;
        EditText inputPassword;
        Button loginButton;

        @Override
        protected void onCreate (Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            inputUser = (EditText) findViewById(R.id.inputUser);
            inputPassword = (EditText) findViewById(R.id.inputPassword);
            loginButton = (Button) findViewById(R.id.loginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User = inputUser.getText().toString();
                    password = inputPassword.getText().toString();
                }
            });
        }
    }
