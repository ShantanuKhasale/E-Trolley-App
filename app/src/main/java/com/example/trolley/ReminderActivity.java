package com.example.trolley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ReminderActivity extends AppCompatActivity {

    private List<String> items;
    private ArrayAdapter<String > itemAdapter;
    private ListView listView;
    private Button buttonAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);


        // Code for
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);



        listView = findViewById(R.id.listView);
        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        items = SharedPrefManager.getList(this);
        if(items == null)
            items = new ArrayList<>();

        itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemAdapter);
        deleteItem();

    }

    private void  deleteItem() {
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Context context = getApplicationContext();
//                Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();
//
//                items.remove(i);
//                itemAdapter.notifyDataSetChanged();
//                SharedPrefManager.saveList(getApplicationContext(), items);
//                return true;
//            }
//        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();

                items.remove(i);
                itemAdapter.notifyDataSetChanged();
                SharedPrefManager.saveList(getApplicationContext(), items);

            }
        });
    }

    private void addItem(View view) {
        EditText input = findViewById(R.id.editTextReminder);
        String editTextReminder =input.getText().toString();

        if(!(editTextReminder.equals(""))){
            itemAdapter.add(editTextReminder);
            SharedPrefManager.saveList(getApplicationContext(), items);
            input.setText("");

        }
        else{
            Toast.makeText(getApplicationContext(), "Please enter text..", Toast.LENGTH_SHORT).show();
        }
    }
}