package com.example.grybos.firebase_database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddTrackActivity extends AppCompatActivity {

    private TextView textViewArtistName;
    private EditText editTextTrackName;
    private SeekBar seekBarRating;
    private ListView listView;
    private Button button;
    private ArrayList<Track> tracks = new ArrayList<>();
    private RecyclerView recyclerView;
    private RAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference databaseReference; //Obiekt bazy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        textViewArtistName = findViewById(R.id.textViewArtistName);
        editTextTrackName = findViewById(R.id.edit_text1);
        seekBarRating = findViewById(R.id.seekBarRating);
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button1);
        recyclerView = findViewById(R.id.recycler);

        Bundle bundle = getIntent().getExtras();

        String id = bundle.getString("ArtistId"); //Pobieram id artysty
        String name = bundle.getString("ArtistName"); //Pobieram nazwę artysty

        textViewArtistName.setText(name); //Ustawiam tą nazwę w text View

        databaseReference = FirebaseDatabase.getInstance().getReference("tracks").child(id); //Tworzę nową gałąź o nazwie "tracks" i dodaję dziecko o id tym samym co artysty

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveTrack();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                tracks.clear();

                for (DataSnapshot tracksnapshot : snapshot.getChildren()){

                    Track track = tracksnapshot.getValue(Track.class);

                    tracks.add(track);

                }

                generateRecycler();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void generateRecycler(){

        layoutManager = new LinearLayoutManager(AddTrackActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RAdapter(tracks);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemLongClickListener(new RAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {

                Track track = tracks.get(position);

                showUpdateDialog(track.getTrackId(), track.getTrackName());

            }
        });

    }

    private void saveTrack() {

        String trackName = editTextTrackName.getText().toString();
        int rating = seekBarRating.getProgress();

        if (TextUtils.isEmpty(trackName)){

            editTextTrackName.setError("Musisz to wpisać!");
            editTextTrackName.requestFocus();
            return;

        }

        String id = databaseReference.push().getKey(); //Dostaję id

        Track track = new Track(id, trackName, rating); //Tworzę utwór z id, nazwą i oceną

        databaseReference.child(id).setValue(track); //Dodaję utwór do dziecka

        Toast.makeText(this, "Track saved successfully", Toast.LENGTH_LONG).show();

    }

    private void showUpdateDialog(final String trackId, String trackName){ //Pokazuje dialog, który zmienia dane

        final AlertDialog.Builder alert = new AlertDialog.Builder(AddTrackActivity.this); //Nowy dialog

        LayoutInflater inflater = getLayoutInflater(); //Inflater

        final View dialogView = inflater.inflate(R.layout.changing_track,null); //Przekazujemy mu layout

        alert.setView(dialogView); //Ustawienie layoutu

        final TextView textViewName = dialogView.findViewById(R.id.txt1);
        final EditText editTextName = dialogView.findViewById(R.id.edit_text1);
        final SeekBar seekBarRate = dialogView.findViewById(R.id.rate);
        final Button buttonUpdate = dialogView.findViewById(R.id.update);
        final Button buttonDelete = dialogView.findViewById(R.id.delete);

        textViewName.setText("Updating track: " + trackName);

        final AlertDialog alertDialog = alert.create(); //Tworzymy dialog
        alertDialog.show(); //Pokazujemy go

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editTextName.getText().toString();
                int rating = seekBarRate.getProgress();

                if (!name.isEmpty()){

                    updateTrack(trackId, name, rating); //Metoda do updatu

                }
                else {

                    editTextName.setError("Tu musi coś być!");
                    editTextName.requestFocus();

                }

                alertDialog.dismiss(); //Chowamy dialog

            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteTrack(trackId); //Metoda usuwająca, przyjmuje id
                alertDialog.dismiss();

            }
        });

    }

    private void deleteTrack(String trackId) {

        DatabaseReference drTrack = databaseReference.child(trackId); //Obiekt. który reprezentuje jego utwory

        drTrack.removeValue(); //Usuwanie

        Toast.makeText(this, "Track deleted!", Toast.LENGTH_LONG).show();

    }

    private boolean updateTrack(String id, String name, int rating){

        DatabaseReference databaseTrack = databaseReference.child(id); //Pobieramy obiekt utworu w drzewie po id

        Track track = new Track(id, name, rating); //Tworzymy nowy model z nowych danych z dialoga

        databaseTrack.setValue(track); //Nadpisujemy dane

        Toast.makeText(this, "Track Updated", Toast.LENGTH_LONG).show();

        return true;

    }
}
