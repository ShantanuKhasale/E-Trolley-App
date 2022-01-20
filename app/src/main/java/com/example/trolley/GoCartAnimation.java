package com.example.trolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.w3c.dom.Text;

public class GoCartAnimation extends AppCompatActivity {

    LottieAnimationView LottieView;
    TextView msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_cart_animation);
        msg=(TextView) findViewById(R.id.msgCart);
        LottieView=(LottieAnimationView) findViewById(R.id.gocart);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(GoCartAnimation.this,R.color.white));// set status background white
        LottieView.setAnimation("gocartanim.json");
        LottieView.setSpeed(0.5f);
        LottieView.playAnimation();
        LottieView.loop(false);

        Handler h =new Handler() ;
        h.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
                overridePendingTransition(0, 0);
            }

        }, 4100);
    }
}