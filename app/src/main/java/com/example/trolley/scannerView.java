package com.example.trolley;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scannerView extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    private static final int REQUEST_ENABLE_BLUETOOTH = 1 ;


    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public void connectFirst(){
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            System.out.println("EveryThing OK");
        }else{
            finish();
        }
    }


    public static String macAddress;
    public static String scanned;

    ZXingScannerView scannerView;
    LottieAnimationView lottieView;
    ImageView tapView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_view);
        scannerView= (ZXingScannerView) findViewById(R.id.zxscan);
        lottieView = (LottieAnimationView) findViewById(R.id.qranimation);
        tapView = (ImageView) findViewById(R.id.tapView);




//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        scannerView.setLaserColor(this.getResources().getColor(android.R.color.transparent,getTheme()));
        scannerView.setLaserEnabled(false);
        scannerView.setBorderColor(this.getResources().getColor(android.R.color.transparent,getTheme()));
        scannerView.setMaskColor(this.getResources().getColor(android.R.color.transparent,getTheme()));
        scannerView.setAutoFocus(true);


        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

        if (!bluetoothAdapter.isEnabled()){
            connectFirst();
        }
//        scannerView.setLaserEnabled(false);
//        scannerView.setBorderColor(this.getResources().getColor(android.R.color.transparent));
//        scannerView.setMaskColor(this.getResources().getColor(android.R.color.transparent));
//        List<BarcodeFormat> enableFormat = new ArrayList<>();
//        enableFormat.add(BarcodeFormat.QR_CODE);
//        scannerView.setFormats(enableFormat);
//        scannerView.setAutoFocus(true);

        try {
            int padding = paddingInDp(100);
            lottieView.setBackgroundColor(getResources().getColor(android.R.color.transparent,getTheme()));
            lottieView.setPadding(-padding, -padding, -padding, -padding);
            lottieView.setAnimation("myqrscann.json");
            lottieView.setMinAndMaxFrame(0,149);
            lottieView.setSpeed(1f);
            lottieView.playAnimation();
            lottieView.loop(true);
        } catch (Exception e){
            e.printStackTrace();
        }


        tapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Cliked on Screen");
                scannerView.setAutoFocus(true);
            }
        });


    }

    public int paddingInDp(int paddingDp){
        float density = this.getResources().getDisplayMetrics().density;
        return (int)(paddingDp * density);
    }

    public static boolean isValidMACAddress(String str)
    {
        String regex = "^([0-9A-Fa-f]{2}[:-])"
                + "{5}([0-9A-Fa-f]{2})|"
                + "([0-9a-fA-F]{4}\\."
                + "[0-9a-fA-F]{4}\\."
                + "[0-9a-fA-F]{4})$";

        Pattern p = Pattern.compile(regex);
        if (str == null)
        {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

    @Override
    public void handleResult(Result rawResult) {
        MainActivity.scanned=rawResult.getText().toString();
        CartActivity.macAddress= rawResult.getText().toString();
        System.out.println("handleResult : "+CartActivity.macAddress);
        if (isValidMACAddress(CartActivity.macAddress) ){
            startActivity(new Intent(getApplicationContext(), GoCartAnimation.class));
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), "Not a Mac Address", Toast.LENGTH_SHORT).show();
            onResume();
        }
//        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(scannerView.this);
        scannerView.startCamera();
//        Handler h =new Handler(Looper.getMainLooper()) ;
//        h.postDelayed(new Runnable() {
//            public void run() {
//                scannerView.setResultHandler(scannerView.this);
//                scannerView.startCamera();
//            }
//
//        }, 1000);
    }
}