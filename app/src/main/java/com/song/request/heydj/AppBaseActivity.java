package com.song.request.heydj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

public class AppBaseActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    String MY_PREFS_NAME = "MyPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
    }
}
