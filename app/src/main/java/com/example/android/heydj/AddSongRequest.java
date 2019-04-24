package com.example.android.heydj;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.heydj.SpotifyClasses.HeyDJProperties;
import com.example.android.heydj.SpotifyClasses.SpotifyClient;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Collection;

import de.umass.lastfm.Artist;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Chart;
import de.umass.lastfm.Track;
import de.umass.lastfm.User;
import saschpe.discogs.Discogs;

public class AddSongRequest extends AppCompatActivity
{
    TextView mDj;
    Button mValidateBtn;

    EditText mArtistName;
    EditText mSongName;

    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_request);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = getApplicationContext();

        mDj = (TextView) findViewById(R.id.displayDjNameRequest);

        String s = getIntent().getStringExtra("DjName");
        mDj.setText("Request songs to: " + s);

        mValidateBtn = (Button) findViewById(R.id.validate_song_btn);


        mValidateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mArtistName = (EditText) findViewById(R.id.editText2);
        final String search_string = mArtistName.getText().toString();


        //clicking on the floating action button to test the returned api query result
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    HeyDJProperties props = new HeyDJProperties();

                    SpotifyClient client = new SpotifyClient(
                            props.getClientId(),
                            props.getClientSecret(),
                            props.getRefreshToken()
                    );

                    String result = client.search("track:" + search_string, "track", 10);
                    Log.d("THIS IS THE OUTPUT: ", result);
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public static Context getContext()
    {
        return mContext;
    }
}
