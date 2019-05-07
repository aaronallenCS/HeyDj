package com.example.android.heydj;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {
    private final List<String> mSongList;
    private final List<String> imageUrl;

    private LayoutInflater mInflater;
    private Context context;

    private String mCurrent;

    public TrackAdapter(Context context, List<String> mSongList, List<String> imageUrl)
    {
        mInflater = LayoutInflater.from(context);
        this.mSongList = mSongList;
        this.imageUrl = imageUrl;
        this.context = context;

    }



    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position)
    {
        mCurrent = mSongList.get(position);
        String mCurrImage = imageUrl.get(position + 2);

        Picasso.get().load(mCurrImage).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.albumImage);
        holder.songItemView.setText(mCurrent);
    }

    @Override
    public TrackViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        View mItemView = mInflater.inflate(R.layout.tracklist_adapter, parent, false);

        final TrackViewHolder vHolder = new TrackViewHolder(mItemView);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(parent.getContext());
        alertDialog.setTitle("Song");
        alertDialog.setMessage("Are you sure you want to request " + mSongList.get(vHolder.getAdapterPosition() + 1));
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference rootRef, demoRef;

                FirebaseApp.initializeApp(parent.getContext());

                rootRef = FirebaseDatabase.getInstance().getReference();

                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(parent.getContext());
//                acct.getDisplayName();
//                demoRef = rootRef.child("djprofile").child();
//                if (AddSongRequest.djNameSelected.equals(acct.getDisplayName()))
//                {
//
//                }
                rootRef.child(acct.getDisplayName()).child("djName").child(acct.getDisplayName()).child(AddSongRequest.djNameSelected.toString()).push().setValue(mSongList.get(vHolder.getAdapterPosition() + 1));

                Toast.makeText(parent.getContext(), "Here is the signed in user: "  + acct.getDisplayName(), Toast.LENGTH_LONG).show();
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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


        return new TrackViewHolder(mItemView);
    }
    class TrackViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView songItemView;
        private final ImageView albumImage;

        public TrackViewHolder(View itemView)
        {
            super(itemView);


            songItemView = (TextView) itemView.findViewById(R.id.song_name_result);
            albumImage = (ImageView) itemView.findViewById(R.id.albumArt);
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
