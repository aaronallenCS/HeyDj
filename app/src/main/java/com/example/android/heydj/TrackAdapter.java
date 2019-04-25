package com.example.android.heydj;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.heydj.SpotifyClasses.JSONResultsList;

import java.util.LinkedList;
import java.util.List;


public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder>
{
    private final List<String> mSongList;

    private LayoutInflater mInflater;

    public TrackAdapter(Context context, List<String> mSongList)
    {
        mInflater = LayoutInflater.from(context);
        this.mSongList = mSongList;

    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View mItemView = mInflater.inflate(R.layout.tracklist_adapter, parent, false);

        return new TrackViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position)
    {
        String mCurrent = mSongList.get(position);

//        holder.artistItemView.setText(mCurrent);
        holder.songItemView.setText(mCurrent);
    }

    class TrackViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView songItemView;
        private final ImageView albumImage;

//        private final TextView artistItemView;
        final TrackAdapter mAdapter;

        public TrackViewHolder(View itemView, TrackAdapter adapter)
        {
            super(itemView);
            this.mAdapter = adapter;

            songItemView = (TextView) itemView.findViewById(R.id.song_name_result);
            albumImage = (ImageView) itemView.findViewById(R.id.albumArt);

        }

    }

    @Override
    public int getItemCount()
    {
        return mSongList.size();
    }
}
