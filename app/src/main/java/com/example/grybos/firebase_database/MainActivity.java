package com.example.grybos.firebase_database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private Spinner spinner;
    private ListView listView;

    private DatabaseReference databaseArtists; //Obiekt wpisu do bazy

    private ArrayList<Artist> list = new ArrayList<>(); //Lista na obiekty klasy Artist

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtists = FirebaseDatabase.getInstance().getReference("artists"); //Tworzymy nową część drzewa o nazwie "artists"

        editText = findViewById(R.id.edit_text1);
        button = findViewById(R.id.button1);
        spinner = findViewById(R.id.spinner1);
        listView = findViewById(R.id.listView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addArtist();

            }
        });

    }

    @Override
    protected void onStart() { //Przy starcie ekranu
        super.onStart();

        databaseArtists.addValueEventListener(new ValueEventListener() { //Za każdym updatem bazy danych
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear(); //Czyścimy listę

                for (DataSnapshot artistSnapshot : snapshot.getChildren()){ //For, który:

                    Artist artist = artistSnapshot.getValue(Artist.class); //Pobiera klasę artysty z bazy

                    list.add(artist); //Dodaje go do listy

                }

                ArtistList adapter = new ArtistList(MainActivity.this, R.layout.list_layout, list); //Tworzymy adapter
                listView.setAdapter(adapter); //Ustawiamy go

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addArtist(){
        String name = editText.getText().toString();
        String genre = spinner.getSelectedItem().toString();

        if (name.isEmpty()){

            editText.setError("You must enter a text!");
            editText.requestFocus();
            return;

        }

        String id = databaseArtists.push().getKey(); //Generujemy klucz dla artysty

        Artist artist = new Artist(id, name, genre); //Tworzymy nowy obiekt artysty z id, nazwą i gatunkiem

        databaseArtists.child(id).setValue(artist); //Tworzymy nowe odgałęzienie o id wygenerowanym wcześniej i dajemy tam obiekt artysty

        Toast.makeText(this, "Artist added", Toast.LENGTH_LONG).show();

    }

}
