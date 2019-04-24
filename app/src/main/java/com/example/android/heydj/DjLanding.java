package com.example.android.heydj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DjLanding extends AppCompatActivity
{
    public static String personName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj_landing);


        TextView welcomeText = (TextView) findViewById(R.id.introTextView);
        Button mButtonSignOut = (Button) findViewById(R.id.sign_out_button);
        mButtonSignOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LandingActivity.mGoogleSignInClient.signOut();

                Intent i = new Intent(view.getContext(), LandingActivity.class);
                Toast.makeText(view.getContext(), "You have been signed out", Toast.LENGTH_LONG).show();
                startActivity(i);

            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(DjLanding.this);
        if (acct != null) {
            personName = acct.getDisplayName();
            String djNamessss = getIntent().getStringExtra("DjName");

            welcomeText.setText("Hey, " + djNamessss + "!");

            RecyclerView recyclerView = findViewById(R.id.display_name_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

}
