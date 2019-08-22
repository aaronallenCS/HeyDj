package com.example.android.heydj;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "LandingActivity";
    protected GoogleSignInClient mGoogleSignInClient;
    protected GoogleSignInOptions gso;

    final int RC_SIGN_IN = 1;  // The request code


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        String djName = RegisterDJActivity.getDefaults("DjName", getApplicationContext());

        if (djName != null)
        {
            Intent i = new Intent(getApplicationContext(), DjLanding.class);
            startActivity(i);
        }

        SignInButton signInButton = findViewById(R.id.sign_out_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        setGooglePlusButtonText(signInButton, "Sign in with Google");

        findViewById(R.id.sign_out_button).setOnClickListener(this);



        Button b = (Button) findViewById(R.id.button_direct_attendee);

        b.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AttendeeLandingActivity.class);
                startActivity(i);
            }
        });
    }


    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText)
    {
        for(int i = 0; i < signInButton.getChildCount(); i++)
        {
            View v = signInButton.getChildAt(i);

            if(v instanceof TextView)
            {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }

    }

    private void updateUI(final GoogleSignInAccount gsa)
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(gsa.getDisplayName()))
                {
                    Intent i = new Intent(getApplicationContext(), ListDjActivity.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(getApplicationContext(), RegisterDJActivity.class);

                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.sign_out_button:
                signIn();
                break;
        }
    }

    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }



}
