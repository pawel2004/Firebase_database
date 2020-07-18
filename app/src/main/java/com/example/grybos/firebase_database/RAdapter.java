package com.example.grybos.firebase_database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RAdapter extends RecyclerView.Adapter<RAdapter.ViewHolder> {

    private ArrayList<Track> tracks;
    private OnItemLongClickListener mlistener;

    public interface OnItemLongClickListener{ //Tworzę interface, czyli to co np.: w setOnClickListener jest w nawiasie po new

        void onItemLongClick(int position); //Metoda po override

    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){ //Metoda, która tworzy interface

        mlistener = listener;

    }

    public RAdapter(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView rating;

        public ViewHolder(@NonNull View itemView, final OnItemLongClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.txt1);
            rating = itemView.findViewById(R.id.txt2);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (listener != null){

                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){

                            listener.onItemLongClick(position);

                        }

                    }

                    return true;
                }
            });

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ViewHolder(v, mlistener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Track listTrack = tracks.get(position);

        holder.title.setText(listTrack.getTrackName());
        holder.rating.setText("Ocena: " + listTrack.getTrackRating());

    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }
}
