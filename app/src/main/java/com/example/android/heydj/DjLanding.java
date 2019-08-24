package com.example.android.heydj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.heydj.Fragments.Settings;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class DjLanding extends AppCompatActivity implements Settings.OnFragmentInteractionListener
{
    public String personName;

    private FirebaseRecyclerAdapter<DataSnapshot, ResultsViewHolder> firebaseRecyclerAdapter;
    public Query query;

    DatabaseReference mProfileDatabase;
    DrawerLayout mDrawer;
    NavigationView navDrawer;
    List<String> songNames;
    TextView songCountBadge;
    public Map<String, Integer> countSongOccurence = new HashMap<String, Integer>();

    public FirebaseListAdapter<String> myAdapter;


    public static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj_landing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        navDrawer = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navDrawer);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        final RecyclerView recyclerView = findViewById(R.id.display_name_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        TextView djNamess = findViewById(R.id.list_user_result_dj);

        Intent i = getIntent();

        final String djName = i.getStringExtra("djName");

        mProfileDatabase = FirebaseDatabase.getInstance().getReference();

        Button mButtonSignOut = (Button) findViewById(R.id.sign_out_button);
        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>()
                        {
                            @Override
                            public void onResult(Status status)
                            {
                                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT);
                                Intent i = new Intent(getApplicationContext(), LandingActivity.class);
                                startActivity(i);
                            }
                        }
                );
            }
        });
        songNames = new ArrayList<String>();

        mProfileDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Log.d("THIS", djName);
                query = mProfileDatabase.child(djName).orderByKey();

                FirebaseRecyclerOptions<DataSnapshot> firebaseRecyclerOptions =
                        new FirebaseRecyclerOptions.Builder<DataSnapshot>()
                        .setQuery(query, new SnapshotParser<DataSnapshot>(){
                            @NonNull
                            @Override
                            public DataSnapshot parseSnapshot(@NonNull DataSnapshot snapshot)
                            {
                                return snapshot;
                            }

                        }).build();



                firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<DataSnapshot, ResultsViewHolder>(firebaseRecyclerOptions)
                        {

                            @Override
                            protected void onBindViewHolder(@NonNull ResultsViewHolder holder, int position, @NonNull DataSnapshot model)
                            {

                                List<String> sArr = new ArrayList<String>();
                                for(DataSnapshot snapshot : model.getChildren())
                                {
                                    sArr.add(snapshot.getValue(String.class));
                                }

                                LinkedHashMap<String, Integer> songCounts = new LinkedHashMap<>();
                                for(int i = 0; i < sArr.size(); i++)
                                {
                                    String eachSong = sArr.get(i);
                                    if(songCounts.containsKey(eachSong))
                                    {
                                        int count = songCounts.get(eachSong);
                                        songCounts.put(eachSong, count + 1);
                                    }
                                    else
                                    {
                                        songCounts.put(eachSong, 1);
                                    }
                                }
                                ArrayList<POJO> arrayList = new ArrayList<>();
                                for(Map.Entry<String,Integer> s:songCounts.entrySet()){
                                    POJO pojo = new POJO(s.getKey(),s.getValue());
                                    arrayList.add(pojo);
                                }
                                holder.setData(arrayList);
                            }


                            @NonNull
                            @Override
                            public ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                            {
                                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.djlanding_linear, viewGroup, false);
                                return new ResultsViewHolder(view);
                            }
                        };

                firebaseRecyclerAdapter.startListening();
                recyclerView.setAdapter(firebaseRecyclerAdapter);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setupDrawerContent(NavigationView navigationView)
    {
        navDrawer.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                }
        );
    }

    public void selectDrawerItem(MenuItem menuItem)
    {
        Fragment fragment = null;
        Class fragmentClass;

        switch(menuItem.getItemId())
        {
            case R.id.nav_third_fragment:
                fragmentClass = Settings.class;
                break;
            default:
                fragmentClass = Settings.class;
                break;
        }

        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        FrameLayout fragmentLayout = new FrameLayout(this);
        fragmentLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        fragmentLayout.setId(R.id.flContent);

        setContentView(fragmentLayout);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flContent, new Settings()).commit();
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int stackHeight = getSupportFragmentManager().getBackStackEntryCount();
                if (stackHeight > 0) {
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                }
            }
        });

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onStart()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleApiClient.connect();
        super.onStart();
    }



    public class ResultsViewHolder extends RecyclerView.ViewHolder
    {
        //private TextView djNameView;
        //private TextView badge;
        private LinearLayout linear;


        public ResultsViewHolder(View itemView)
        {
            super(itemView);
            linear = itemView.findViewById(R.id.linear);
            //djNameView = itemView.findViewById(R.id.song_result_dj);
            //badge = itemView.findViewById(R.id.song_badge);
        }
        void setData(ArrayList<POJO> pojo)
        {
            for(POJO p:pojo) {
                View child = getLayoutInflater().inflate(R.layout.djname_item, null);
                TextView djNameView = child.findViewById(R.id.song_result_dj);
                TextView badge = child.findViewById(R.id.song_badge);
                djNameView.setText(p.getName());
                badge.setText(p.getCount()+"");
                linear.addView(child);
            }
            //djNameView.setText(pojo.getName());
            //badge.setText(pojo.getCount()+"");
        }

    }

    public class POJO{
        String name;
        Integer count;

        public POJO(String name, Integer count) {
            this.name = name;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }


}
