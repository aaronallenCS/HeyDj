package com.example.android.heydj;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackViewHolder>
{
    private final List<String> mSongList;
    private LayoutInflater mInflater;
    private String mCurrent;


    public TrackAdapter(Context context, List<String> mSongList)
    {
        mInflater = LayoutInflater.from(context);
        this.mSongList = mSongList;
    }

    @Override
    public void onBindViewHolder(final TrackViewHolder holder, final int position)
    {
        mCurrent = mSongList.get(position);
        holder.songItemView.setText(mCurrent);

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getRootView().getContext());

                builder1.setMessage("Are you sure you want to request: " + holder.songItemView.getText().toString());
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                FirebaseDatabase.getInstance().getReference().child(AddSongRequest.getAssociatedAcct).child("song").push().setValue(holder.songItemView.getText().toString());
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert1 = builder1.create();
                alert1.show();
            }
        });
    }



    @Override
    public TrackViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        final View mItemView = mInflater.inflate(R.layout.tracklist_adapter, parent, false);

        final TrackViewHolder vHolder = new TrackViewHolder(mItemView);


        return new TrackViewHolder(mItemView);
    }



    @Override
    public int getItemCount()
    {
        return mSongList.size();
    }
}
