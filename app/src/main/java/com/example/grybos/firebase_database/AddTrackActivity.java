package com.example.grybos.firebase_database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTrackActivity extends AppCompatActivity {

    private TextView textViewArtistName;
    private EditText editTextTrackName;
    private SeekBar seekBarRating;
    private ListView listView;
    private Button button;

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
}
