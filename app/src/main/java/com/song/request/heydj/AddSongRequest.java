package com.song.request.heydj;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.song.request.heydj.R;
import com.song.request.heydj.SpotifyClasses.HeyDJProperties;
import com.song.request.heydj.SpotifyClasses.SpotifyClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddSongRequest extends AppCompatActivity
{
    TextView mDj;
    EditText mArtistName;
    protected static Context mContext;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private RecyclerView.Adapter adapter;
    private List<String> songLists;
    public static String djNameSelected;
    public static String getAssociatedAcct;

    private ImageButton imageButton;
    private ImageButton clearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_request);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mContext = getApplicationContext();
        mDj = (TextView) findViewById(R.id.displayDjNameRequest);

        djNameSelected = getIntent().getStringExtra("DjName");
        getAssociatedAcct = getIntent().getStringExtra("UserAcct");

        mDj.setText("Request songs to: " + djNameSelected);

        mArtistName = (EditText) findViewById(R.id.editText2);



        recyclerView = (RecyclerView) findViewById(R.id.search_result_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CoordinatorLayout cdl = findViewById(R.id.cdLayout);

        final EditText djSearch = findViewById(R.id.editText2);

        final ImageButton imgBClear = findViewById(R.id.clear);

        imageButton = findViewById(R.id.search);

        imgBClear.setVisibility(View.INVISIBLE);
        djSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH)
                {
                    imageButton.setVisibility(View.INVISIBLE);
                    imgBClear.setVisibility(View.VISIBLE);

                    imgBClear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            imgBClear.setVisibility(View.INVISIBLE);
                            imageButton.setVisibility(View.VISIBLE);
                            songLists.clear();
                            adapter.notifyDataSetChanged();
                            djSearch.setText("");
                        }
                    });

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(cdl.getWindowToken(), 0);
                    songLists.clear();
                    adapter.notifyDataSetChanged();
                    updateInfo();
                }
                return false;
            }
        });

        songLists = new ArrayList<String>();
        adapter = new TrackAdapter(getApplicationContext(), songLists);

        imageButton = findViewById(R.id.search);

        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imageButton.setVisibility(View.INVISIBLE);
                imgBClear.setVisibility(View.VISIBLE);
                imgBClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgBClear.setVisibility(View.INVISIBLE);
                        imageButton.setVisibility(View.VISIBLE);
                        djSearch.setText("");
                        songLists.clear();
                        adapter.notifyDataSetChanged();
                    }
                });

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(cdl.getWindowToken(), 0);
                songLists.clear();
                adapter.notifyDataSetChanged();
                updateInfo();
            }
        });
    }

    public void updateInfo()
    {

        try
        {
            recyclerView.setVisibility(View.VISIBLE);
            HeyDJProperties props = new HeyDJProperties();
            SpotifyClient client = new SpotifyClient
                    (
                            props.getClientId(),
                            props.getClientSecret(),
                            props.getRefreshToken()
                    );

            String result = client.search("track:" + mArtistName.getText().toString(), "track", 50);

            Log.d("RESULT API", result);

            try
            {
                JSONObject obj = new JSONObject(result);
                JSONObject tracks = obj.getJSONObject("tracks");
                JSONArray items = tracks.getJSONArray("items");

                for(int i = 0; i < items.length(); i++)
                {

                    //album object
                    JSONObject album = items.getJSONObject(i).getJSONObject("album");
                    //SHOULD be getting the images array but does not

                    //artist array
                    JSONArray artists = album.getJSONArray("artists");

                    JSONObject values  = items.getJSONObject(i);
                    String name = values.getString("name");


                    //gets the necessary artist information
                    for(int j = 0; j < artists.length(); j++)
                    {
                        JSONObject artist = artists.getJSONObject(j);
                        songLists.add(artist.getString("name") +  " - " + name);
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
        recyclerView.setAdapter(adapter);
    }


    public static Context getContext()
    {
        return mContext;
    }
}
