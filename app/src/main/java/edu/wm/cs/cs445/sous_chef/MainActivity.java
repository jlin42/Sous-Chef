package edu.wm.cs.cs445.sous_chef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Button pantry;
    Button history;
    Button favorites;

    ImageView camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pantry = findViewById(R.id.pantry);
        history = findViewById(R.id.favStar);
        favorites = findViewById(R.id.favorites);
        camera = findViewById(R.id.cameraIcon);

        pantry.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PantryActivity.class)));

        history.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));

        favorites.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FavoriteRecipesActivity.class)));

        camera.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PhotosActivity.class)));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_container, new BaseFrame())
                .commit();

        Button debugBtn = (Button) findViewById(R.id.debugViewRecipe);
        debugBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ViewRecipeActivity.class)));
    }
}