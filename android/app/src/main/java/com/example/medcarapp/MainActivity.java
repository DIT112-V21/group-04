package com.example.medcarapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements Adapter.ItemClickListener {
    RecyclerView rvAvailableCars;
    Dialog creditDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        creditDialog();
        carSelection();
        connectButton();
    }

    public void CreditPopup(View v) {
        creditDialog.setContentView(R.layout.credit_popup);
        creditDialog.setCanceledOnTouchOutside(true);
        creditDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        creditDialog.show();
    }

    public void carSelection(){
        String[] s1, s2;
        rvAvailableCars = findViewById(R.id.rvAvaliableCars);

        s1 = getResources().getStringArray(R.array.carNames);
        s2 = getResources().getStringArray(R.array.description);

        Adapter adapter = new Adapter(this, s1, s2);
        adapter.addItemClickListener(this);
        rvAvailableCars.setAdapter(adapter);
        rvAvailableCars.setLayoutManager(new LinearLayoutManager(this));
    }

    public void connectButton(){
        Button button = findViewById(R.id.btnConnect);
        button.setEnabled(false);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManualControl.class);


            startActivity(intent);
        });
    }

    public void creditDialog(){
        creditDialog = new Dialog(this);
    }

    @Override
    public void onItemClick(int position) {
        Button button = findViewById(R.id.btnConnect);
        button.setEnabled(true);
    }
}