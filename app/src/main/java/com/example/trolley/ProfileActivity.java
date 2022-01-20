package com.example.trolley;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewUsername, textViewEmail, textViewPhone, fab1Text, fab2Text, fab3Text;
    private long pressedTime;
    User user = SharedPrefManager.getInstance(this).getUser();

    FloatingActionButton fabMain, fab1, fab2, fab3;
    Float translationYaxis = 100f;
    LottieAnimationView profileManView;
    LottieAnimationView startButtonView;

    Boolean menuOpen = false;


    OvershootInterpolator interpolator = new OvershootInterpolator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        profileManView=(LottieAnimationView) findViewById(R.id.boxAnimation);
        profileManView.setAnimation("profileman.json");
        profileManView.playAnimation();
        profileManView.loop(true);

        startButtonView = (LottieAnimationView) findViewById(R.id.startButton);
        startButtonView.setAnimation("startbutton.json");
        startButtonView.playAnimation();
        startButtonView.loop(true);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(ProfileActivity.this,R.color.white));// set status background white


        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.home:
                        break;

                    case R.id.history:
                        Intent history = new Intent(ProfileActivity.this, HistoryActivity.class);
                        startActivity(history);

                        break;
                    case R.id.notepad:
                        Intent popUpWindow = new Intent(ProfileActivity.this, NotepadActivity.class);
                        startActivity(popUpWindow);
                        break;
                    case R.id.about:
                        Intent info = new Intent(ProfileActivity.this, AboutActivity.class);
                        startActivity(info);
                        break;
                    case R.id.logout:
                        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
                        Menu menu = bottomNavigationView.getMenu();
                        MenuItem menuItem = menu.getItem(4);
                        menuItem.setChecked(true);
                        Context context = ProfileActivity.this;
                        new AlertDialog.Builder(context)
                                .setCancelable(false)
                                .setTitle("Logout")
                                .setMessage("Would you like to logout?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // logout
                                        finish();
                                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // user doesn't want to logout
                                        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
                                        Menu menu = bottomNavigationView.getMenu();
                                        MenuItem menuItem = menu.getItem(0);
                                        menuItem.setChecked(true);
                                    }
                                })
                                .show();

                        break;

                }
                return false;
            }
        });
        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail =  findViewById(R.id.textViewEmail);
        textViewPhone =  findViewById(R.id.textViewPhone);


        //setting the values to the textview
        textViewUsername.setText(user.getUsername());
        textViewEmail.setText(user.getEmail());
        textViewPhone.setText(user.getPhone());
        textViewEmail.setMovementMethod(new ScrollingMovementMethod());

        //when the user presses logout button
        //calling the logout method
//        findViewById(R.id.buttonLogout).setOnClickListener(view -> {
//            finish();
//            SharedPrefManager.getInstance(getApplicationContext()).logout();
//            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
//        });

        //When user click Start shopping button
        findViewById(R.id.buttonStartShopping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createInvoice();
            }
        });




    }






    private void openNotepad() {
        Intent popUpWindow = new Intent(ProfileActivity.this, NotepadActivity.class);
        startActivity(popUpWindow);
    }


    private void openPopUpReminderList() {
        Intent popUpWindow = new Intent(ProfileActivity.this, ReminderActivity.class);
        startActivity(popUpWindow);
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


        private void createInvoice(){
            final String userId = String.valueOf(user.getId());

            class createInvoice extends AsyncTask<Void, Void, String> {
                @Override
                protected String doInBackground(Void... voids) {
                    //creating request handler object
                    RequestHandler requestHandler = new RequestHandler();

                    //creating request parameters
                    HashMap<String, String> params = new HashMap<>();
                    params.put("userId", userId);

                    //returning the response
                    return requestHandler.sendPostRequest(URLs.URL_CREATEINVOICE, params);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    //hiding the progressbar after completion

                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(s);

                        //if no error in response
                        if (!obj.getBoolean("error")) {


                            //getting the user from the response
                            JSONObject userJson = obj.getJSONObject("invoice");

                            //creating a new user object
                            Invoice invoice = new Invoice(
                                    userJson.getInt("id"),
                                    userJson.getInt("userId"),
                                    userJson.getString("date"),
                                    userJson.getBoolean("paid")
                            );

                            //storing the invoice in shared preferences
                            SharedPrefManager.getInstance(getApplicationContext()).userInvoice(invoice);

                            //starting the Search activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), ShowQrAnimationActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            createInvoice ci = new createInvoice();
            ci.execute();

    }
}