package com.example.medcarapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroductoryActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 5000;

    Animation splashAnim;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_introductory);

        splashAnim = AnimationUtils.loadAnimation(this, R.anim.splash_animation);

        image = findViewById(R.id.logo);
        image.setTag(R.drawable.logo7);

        image.setAnimation(splashAnim);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(IntroductoryActivity.this, ServerSelection.class);
            startActivity(intent);
            finish();
        }, SPLASH_DURATION);
    }
}