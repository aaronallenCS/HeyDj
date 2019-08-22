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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder>
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
    public void onBindViewHolder(TrackViewHolder holder, int position)
    {
        mCurrent = mSongList.get(position);
        holder.songItemView.setText(mCurrent);
    }



    @Override
    public TrackViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        final View mItemView = mInflater.inflate(R.layout.tracklist_adapter, parent, false);

        final TrackViewHolder vHolder = new TrackViewHolder(mItemView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(parent.getContext());
        alertDialog.setTitle("Song");
        alertDialog.setMessage("Are you sure you want to request " + mSongList.get(vHolder.getAdapterPosition() + 1));
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                FirebaseApp.initializeApp(parent.getContext());

                        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                FirebaseDatabase.getInstance().getReference().child(AddSongRequest.getAssociatedAcct).child("song").push().setValue(mSongList.get(vHolder.getAdapterPosition() + 1));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = alertDialog.create();
        vHolder.songItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.show();
            }
        });

        vHolder.buttonViewOption.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(final View view)
            {
                //pop up menu for opening spotify
                PopupMenu popup = new PopupMenu(parent.getContext(), vHolder.buttonViewOption);

                popup.inflate(R.menu.spotify_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        switch(menuItem.getItemId())
                        {
                            case R.id.spotify_menu_item:
                                final String appPackageName = "com.spotify.music";
                                final String referrer = "adjust_campaign=PACKAGE_NAME&" +
                                        "adjust_tracker=ndjczk&utm_source=adjust_preinstall";

                                try
                                {
                                    Uri uri = Uri.parse("market://details")
                                            .buildUpon()
                                            .appendQueryParameter("id", appPackageName)
                                            .appendQueryParameter("referrer", referrer)
                                            .build();

                                    parent.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                                }
                                catch(android.content.ActivityNotFoundException ignored)
                                {
                                    Uri uri = Uri.parse("https://play.google.com/store/apps/details")
                                            .buildUpon()
                                            .appendQueryParameter("id", appPackageName)
                                            .appendQueryParameter("referrer", referrer)
                                            .build();

                                    parent.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                                }
                                return true;
                        }

                        return false;
                    }
                });

                popup.show();
            }
        });


        return new TrackViewHolder(mItemView);
    }


    class TrackViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView songItemView;
        private final TextView buttonViewOption;

        public TrackViewHolder(View itemView)
        {
            super(itemView);

            songItemView = (TextView) itemView.findViewById(R.id.song_name);
            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
        }

        public String getText()
        {
            return songItemView.getText().toString();
        }
    }

    @Override
    public int getItemCount()
    {
        return mSongList.size();
    }
}
