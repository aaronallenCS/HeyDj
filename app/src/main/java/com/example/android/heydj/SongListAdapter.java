package com.example.android.heydj;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongListViewHolder>
{
    private final LinkedList<String> mWordList;

    private LayoutInflater mInflater;

    public SongListAdapter(Context context, LinkedList<String> mWordList)
    {
        mInflater = LayoutInflater.from(context);
        this.mWordList = mWordList;
    }

    @Override
    public SongListViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View mItemView = mInflater.inflate(R.layout.djname_item, parent, false);

        return new SongListViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(SongListViewHolder holder, int position)
    {
        String mCurrent = mWordList.get(position);
        holder.wordItemView.setText(mCurrent);
    }

    class SongListViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;
        final SongListAdapter mAdapter;

        public SongListViewHolder(View itemView, SongListAdapter adapter)
        {
            super(itemView);

            wordItemView = itemView.findViewById(R.id.dj_name_result);
            this.mAdapter = adapter;
        }

    }

    @Override
    public int getItemCount()
    {
        return mWordList.size();
    }
}
