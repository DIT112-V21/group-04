package com.example.medcarapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {
    public Button getLoginButton() {
        return loginButton;
    }

    public void setLoginButton(Button loginButton) {
        this.loginButton = loginButton;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    String User;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String password;
        EditText inputUser;
        EditText inputPassword;
        Button loginButton;

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
                    Intent intent = new Intent(login.this, MainActivity.class);
                    boolean shouldSwitch = getIntent().getExtras().getBoolean("Switch server");
                    User = inputUser.getText().toString();
                    password = inputPassword.getText().toString();
                    intent.putExtra("userKey",User);
                    intent.putExtra("passKey",password);
                    intent.putExtra("Switch server", shouldSwitch);
                    startActivity(intent);
                }
            });
        }
    }
