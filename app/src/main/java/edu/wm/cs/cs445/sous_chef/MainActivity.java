package edu.wm.cs.cs445.sous_chef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button pantry;
    Button history;
    Button favorites;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pantry = findViewById(R.id.pantry);
        history = findViewById(R.id.history);
        favorites = findViewById(R.id.favorites);

        pantry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PantryActivity.class));
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FavoriteRecipesActivity.class));
            }
        });
    }
}