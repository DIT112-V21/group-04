package com.example.medcarapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ServerSelection extends AppCompatActivity {
    RecyclerView rvServerSelection;
    Dialog creditDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_selection);
        creditDialog();
        serverSelection();
    }

    public void CreditPopup(View v) {
        creditDialog.setContentView(R.layout.credit_popup);
        creditDialog.setCanceledOnTouchOutside(true);
        creditDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        creditDialog.show();
    }

    public void creditDialog(){
        creditDialog = new Dialog(this);
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
