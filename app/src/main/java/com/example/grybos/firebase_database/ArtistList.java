package com.example.grybos.firebase_database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ArtistList extends ArrayAdapter<Artist> {

    private Context context;
    private ArrayList<Artist> list;
    private int resource;

    public ArtistList(@NonNull Context context, int resource, @NonNull ArrayList<Artist> objects) {
        super(context, resource, objects);

        this.context = context;
        this.list = objects;
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(resource, null);

        TextView artistName = convertView.findViewById(R.id.artistName);
        TextView genre = convertView.findViewById(R.id.genre);

        Artist artist = list.get(position);

        artistName.setText(artist.getArtistName());
        genre.setText(artist.getArtistGenre());

        return convertView;

    }
}
