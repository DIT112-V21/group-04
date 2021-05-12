package com.example.medcarapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ServerSelection extends AppCompatActivity {
    RecyclerView rvServerSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_selection);

        serverSelection();
        connectButton();
    }

    public void serverSelection(){
        String[] serverName, serverDesc;
        rvServerSelection = findViewById(R.id.rvServerSelection);

        serverName = getResources().getStringArray(R.array.serverOption);
        serverDesc = getResources().getStringArray(R.array.serverDescription);

        Adapter serverAdapter = new Adapter(this, serverName, serverDesc);
        rvServerSelection.setAdapter(serverAdapter);
        rvServerSelection.setLayoutManager(new LinearLayoutManager(this));
    }

    public void connectButton(){
        Button button = findViewById(R.id.btnConfirm);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);


            startActivity(intent);
        });
    }
}
