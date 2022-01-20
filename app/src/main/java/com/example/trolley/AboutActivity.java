package com.example.trolley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(AboutActivity.this,R.color.white));// set status background white


        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.home:
                        finish();
                        Intent home = new Intent(AboutActivity.this, ProfileActivity.class);
                        startActivity(home);
                        break;

                    case R.id.history:
                        finish();
                        Intent history = new Intent(AboutActivity.this, HistoryActivity.class);
                        startActivity(history);

                        break;
                    case R.id.notepad:

                        Intent popUpWindow = new Intent(AboutActivity.this, NotepadActivity.class);
                        startActivity(popUpWindow);
                        break;
                    case R.id.about:
                        break;

                    case R.id.logout:
                        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
                        Menu menu = bottomNavigationView.getMenu();
                        MenuItem menuItem = menu.getItem(4);
                        menuItem.setChecked(true);
                        Context context = AboutActivity.this;
                        new AlertDialog.Builder(context)
                                .setCancelable(false)
                                .setTitle("Logout")
                                .setMessage("Would you like to logout?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // logout
                                        finish();
                                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                                        startActivity(new Intent(AboutActivity.this, LoginActivity.class));
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // user doesn't want to logout
                                        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
                                        Menu menu = bottomNavigationView.getMenu();
                                        MenuItem menuItem = menu.getItem(3);
                                        menuItem.setChecked(true);
                                    }
                                })
                                .show();
                        break;

                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AboutActivity.this, ProfileActivity.class));

    }
}