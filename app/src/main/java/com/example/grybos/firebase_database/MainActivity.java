package com.example.grybos.firebase_database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private Spinner spinner;

    private DatabaseReference databaseArtists; //Obiekt wpisu do bazy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseArtists = FirebaseDatabase.getInstance().getReference("artists"); //Tworzymy nową część drzewa o nazwie "artists"

        editText = findViewById(R.id.edit_text1);
        button = findViewById(R.id.button1);
        spinner = findViewById(R.id.spinner1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addArtist();

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
