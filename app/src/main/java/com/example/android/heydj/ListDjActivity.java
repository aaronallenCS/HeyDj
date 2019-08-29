package com.example.android.heydj;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ListDjActivity extends AppCompatActivity
{
    private FirebaseRecyclerAdapter<DataSnapshot, ListResultsHolder> firebaseRecyclerAdapter;
    public Query query;

    DatabaseReference mProfileDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dj);


        final RecyclerView recyclerView = findViewById(R.id.list_curr_dj);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());


        mProfileDatabase = FirebaseDatabase.getInstance().getReference();
        mProfileDatabase.addListenerForSingleValueEvent(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                query = mProfileDatabase.child(acct.getDisplayName()).orderByChild("djName");

                Log.d("GET THAT ACCOUNT", acct.getDisplayName());
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


                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DataSnapshot, ListResultsHolder>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull ListResultsHolder holder, int position, @NonNull DataSnapshot model)
                    {
                        for(DataSnapshot ds : model.getChildren())
                        {
                            String name = ds.getValue(String.class);
                            DjProfile profile = new DjProfile(name);
                            profile.setDjName(name);
                            holder.setCurrentUserDj(profile);
                        }
                    }

                    @NonNull
                    @Override
                    public ListResultsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list_dj_item, viewGroup, false);

                        ListResultsHolder viewHolder = new ListResultsHolder(view);
                        viewHolder.setOnClickListener(new ListResultsHolder.ClickListener(){

                            @Override
                            public void onItemClick(View view, int position)
                            {
                                TextView t = view.findViewById(R.id.list_user_result_dj);
                                Intent i = new Intent(view.getContext(), DjLanding.class);
                                i.putExtra("djName", t.getText().toString());
                                startActivity(i);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                Toast.makeText(view.getContext(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
                            }
                        });

                        return viewHolder;
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

    public static class ListResultsHolder extends RecyclerView.ViewHolder
    {
        private TextView djNameView;

        private ListResultsHolder.ClickListener mClickListener;

        public interface ClickListener
        {
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(ListResultsHolder.ClickListener clickListener)
        {
            mClickListener = clickListener;
        }

        public ListResultsHolder(View itemView)
        {
            super(itemView);
            djNameView = itemView.findViewById(R.id.list_user_result_dj);

            itemView.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View view) {
                    mClickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }

        void setCurrentUserDj(DjProfile profile)
        {
            String djName = profile.getdjName();
            djNameView.setText(djName);
        }
    }
}
