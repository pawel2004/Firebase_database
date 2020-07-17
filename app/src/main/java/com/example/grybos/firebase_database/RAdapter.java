package com.example.grybos.firebase_database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RAdapter extends RecyclerView.Adapter<RAdapter.ViewHolder> {

    private ArrayList<Track> tracks;

    public RAdapter(ArrayList<Track> tracks) {
        this.tracks = tracks;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.txt1);
            rating = itemView.findViewById(R.id.txt2);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new ViewHolder(v);

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
