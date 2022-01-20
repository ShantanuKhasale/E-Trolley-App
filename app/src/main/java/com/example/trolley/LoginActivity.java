package com.example.trolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    private long pressedTime;
    LottieAnimationView lottieView;
    Button buttonLogin;
    TextView textContent;

    public static void changeTextColor(final TextView textView, int startColor, int endColor,
                                       final long animDuration, final long animUnit){
        if (textView == null) return;

        final int startRed = Color.red(startColor);
        final int startBlue = Color.blue(startColor);
        final int startGreen = Color.green(startColor);

        final int endRed = Color.red(endColor);
        final int endBlue = Color.blue(endColor);
        final int endGreen = Color.green(endColor);

        new CountDownTimer(animDuration, animUnit){
            //animDuration is the time in ms over which to run the animation
            //animUnit is the time unit in ms, update color after each animUnit

            @Override
            public void onTick(long l) {
                int red = (int) (endRed + (l * (startRed - endRed) / animDuration));
                int blue = (int) (endBlue + (l * (startBlue - endBlue) / animDuration));
                int green = (int) (endGreen + (l * (startGreen - endGreen) / animDuration));

                textView.setTextColor(Color.rgb(red, green, blue));
            }

            @Override
            public void onFinish() {
                textView.setTextColor(Color.rgb(endRed, endGreen, endBlue));
            }
        }.start();
    }

    public void callotherAnimation(){
        lottieView.setSpeed(-2F);
        lottieView.setMinAndMaxFrame(0, 60);
        lottieView.playAnimation();
        lottieView.loop(true);
    }

    public void callotherAnimation2(){
        lottieView.setMinAndMaxFrame(95,120);
        lottieView.setSpeed(0.4F);
        lottieView.playAnimation();
        lottieView.loop(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        lottieView = (LottieAnimationView) findViewById(R.id.animationView);
        textContent=(TextView) findViewById(R.id.textContent);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this,R.color.white));// set status background white
        lottieView.setAnimation("whitebgcat.json");
        buttonLogin = findViewById(R.id.buttonLogin);
        lottieView.loop(false);


        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }

        //if user presses on login
        //calling the method login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        //if user presses on not registered
        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        textContent.setText("Hi i am Ollie, \nWelcome to E-Trolley App \nPlease Login to start your journey with us! ");
        MainActivity.changeTextColor(textContent,getResources().getColor(R.color.newblue),getResources().getColor(R.color.neworange),2000,1);
        YoYo.with(Techniques.SlideInRight).duration(2000).playOn(textContent);

        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                if (!hasFocus) {


                } else {

                    textContent.setText("Hi i am Ollie, \nWelcome to E-Trolley App \nPlease Login to start your journey with us! ");
                    MainActivity.changeTextColor(textContent,getResources().getColor(R.color.newblue),getResources().getColor(R.color.neworange),2000,1);
//                    YoYo.with(Techniques.SlideInRight).duration(2000).playOn(textContent);

                    if (lottieView.getFrame()>=0 && lottieView.getFrame()<95){
                        lottieView.setMinAndMaxFrame(lottieView.getFrame(),95);
                        lottieView.setSpeed(2F);
                        lottieView.playAnimation();
                        lottieView.loop(false);

                        int Totalframes = (int)(lottieView.getMaxFrame() - lottieView.getMinFrame());
                        int secs = (int) Totalframes/30;
                        double microseconds = Totalframes%30 * Math.pow(10,-2);
                        long time = (long) (((secs + microseconds)*1000));

                        lottieView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                callotherAnimation2();
                            }
                        }, time);

                    }
                    else {
                        lottieView.setMinAndMaxFrame(95, 120);
                        lottieView.setSpeed(0.4F);
                        lottieView.playAnimation();
                        lottieView.loop(true);
                    }

                }
            }
        });



        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus ) {

                } else {

                    textContent.setText("Peek-a-boo!!! \nWe make sure to keep your password private \n Even Ollie is not allowed to watch your password");
                    MainActivity.changeTextColor(textContent,getResources().getColor(R.color.newblue),getResources().getColor(R.color.neworange),2000,1);
//                    YoYo.with(Techniques.StandUp).duration(2000).playOn(textContent);

                    if (lottieView.getFrame()>60) {
                        lottieView.setMinAndMaxFrame(0, lottieView.getFrame());
                        lottieView.setSpeed(-2F);

                        lottieView.playAnimation();
                        lottieView.loop(false);

                        int Totalframes = (int)(lottieView.getMaxFrame() - lottieView.getMinFrame());
                        int secs = (int) Totalframes/30;
                        double microseconds = Totalframes%30 * Math.pow(10,-2);
                        long time = (long) (((secs + microseconds)*1000));

                        lottieView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                callotherAnimation();
                            }
                        }, time);



                    }

                    else{
                        lottieView.setSpeed(-2F);
                        lottieView.setMinAndMaxFrame(0, 120);
                        lottieView.playAnimation();
                        lottieView.loop(true);

                    }
                }
            }
        });
    }


    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }


    private void userLogin() {
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        //if everything is fine

        class UserLogin extends AsyncTask<Void, Void, String> {



            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);



                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(s);

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getInt("id"),
                                userJson.getString("username"),
                                userJson.getString("email"),
                                userJson.getString("phone")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                //returning the response
                return requestHandler.sendPostRequest(URLs.URL_LOGIN, params);
            }
        }

        UserLogin ul = new UserLogin();
        ul.execute();
    }
}