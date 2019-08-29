package com.example.android.heydj;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                                mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
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
