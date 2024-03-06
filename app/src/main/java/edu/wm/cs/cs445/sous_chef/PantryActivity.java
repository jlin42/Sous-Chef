package edu.wm.cs.cs445.sous_chef;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class PantryActivity extends AppCompatActivity {

    private RecyclerView inputBox;
    private InputAdapter inputAdapter;
    private ArrayList<String> pantryIngredients;
    private SharedPreferences pantryPrefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_container, new BaseFrame())
                .commit();

        //Cannot call storePrefs() or loadPrefs() above these line
        pantryPrefs = PantryActivity.this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        editor = pantryPrefs.edit();

        AutoCompleteTextView pantryTextView = (AutoCompleteTextView) findViewById(R.id.pantry_textview);

        pantryIngredients = loadPrefs();

        inputBox = findViewById(R.id.yourPantryView);
        inputAdapter = new InputAdapter(pantryIngredients, PantryActivity.this);
        inputBox.setAdapter(inputAdapter);
        inputBox.setLayoutManager(new LinearLayoutManager(this));

        Button addPantry = (Button) findViewById(R.id.addPantryBtn);
        addPantry.setOnClickListener(v -> {
            //First letter capitalized, all else lowercase
            String newIngredient = pantryTextView.getText().toString().toUpperCase().charAt(0) + pantryTextView.getText().toString().toLowerCase().substring(1);
            if (!pantryIngredients.contains(newIngredient)) {
                pantryIngredients.add(newIngredient);
                pantryIngredients.sort(String::compareTo);
                inputAdapter.notifyDataSetChanged();
                pantryTextView.setText("");
            } else {
                Toast alreadyAdded = Toast.makeText(PantryActivity.this, "Ingredient is already in your current filter", Toast.LENGTH_SHORT);
                alreadyAdded.show();
            }
        });
    }

    //Using no modifier is package-private.  BaseFrame.java needs the method to store prefs when leaving activity,
    //but this is technically more secure than protected so I figured it would be good practice
    void storePrefs() {
        StringBuilder pantryPrefBuilder = new StringBuilder();
        if (pantryIngredients.size() > 0) {
            for (int i=0; i < pantryIngredients.size()-1; i++) {
                pantryPrefBuilder = pantryPrefBuilder.append(pantryIngredients.get(i)).append(",");
            }
            pantryPrefBuilder = pantryPrefBuilder.append(pantryIngredients.get(pantryIngredients.size()-1));
        }
        editor.putString(getString(R.string.pantry_ingredients_key), pantryPrefBuilder.toString());
        editor.apply();

        Log.i("PantryActivity:","Preferences stored [" + getString(R.string.pantry_ingredients_key) + ": " + pantryPrefBuilder + "]");
    }

    private ArrayList<String> loadPrefs() {
        String loadedPrefStr = pantryPrefs.getString(getString(R.string.pantry_ingredients_key), "");
        //Without this step, the pantry will have a blank list item in it when activity starts
        if (loadedPrefStr.equals("")) { return new ArrayList<>(); }
        ArrayList<String> loadedPrefsList = new ArrayList<>(Arrays.asList(loadedPrefStr.split(",")));
        Log.i("PantryActivity:", "Preferences loaded [" + getString(R.string.pantry_ingredients_key) + ": "+ loadedPrefStr + "]");
        return loadedPrefsList;
    }
}
