package com.example.trolley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity {

    private ListView lv;
    User user = SharedPrefManager.getInstance(this).getUser();
    final String userId = String.valueOf(user.getId());
    ArrayList<HashMap<String, String>> historyList;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HistoryActivity.this, HistoryActivity.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        historyList = new ArrayList<>();


        lv = (ListView) findViewById(R.id.listView);
        new getHistory().execute();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(HistoryActivity.this,R.color.white));// set status background white


        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.home:
                        finish();
//                        Intent home = new Intent(HistoryActivity.this, ProfileActivity.class);
//                        startActivity(home);
                        break;

                    case R.id.history:

                        break;
                    case R.id.notepad:
                        Intent popUpWindow = new Intent(HistoryActivity.this, NotepadActivity.class);
                        startActivity(popUpWindow);
                        break;
                    case R.id.about:
                        finish();
                        Intent info = new Intent(HistoryActivity.this, AboutActivity.class);
                        startActivity(info);
                        break;
                    case R.id.logout:
                        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
                        Menu menu = bottomNavigationView.getMenu();
                        MenuItem menuItem = menu.getItem(4);
                        menuItem.setChecked(true);
                        Context context = HistoryActivity.this;
                        new AlertDialog.Builder(context)
                                .setCancelable(false)
                                .setTitle("Logout")
                                .setMessage("Would you like to logout?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // logout
                                        finish();
                                        SharedPrefManager.getInstance(getApplicationContext()).logout();
                                        startActivity(new Intent(HistoryActivity.this, LoginActivity.class));
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // user doesn't want to logout
                                        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
                                        Menu menu = bottomNavigationView.getMenu();
                                        MenuItem menuItem = menu.getItem(1);
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

    private class getHistory extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("userId", userId);

            //returning the response
            return requestHandler.sendPostRequest(URLs.URL_HISTORY, params);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);

                //if no error in response
                if (!obj.getBoolean("error")) {

                    //getting the user from the response
                    JSONArray products = obj.getJSONArray("history");

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        String id = c.getString("id");
                        String total = c.getString("total");
                        String date = c.getString("date");

                        // tmp hash map for single contact
                        HashMap<String, String> invoice = new HashMap<>();

                        // adding each child node to HashMap key => value
                        invoice.put("id", id);
                        invoice.put("total", total);
                        invoice.put("date", date);

                        // adding contact to contact list
                        historyList.add(invoice);
                    }

                    ListAdapter adapter = new SimpleAdapter(
                            HistoryActivity.this, historyList,
                            R.layout.history_item, new String[]{"id", "total",
                            "date"}, new int[]{R.id.id,
                            R.id.total, R.id.date});

                    lv.setAdapter(adapter);

                } else {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}