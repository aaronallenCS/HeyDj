package com.example.android.heydj;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AttendeeLandingActivity extends AppCompatActivity {


    private FirebaseRecyclerAdapter<DataSnapshot, ResultsViewHolder> firebaseRecyclerAdapter;
    public static Query query;
    public static String searchData;
    DatabaseReference mProfileDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_landing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final CoordinatorLayout cdl = findViewById(R.id.cdAttendee);



        final RecyclerView recyclerView = findViewById(R.id.results_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        final EditText t = (EditText) findViewById(R.id.DjNameET);
        final TextView djResultsHeader = (TextView) findViewById(R.id.dj_results_header);

        djResultsHeader.setVisibility(View.GONE);
        mProfileDatabase = FirebaseDatabase.getInstance().getReference();

        t.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(cdl.getWindowToken(), 0);
                if(i == EditorInfo.IME_ACTION_SEARCH)
                {
                    djResultsHeader.setVisibility(View.VISIBLE);
                    searchData = t.getText().toString();

                    query = mProfileDatabase.child("allDeeJays").orderByChild("djName")
                            .startAt(t.getText().toString()).endAt(t.getText().toString() + "\uf8ff");


                    FirebaseRecyclerOptions<DataSnapshot> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<DataSnapshot>()
                            .setQuery(query, new SnapshotParser<DataSnapshot>() {
                                @Override
                                public DataSnapshot parseSnapshot(DataSnapshot snapshot) {
                                    return snapshot;
                                }
                            }).build();

                    firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataSnapshot, ResultsViewHolder>(firebaseRecyclerOptions)
                    {

                        @NonNull
                        @Override
                        public ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                        {
                            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchitem_list, viewGroup, false);
                            return new ResultsViewHolder(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull ResultsViewHolder holder, final int position, @NonNull final DataSnapshot snapshot) {
                            // Here you convert the DataSnapshot to whatever data your ViewHolder needs
                            String s = "";
                            for(DataSnapshot ds : snapshot.getChildren())
                            {
                                s = ds.getValue(String.class);
                                DjProfile model = new DjProfile(s);
                                model.setDjName(s);

                                holder.setDjProfile(model);
                            }

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {

                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("allDeeJays");

                                    String name = "";
                                    String acct = "";
                                    for(DataSnapshot ds : snapshot.getChildren())
                                    {
                                        name = ds.getValue(String.class);
                                        acct = snapshot.getChildren().iterator().next().getValue().toString();
                                        DjProfile model = new DjProfile(name);
                                        model.setDjName(name);
                                    }
                                    Intent i = new Intent(getApplication(), AddSongRequest.class);
                                    i.putExtra("DjName", name);
                                    i.putExtra("UserAcct", acct);
                                    startActivity(i);
                                }
                            });
                        }
                    };
                    firebaseRecyclerAdapter.startListening();
                    recyclerView.setAdapter(firebaseRecyclerAdapter);

                    return true;
                }
                return false;
            }
        });

        ImageButton imgB= (ImageButton) findViewById(R.id.searchBtn);
        imgB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(cdl.getWindowToken(), 0);
                djResultsHeader.setVisibility(View.VISIBLE);
                // in case the search button is clicked
                query = mProfileDatabase.child("allDeeJays").orderByChild("djName")
                        .startAt(t.getText().toString()).endAt(t.getText().toString() + "\uf8ff");



                FirebaseRecyclerOptions<DataSnapshot> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<DataSnapshot>()
                        .setQuery(query, new SnapshotParser<DataSnapshot>() {
                            @Override
                            public DataSnapshot parseSnapshot(DataSnapshot snapshot) {
                                return snapshot;
                            }
                        }).build();

                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataSnapshot, ResultsViewHolder>(firebaseRecyclerOptions)
                {

                    @NonNull
                    @Override
                    public ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchitem_list, viewGroup, false);
                        return new ResultsViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ResultsViewHolder holder, final int position, @NonNull final DataSnapshot snapshot) {
                        // Here you convert the DataSnapshot to whatever data your ViewHolder needs
                        String s = "";
                        for(DataSnapshot ds : snapshot.getChildren())
                        {
                            s = ds.getValue(String.class);
                            DjProfile model = new DjProfile(s);
                            model.setDjName(s);

                            holder.setDjProfile(model);
                        }

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {

                                String name = "";
                                String acct = "";
                                for(DataSnapshot ds : snapshot.getChildren())
                                {
                                    name = ds.getValue(String.class);
                                    acct = snapshot.getChildren().iterator().next().getValue().toString();
                                    DjProfile model = new DjProfile(name);
                                    model.setDjName(name);
                                }
                                Intent i = new Intent(getApplication(), AddSongRequest.class);
                                i.putExtra("DjName", name);
                                i.putExtra("UserAcct", acct);
                                startActivity(i);
                            }
                        });
                    }
                };
                firebaseRecyclerAdapter.startListening();
                recyclerView.setAdapter(firebaseRecyclerAdapter);
            }
        });
    }

    public static class ResultsViewHolder extends RecyclerView.ViewHolder
    {
        private TextView djNameView;

        public ResultsViewHolder(View itemView)
        {
            super(itemView);
            djNameView = itemView.findViewById(R.id.result_dj);
        }

        void setDjProfile(DjProfile profile)
        {
            String djName = profile.getdjName();
            djNameView.setText(djName);
        }
    }


}