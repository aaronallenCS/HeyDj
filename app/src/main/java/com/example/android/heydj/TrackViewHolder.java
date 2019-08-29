package com.example.android.heydj;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class TrackViewHolder extends RecyclerView.ViewHolder {
    public TextView songItemView;
    public TextView buttonViewOption;


    private TrackViewHolder.ClickListener mClickListener;

    public TrackViewHolder(View itemView)
    {
        super(itemView);

        songItemView = (TextView) itemView.findViewById(R.id.song_name);
        buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);


        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
    }


    public interface ClickListener
    {
        void onItemClick(View view, int position);
    }


    public void setOnClickListener(TrackViewHolder.ClickListener mClickListener)
    {
        this.mClickListener = mClickListener;
    }

    public String getText()
    {
        return songItemView.getText().toString();
    }
}
