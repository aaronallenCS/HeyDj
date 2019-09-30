package com.song.request.heydj;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.song.request.heydj.Fragments.AboutMeFragment;
import com.song.request.heydj.Fragments.HelpFragment;
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
import com.google.firebase.database.ValueEventListener;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DjLanding extends AppCompatActivity implements HelpFragment.OnFragmentInteractionListener, AboutMeFragment.OnFragmentInteractionListener
{
    DatabaseReference mProfileDatabase;
    DrawerLayout mDrawer;
    NavigationView navDrawer;
    ArrayList<String> songNames;
    public ArrayList<POJO> arrayList = new ArrayList<>();
    public ArrayList<POJO> newList = new ArrayList<POJO>();
    public CustomRecyclerAdapter adapter = new CustomRecyclerAdapter(DjLanding.this,arrayList);
    public RecyclerView recyclerView;
    public String djName;
    public static final int SELECT_IMAGE = 1;
    public int pickerValue;
    public static GoogleApiClient mGoogleApiClient;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj_landing);

        CollapsingToolbarLayout tb = findViewById(R.id.collapsing_toolbar_layout);

        tb.setTitleEnabled(false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getIntent().getStringExtra("djName"));

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        navDrawer = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navDrawer);

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        recyclerView = findViewById(R.id.display_name_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        djName = getIntent().getStringExtra("djName");

        mProfileDatabase = FirebaseDatabase.getInstance().getReference();

        songNames = new ArrayList<String>();

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = "";
                int x = 0;
                for(String i:songNames){
                    if(x<=100)
                        shareBody+=i+"\n";
                    x++;
                }
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Share");
                sendIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(sendIntent, "Share songs to..."));
            }
        });



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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child(getIntent().getStringExtra("djName")).child("profile_image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.getValue() != null)
                {
                    try
                    {
                        String value = dataSnapshot.getValue(String.class);
                        Log.d("IMAGEPULL", value);
                        Bitmap imageBitmap = decodeFromFirebaseBase64(value);
                        circleImageView.setImageBitmap(imageBitmap);
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mProfileDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                mProfileDatabase.child(getIntent().getStringExtra("djName")).child("song")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<String> sArr = new ArrayList<String>();
                                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    sArr.add(snapshot.getValue(String.class));
                                }

                                HashMap<String, Integer> songCounts = new HashMap<>();
                                songNames.clear();
                                arrayList.clear();
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

                                HashMap<String, Integer> hashSorted = sortByComparator(songCounts, false);


                                for(Map.Entry<String,Integer> s : hashSorted.entrySet())
                                {
                                    POJO pojo = new POJO(s.getKey(), s.getValue());
                                    songNames.add(s.getKey()+" ("+s.getValue()+")");
                                    arrayList.add(pojo);
                                }

                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        View navHeader = navDrawer.getHeaderView(0);
        circleImageView = navHeader.findViewById(R.id.dj_pic);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).show(DjLanding.this)
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                if(r.getError()==null){
                                    circleImageView.setImageBitmap(r.getBitmap());
                                    encodeBitmapAndSaveToFirebase(r.getBitmap());
                                }
                            }
                        });
            }
        });
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException
    {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);

    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(getIntent().getStringExtra("djName")).child("profile_image").setValue(imageEncoded);
    }


    private void setupDrawerContent(NavigationView navigationView)
    {
        navDrawer.setNavigationItemSelectedListener
                (
                        new NavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                                selectDrawerItem(menuItem);
                                return true;
                            }
                        }
                );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.overflow_menu_dj, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()) {
            case R.id.DeleteAll:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DjLanding.this);
                alertDialog.setTitle("Notice");
                alertDialog.setMessage("If there are songs in the list, would you like to delete them?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrayList.clear();
                        newList.clear();
                        mProfileDatabase.child(getIntent().getStringExtra("djName")).child("song").removeValue();
                        adapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

                return true;
            case R.id.filter_songs:
                if (arrayList.size() == 0)
                {
                    Toast.makeText(DjLanding.this, "Sorry, you don't have any songs to filter", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    View customLayout = getLayoutInflater().inflate(R.layout.filter_songs, null);

                    NumberPicker np =  customLayout.findViewById(R.id.numberPicker);
                    String[] nums = new String[arrayList.size()];
                    for (int i = 0; i < nums.length; i++)
                    {
                        nums[i] = Integer.toString(i);
                    }

                    np.setMinValue(1);
                    np.setMaxValue(arrayList.size());
                    np.setWrapSelectorWheel(false);
                    np.setDisplayedValues(nums);
                    np.setValue(1);

                    np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            pickerValue = newVal;
                        }
                    });

                    AlertDialog myDialog = new AlertDialog.Builder(this)
                            .setTitle("How many songs would you like to display?")
                            .setView(customLayout)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                            {


                                @Override
                                public void onClick(DialogInterface dialogInterface, int i)
                                {
                                    recyclerView.removeAllViewsInLayout();

                                    mProfileDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                        {

                                            mProfileDatabase.child(djName).child("song")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            djName = getIntent().getStringExtra("djName");

                                                            List<String> sArr = new ArrayList<String>();
                                                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                                sArr.add(snapshot.getValue(String.class));
                                                            }

                                                            HashMap<String, Integer> songCounts = new HashMap<>();
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

                                                            HashMap<String, Integer> hashSorted = sortByComparator(songCounts, false);


                                                            for(Map.Entry<String,Integer> s : hashSorted.entrySet())
                                                            {
                                                                POJO pojo = new POJO(s.getKey(), s.getValue());
                                                                arrayList.add(pojo);
                                                            }

                                                            Log.d("HERES THE VALUE", "HE" +  pickerValue);
                                                            for(int i = 0; i < pickerValue - 1; i++)
                                                            {
                                                                newList.add(arrayList.get(i));
                                                            }

                                                            adapter = new CustomRecyclerAdapter(DjLanding.this, newList);

                                                            recyclerView.setAdapter(adapter);
                                                            adapter.notifyDataSetChanged();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            })
                            .create();

                    arrayList.clear();
                    myDialog.show();
                }
                return true;
        }
        return false;
    }

    private static HashMap<String, Integer> sortByComparator(HashMap<String, Integer> unsortMap, final boolean order)
    {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public void selectDrawerItem(MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.hey_dj:
                String url = "http://www.heyydj.com";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                break;
            case R.id.linked_in:
                String url2 = "https://www.linkedin.com/in/aallencs";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url2)));
                break;
            case R.id.youtube:
                String url3 = "https://www.youtube.com/channel/UCszZeOR5O-3ZP_4Az7ziP1g?view_as=subscriber";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url3)));
                break;
            case R.id.support:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","arratronics@gmail.com", null));
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
        }
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public class POJO
    {
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


    public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>
    {

        private Context context;
        private ArrayList<POJO> personUtils;

        public CustomRecyclerAdapter(Context context, ArrayList<POJO> personUtils) {
            this.context = context;
            this.personUtils = personUtils;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.djname_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.itemView.setTag(personUtils.get(position));
            POJO pu = personUtils.get(position);
            holder.djNameView.setText(pu.getName());
            holder.badge.setText(pu.getCount() + "");
        }

        @Override
        public int getItemCount() {
            return personUtils.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView djNameView;
            public TextView badge;

            public ViewHolder(View itemView) {
                super(itemView);

                djNameView = itemView.findViewById(R.id.song_result_dj);
                badge = itemView.findViewById(R.id.song_badge);
            }
        }
    }
}