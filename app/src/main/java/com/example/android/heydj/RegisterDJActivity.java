package com.example.android.heydj;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;


/**
 * A login screen that offers login via email/password.
 */
public class RegisterDJActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    // UI references.
    private AutoCompleteTextView mDjName;

    private static String getDjName;

    DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_dj);


        // Set up the login form.
        mDjName = (AutoCompleteTextView) findViewById(R.id.dj_name);
        FirebaseApp.initializeApp(this);

        rootRef = FirebaseDatabase.getInstance().getReference();

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the dj name the user typed in and entered
                final String djName = mDjName.getText().toString();

                //Get the last account signed in from the user
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(RegisterDJActivity.this);

                //get the name of the google user
                final String googleUserName = acct.getDisplayName();

                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //checks in the database to see if the user already has DJs
                        if (dataSnapshot.hasChild(googleUserName)) {
                            if (dataSnapshot.hasChild(djName)) {
                                return;
                            }
                            //pushes the value allDeeJays for every single user
                            Map<String, Object> updates = new HashMap<String, Object>();
                            updates.put("acct", googleUserName);
                            updates.put("djName", djName);
                            rootRef.child("allDeeJays").push().updateChildren(updates);
                            //pushes the value of the dj to the dj for the specific google user
                            rootRef.child(googleUserName).child(djName).push().setValue(djName);
                        } else {
                            Map<String, Object> updates = new HashMap<String, Object>();
                            updates.put("djName", djName);
                            //sets a new google username for ones that don't exist
                            rootRef.child("allDeeJays").push().updateChildren(updates);
                            rootRef.child(googleUserName).push().child("djName").setValue(djName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }
                });

                Intent i = new Intent(getApplicationContext(), DjLanding.class);
                i.putExtra("djName", djName);
                startActivity(i);
            }
        });
    }


    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaults(String key, Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}

