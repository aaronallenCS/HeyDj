package com.song.request.heydj;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_request);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText t = findViewById(R.id.custom_song_name);


        Button b = findViewById(R.id.submit_custom_request);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t.getText() != null)
                {
                    FirebaseDatabase.getInstance().getReference().child(AddSongRequest.getAssociatedAcct).child("song").push().setValue(t.getText().toString());
                    Intent i = new Intent(getApplicationContext(), LandingActivity.class);
                    startActivity(i);
                    Toast.makeText(CustomRequestActivity.this, "Your request has been submitted!", Toast.LENGTH_SHORT);
                }
                else
                {
                    Toast.makeText(CustomRequestActivity.this, "Sorry, can't submit a blank request", Toast.LENGTH_SHORT);
                }
            }
        });

    }

}
