package com.example.trolley;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//here for this class we are using a singleton pattern

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_PHONE = "keyphone";
    private static final String KEY_ID = "keyid";

    private static final String KEY_INVOICEID = "keyinvoiceId";
    private static final String KEY_USERID = "keyuserId";
    private static final String KEY_DATE = "keydate";
    private static final String KEY_PAID = "keypaid";
    private static final String LIST_KEY = "keylist";
    private static final String NOTEPAD_KEY = "keynotepad";



    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_PHONE, user.getPhone());
        editor.apply();
    }


    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_PHONE, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }

    //method to let the user login
    //this method will store the invoice data in shared preferences
    public void userInvoice(Invoice invoice) {
        SharedPreferences sharedPreferencesInvoice = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorInvoice = sharedPreferencesInvoice.edit();
        editorInvoice.putInt(KEY_INVOICEID, invoice.getId());
        editorInvoice.putInt(KEY_USERID, invoice.getUserId());
        editorInvoice.putString(KEY_DATE, invoice.getDate());
        editorInvoice.putBoolean(KEY_PAID, invoice.getPaid());
        editorInvoice.apply();
    }

    //this method will give the created invoice of user
    public Invoice getInvoice() {
        SharedPreferences sharedPreferencesInvoice = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Invoice(
                sharedPreferencesInvoice.getInt(KEY_INVOICEID, -1),
                sharedPreferencesInvoice.getInt(KEY_USERID, -1),
                sharedPreferencesInvoice.getString(KEY_DATE, null),
                sharedPreferencesInvoice.getBoolean(KEY_PAID,false)
        );
    }


    //this method will save reminder List
    public static void saveList(Context context, List<String> list){
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        SharedPreferences pref = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorList = pref.edit();
        editorList.putString(LIST_KEY, jsonString);
        editorList.apply();
    }

    //this method will return reminder List
    public static List<String> getList(Context context){
        SharedPreferences pref = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String jsonString = pref.getString(LIST_KEY, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        List<String> list = gson.fromJson(jsonString, type);
        return list;
    }

    //this method will save Notepad content
    public static void saveNotepad(Context context, String data){
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorNotepad = preferences.edit();
        editorNotepad.putString(NOTEPAD_KEY, data);
        editorNotepad.apply();
    }

    //this method will return Notepad Content
    public static String getNotepad(Context context){
        SharedPreferences preferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String data = preferences.getString(NOTEPAD_KEY, "");

        return data;
    }

}