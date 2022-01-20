package com.example.trolley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static String scanned;

    EditText editTextUsername, editTextEmail, editTextPassword, editTextPhone;
    LottieAnimationView lottieView;
    private long pressedTime;
    TextView textContent;
    Button buttonRegister;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }
        //getSupportActionBar().hide();
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        lottieView = (LottieAnimationView) findViewById(R.id.animationView);
        textContent=(TextView) findViewById(R.id.textContent);
        lottieView.setAnimation("whitebgcat.json");
        buttonRegister = findViewById(R.id.buttonRegister);
        lottieView.loop(false);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.white));// set status background white

        textContent.setText("Hi i am Ollie, \nWelcome to E-Trolley App \nPlease Signup to start your journey with us! ");
        changeTextColor(textContent,getResources().getColor(R.color.newblue),getResources().getColor(R.color.neworange),2000,1);
        editTextPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {


                } else {

                    textContent.setText("Hi i am Ollie, \nWelcome to E-Trolley App \nPlease Login to start your journey with us! ");
                    changeTextColor(textContent,getResources().getColor(R.color.newblue),getResources().getColor(R.color.neworange),2000,1);

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


        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {


                } else {

                    textContent.setText("Hi i am Ollie, \nWelcome to E-Trolley App \nPlease Login to start your journey with us! ");
                    changeTextColor(textContent,getResources().getColor(R.color.newblue),getResources().getColor(R.color.neworange),2000,1);

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

        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {


                } else {

                    textContent.setText("Hi i am Ollie, \nWelcome to E-Trolley App \nPlease Login to start your journey with us! ");
                    changeTextColor(textContent,getResources().getColor(R.color.newblue),getResources().getColor(R.color.neworange),2000,1);

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
                    changeTextColor(textContent,getResources().getColor(R.color.newblue),getResources().getColor(R.color.neworange),2000,1);

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













        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                registerUser();
            }
        });

        //if user pressed on login
        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //we will open the login screen
                finish();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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


    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();

        //first we will do the validations

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter username");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextPhone.setError("Please enter your phone number");
            editTextPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }

        //if it passes all the validations

        class RegisterUser extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler();

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("phone", phone);

                //returing the response
                return requestHandler.sendPostRequest(URLs.URL_REGISTER, params);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //displaying the progress bar while user registers on the server
                progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //hiding the progressbar after completion
                progressBar.setVisibility(View.GONE);

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
                        Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }


}