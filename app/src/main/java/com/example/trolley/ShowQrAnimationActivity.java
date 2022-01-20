package com.example.trolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class ShowQrAnimationActivity extends AppCompatActivity {
    LottieAnimationView LottieView;
    TextView msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr_animation);
        LottieView = (LottieAnimationView) findViewById(R.id.qrentanimation);
        msg= (TextView) findViewById(R.id.msg);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(ShowQrAnimationActivity.this,R.color.white));// set status background white
        LottieView.setAnimation("qranimation.json");
        LottieView.playAnimation();
        LottieView.loop(false);
        Handler h =new Handler() ;
        h.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(getApplicationContext(), scannerView.class));
            }

        }, 3000);
    }

}
