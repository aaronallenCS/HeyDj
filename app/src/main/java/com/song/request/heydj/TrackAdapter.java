package com.song.request.heydj;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.song.request.heydj.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackViewHolder>
{
    private final List<String> mSongList;
    private LayoutInflater mInflater;
    private String mCurrent;

    private InterstitialAd mInterstitialAd;

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
        MobileAds.initialize(holder.itemView.getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

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
                                mInterstitialAd = new InterstitialAd(holder.itemView.getContext());
                                mInterstitialAd.setAdUnitId("ca-app-pub-4170331321992813/1417147004");
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                                mInterstitialAd.setAdListener(new AdListener(){
                                    public void onAdLoaded()
                                    {
                                        if(mInterstitialAd.isLoaded())
                                        {
                                            mInterstitialAd.show();
                                        }
                                    }
                                });
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

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                android.widget.PopupMenu popup = new android.widget.PopupMenu(view.getContext(), holder.buttonViewOption);
                popup.getMenuInflater().inflate(R.menu.spotify_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
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
                                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                                catch(android.content.ActivityNotFoundException ignored)
                                {
                                    Uri uri = Uri.parse("https://play.google.com/store/apps/details")
                                            .buildUpon()
                                            .appendQueryParameter("id", appPackageName)
                                            .appendQueryParameter("referrer", referrer)
                                            .build();

                                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
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
