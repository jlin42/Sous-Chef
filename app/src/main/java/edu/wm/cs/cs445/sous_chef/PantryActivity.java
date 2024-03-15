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

        //Cannot call storePrefs() or loadPrefs() above these lines
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
            if (!pantryTextView.getText().toString().equals("")) {
                //First letter capitalized, all else lowercase
                String newIngredient = pantryTextView.getText().toString().toUpperCase().charAt(0) + pantryTextView.getText().toString().toLowerCase().substring(1);
                if (!pantryIngredients.contains(newIngredient)) {
                    pantryIngredients.add(newIngredient);
                    pantryIngredients.sort(String::compareTo);
                    inputAdapter.notifyDataSetChanged();
                    pantryTextView.setText("");
                } else {
                    Log.i("PantryActivity", "Attempted to add ingredient that is already in pantry");
                    Toast alreadyAdded = Toast.makeText(PantryActivity.this, "This ingredient is already in your pantry", Toast.LENGTH_SHORT);
                    alreadyAdded.show();
                }
            }
        });

        Button gradersPantry = (Button) findViewById(R.id.graderPantryBtn);
        gradersPantry.setOnClickListener(v -> {
            String[] pantryArr = getResources().getStringArray(R.array.user_ingredients);
            pantryIngredients.clear();
            inputAdapter.notifyDataSetChanged();
            for (int i = 0; i < pantryArr.length; i++) {
                pantryIngredients.add(pantryArr[i]);
                inputAdapter.notifyItemInserted(i);
            }

            pantryIngredients.sort(String::compareTo);
            Log.i("PantryActivity", "Loaded default pantry: " + pantryIngredients);
            Toast loadedGradePant = Toast.makeText(PantryActivity.this, "USED FOR GRADING: Grading pantry has been created", Toast.LENGTH_SHORT);
            loadedGradePant.show();
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

    private ArrayList<String> loadDebugPantry() {
        Log.e("RecipeListActivity", "Error loading pantry. No pantry found, using default");
        return new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.user_ingredients)));
    }
}
