package com.pravdin.tpfinalmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Animation topAnimationTitle;
    Animation bottomAnimationTitle;
    ImageView imageViewLogo;
    TextView textViewTitle;

    Pair[] pairsAnimations = new Pair[2];

    final static int SCREEN_DURATION = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topAnimationTitle = AnimationUtils.loadAnimation(this, R.anim.top_animation_title);
        bottomAnimationTitle = AnimationUtils.loadAnimation(this, R.anim.bottom_animation_title);

        imageViewLogo = findViewById(R.id.imageViewLogo);
        textViewTitle = findViewById(R.id.textViewTitle);

        imageViewLogo.setAnimation(topAnimationTitle);
        textViewTitle.setAnimation(bottomAnimationTitle);


        pairsAnimations[0] = new Pair<View,String>(imageViewLogo, "imageView_logo");
        pairsAnimations[1] = new Pair<View,String>(textViewTitle, "textView_title");

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, LoginActivity.class);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairsAnimations);

            startActivity(intent, options.toBundle());
        }, SCREEN_DURATION);
    }
}