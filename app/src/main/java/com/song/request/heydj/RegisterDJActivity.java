package com.song.request.heydj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.song.request.heydj.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


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


        final LinearLayout linView = findViewById(R.id.register_lin_view);
        // Set up the login form.
        mDjName = (AutoCompleteTextView) findViewById(R.id.dj_name);
        FirebaseApp.initializeApp(this);

        rootRef = FirebaseDatabase.getInstance().getReference();

        AutoCompleteTextView aCTV = findViewById(R.id.dj_name);

        aCTV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(linView.getWindowToken(), 0);

                if(i == EditorInfo.IME_ACTION_SEARCH)
                {
                    //get the dj name the user typed in and entered
                    final String djName = mDjName.getText().toString();

                    //Get the last account signed in from the user
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(RegisterDJActivity.this);

                    //get the name of the google user
                    final String googleUserName = acct.getDisplayName();

                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String spaceLessDJ = djName.trim();
                            if(spaceLessDJ.trim().isEmpty())
                            {
                                Toast.makeText(RegisterDJActivity.this, "Sorry, you can't submit blanks", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {

                                //checks in the database to see if the user already has DJs
                                if (dataSnapshot.hasChild(googleUserName)) {
                                    if (dataSnapshot.hasChild(spaceLessDJ)) {
                                        return;
                                    }
                                    //pushes the value allDeeJays for every single user
                                    Map<String, Object> updates = new HashMap<String, Object>();
                                    updates.put("acct", googleUserName);
                                    updates.put("djName", spaceLessDJ.toLowerCase());
                                    rootRef.child("allDeeJays").push().updateChildren(updates);
                                    //pushes the value of the dj to the dj for the specific google user
                                    rootRef.child(googleUserName).child(spaceLessDJ.toLowerCase()).push().setValue(spaceLessDJ);
                                } else {
                                    Map<String, Object> updates = new HashMap<String, Object>();
                                    updates.put("djName", spaceLessDJ);
                                    //sets a new google username for ones that don't exist
                                    rootRef.child("allDeeJays").push().updateChildren(updates);
                                    rootRef.child(googleUserName).push().child("djName").setValue(spaceLessDJ.toLowerCase());
                                    Intent i2 = new Intent(getApplicationContext(), DjLanding.class);
                                    i2.putExtra("djName", spaceLessDJ);
                                    startActivity(i2);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                    Intent i2 = new Intent(getApplicationContext(), DjLanding.class);
                    i2.putExtra("djName", djName);
                    startActivity(i2);
                }
                return false;
            }
        });

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
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        String spaceLessDJ = djName.trim();
                        if(spaceLessDJ.trim().isEmpty())
                        {
                            Toast.makeText(RegisterDJActivity.this, "Sorry, you can't submit blanks", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            //checks in the database to see if the user already has DJs
                            if (dataSnapshot.hasChild(googleUserName)) {
                                if (dataSnapshot.hasChild(spaceLessDJ)) {
                                    return;
                                }
                                //pushes the value allDeeJays for every single user
                                Map<String, Object> updates = new HashMap<String, Object>();
                                updates.put("acct", googleUserName);
                                updates.put("djName", spaceLessDJ.toLowerCase());
                                rootRef.child("allDeeJays").push().updateChildren(updates);
                                //pushes the value of the dj to the dj for the specific google user
                                rootRef.child(googleUserName).child(spaceLessDJ.toLowerCase()).push().setValue(spaceLessDJ);
                            } else {
                                Map<String, Object> updates = new HashMap<String, Object>();
                                updates.put("djName", spaceLessDJ);
                                //sets a new google username for ones that don't exist
                                rootRef.child("allDeeJays").push().updateChildren(updates);
                                rootRef.child(googleUserName).push().child("djName").setValue(spaceLessDJ.toLowerCase());
                                Intent i2 = new Intent(getApplicationContext(), DjLanding.class);
                                i2.putExtra("djName", spaceLessDJ);
                                startActivity(i2);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


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

