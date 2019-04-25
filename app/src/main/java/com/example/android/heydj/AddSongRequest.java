package com.example.android.heydj;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.heydj.SpotifyClasses.HeyDJProperties;
import com.example.android.heydj.SpotifyClasses.JSONResultsList;
import com.example.android.heydj.SpotifyClasses.SpotifyClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<String> songLists;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_request);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        mContext = getApplicationContext();

        mDj = (TextView) findViewById(R.id.displayDjNameRequest);

        String s = getIntent().getStringExtra("DjName");
        mDj.setText("Request songs to: " + s);

        mArtistName = (EditText) findViewById(R.id.editText2);



        recyclerView = (RecyclerView) findViewById(R.id.search_result_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        songLists = new ArrayList<String>();



        adapter = new TrackAdapter(getApplicationContext(), songLists);


        //clicking on the floating action button to test the returned api query result
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String songTitle = "";
                try
                {
                    Log.d("Search String", mArtistName.getText().toString());

                    HeyDJProperties props = new HeyDJProperties();

                    SpotifyClient client = new SpotifyClient(
                            props.getClientId(),
                            props.getClientSecret(),
                            props.getRefreshToken()
                    );

                    String result = client.search("track:" + mArtistName.getText().toString(), "track", 10);
                    Log.d("DUMP", result);
                    try
                    {
                        JSONObject obj = new JSONObject(result);
                        JSONObject tracks = obj.getJSONObject("tracks");
                        JSONArray items = tracks.getJSONArray("items");


                        int n = items.length();


                        for(int i = 0; i < n; i++)
                        {
                            JSONObject name = items.getJSONObject(i);

                            JSONArray artists = name.getJSONArray("artists");
                            JSONArray images = name.getJSONArray("images");

                            Log.d("INFO", name.getString("name"));

                            for(int j = 0; j < artists.length(); j++)
                            {
                                JSONObject artist = artists.getJSONObject(j);

                                songLists.add(artist.getString("name") +  " - " +name.getString("name"));
                            }


                        }
                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }catch(IOException e)
                {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();

            }
        });

        recyclerView.setAdapter(adapter);
        clear();
        mArtistName.getText().clear();
        adapter.notifyDataSetChanged();

    }

    public void clear() {
        final int size = songLists.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                songLists.remove(0);
            }

            adapter.notifyItemRangeRemoved(0, size);
        }
    }

    public static Context getContext()
    {
        return mContext;
    }
}
