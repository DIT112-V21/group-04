package com.example.medcarapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvAvaliableCars;

    String s1[], s2[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvAvaliableCars = findViewById(R.id.rvAvaliableCars);

        s1 = getResources().getStringArray(R.array.carNames);
        s2 = getResources().getStringArray(R.array.description);

        Adapter adapter = new Adapter(this, s1, s2);
        rvAvaliableCars.setAdapter(adapter);
        rvAvaliableCars.setLayoutManager(new LinearLayoutManager(this));

        Button button = findViewById(R.id.btnConnect);

        button.setOnClickListener(v -> {

            Intent intent = new Intent(this, ManualControl.class);

            startActivity(intent);
        });
    }
}