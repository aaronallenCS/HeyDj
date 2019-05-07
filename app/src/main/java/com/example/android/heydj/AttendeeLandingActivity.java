package com.example.android.heydj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.internal.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AttendeeLandingActivity extends AppCompatActivity {


    private FirebaseRecyclerAdapter<DjProfile, ResultsViewHolder> firebaseRecyclerAdapter;
    public static Query query;

    public static String searchData;
    DatabaseReference mProfileDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_landing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final RecyclerView recyclerView = findViewById(R.id.results_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final EditText t = (EditText) findViewById(R.id.DjNameET);

        mProfileDatabase = FirebaseDatabase.getInstance().getReference();
        final String newness = "";

        ImageButton imgB= (ImageButton) findViewById(R.id.searchBtn);

        imgB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                t.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if(i == EditorInfo.IME_ACTION_SEARCH)
                        {
                            searchData = t.getText().toString();

                            return true;
                        }
                        return false;
                    }
                });

                //queries all the information for every single dj name
                query = mProfileDatabase.child("allDeeJays")
                        .startAt(t.getText().toString()).endAt(t.getText().toString() + "\uf8ff");


                FirebaseRecyclerOptions<DjProfile> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<DjProfile>()
                        .setQuery(query, DjProfile.class)
                        .build();
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DjProfile, ResultsViewHolder>(firebaseRecyclerOptions)
                {

                    @NonNull
                    @Override
                    public ResultsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.searchitem_list, viewGroup, false);

                        return new ResultsViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ResultsViewHolder holder, final int position, @NonNull final DjProfile model) {
                        holder.setDjProfile(model);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getApplication(), AddSongRequest.class);
                                i.putExtra("DjName", model.getdjName());
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