package com.example.grybos.firebase_database;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Artist artist = list.get(position);

                Intent intent = new Intent(MainActivity.this, AddTrackActivity.class);

                intent.putExtra("ArtistId", artist.getArtistId());
                intent.putExtra("ArtistName", artist.getArtistName());

                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { //Przy długim naciśnięciu w liście
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Artist artist = list.get(position); //Pobieramy artystę

                showUpdateDialog(artist.getArtistId(), artist.getArtistName(), artist.getArtistGenre()); //Używamy metody z danymi artysty

                return true;

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

    private void showUpdateDialog(final String artistId, String artistName, String artistGenre){ //Pokazuje dialog, który zmienia dane

        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this); //Nowy dialog

        LayoutInflater inflater = getLayoutInflater(); //Inflater

        final View dialogView = inflater.inflate(R.layout.changing_artist,null); //Przekazujemy mu layout

        alert.setView(dialogView); //Ustawienie layoutu

        final TextView textViewName = dialogView.findViewById(R.id.txt1);
        final EditText editTextName = dialogView.findViewById(R.id.edit_text1);
        final Spinner spinnerGenre = dialogView.findViewById(R.id.genre);
        final Button buttonUpdate = dialogView.findViewById(R.id.update);

        textViewName.setText("Updating artist: " + artistName);

        final AlertDialog alertDialog = alert.create(); //Tworzymy dialog
        alertDialog.show(); //Pokazujemy go

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editTextName.getText().toString();
                String genre = spinnerGenre.getSelectedItem().toString();

                if (!name.isEmpty()){

                    updateArtist(artistId, name, genre); //Metoda do updatu

                }
                else {

                    editTextName.setError("Tu musi coś być!");
                    editTextName.requestFocus();

                }

                alertDialog.dismiss(); //Chowamy dialog

            }
        });

    }

    private boolean updateArtist(String id, String name, String genre){

        DatabaseReference databaseReference = databaseArtists.child(id); //Pobieramy obiekt artysty w drzewie po id

        Artist artist = new Artist(id, name, genre); //Tworzymy nowy model z nowych danych z dialoga

        databaseReference.setValue(artist); //Nadpisujemy dane

        Toast.makeText(this, "Artist Updated", Toast.LENGTH_LONG).show();

        return true;

    }

}
