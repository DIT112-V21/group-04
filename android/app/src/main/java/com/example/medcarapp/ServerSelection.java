package com.example.medcarapp;

import android.os.Bundle;
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
    }

    public void serverSelection(){
        String[] serverName, serverDesc;
        int images[] = {R.drawable.offline,R.drawable.online};
        rvServerSelection = findViewById(R.id.rvServerSelection);

        serverName = getResources().getStringArray(R.array.serverOption);
        serverDesc = getResources().getStringArray(R.array.serverDescription);

        ServerAdapter serverAdapter = new ServerAdapter(this, serverName, serverDesc, images);
        rvServerSelection.setAdapter(serverAdapter);
        rvServerSelection.setLayoutManager(new LinearLayoutManager(this));
    }
}
