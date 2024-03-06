package edu.wm.cs.cs445.sous_chef;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class RecipesListActivity extends AppCompatActivity {
    SharedPreferences settingsPrefs;

    RecipeViewModel recipeViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_container, new BaseFrame())
                .commit();

        settingsPrefs = RecipesListActivity.this.getSharedPreferences(
                getString(R.string.settings_file_key), Context.MODE_PRIVATE);

        // Retrieve ingredients data
        ArrayList<String> ingredients = (ArrayList<String>) getIntent().getSerializableExtra("ingredients");
        Log.v("filters",String.valueOf(ingredients));

        // Retrieve preferences
        int prefs[] = loadPrefs();
        Log.v("preferences", Arrays.toString(prefs));

        // TODO - figure out how to connect JSON output from API to RecyclerView
        // Add recipes to database (like how they are added in the history java file),
        // marking the "new_recipe" field as true. These recipes will only be shown in this screen

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final RecipeListAdapter adapter = new RecipeListAdapter(new RecipeListAdapter.RecipeDiff(),
                recipeViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recipeViewModel.getNewRecipes().observe(this, recipes -> {
            //update cached copy of recipes in the adapter
            adapter.submitList(recipes);
        });

        // How to add to the recipe list screen:
        // Format: String recipe_name,
        //         String recipe_description,
        //         String recipe_time,
        //         Boolean recipe_saved, (<- true for recipes on favorite_recipes screen, false otherwise)
        //         String recipe_link,
        //         Boolean new_recipe (<- true for recipes on the recipe_list screen, false otherwise)
        /*
        Recipe recipe = new Recipe("Chicken Pot Pie", "Chicken, bread crumbs, assorted veggies, ...",
                "30m", false, "recipe link", false);
        recipeViewModel.insert(recipe);
         */

    }

    private int[] loadPrefs() {
//        String filename = "preferences.txt";
//        int[] prefs = null;
//
//        try {
//            FileInputStream fStream = openFileInput(filename);
//            ObjectInputStream oStream = new ObjectInputStream(fStream);
//            prefs = (int[]) oStream.readObject();
//            oStream.close();
//            fStream.close();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        //Tries to load any saved preferences, returns empty string if not found
        String loadedSettingPrefs = settingsPrefs.getString(getString(R.string.settings_prefs_key), "");
        int[] prefs = new int[8];

        //If loaded settings string is empty, just return a default array
        if (loadedSettingPrefs.equals("")) {
            Log.i("SettingsActivity:", "No saved preferences found");
            return prefs;
        }
        if (loadedSettingPrefs.length() != 8) {
            Log.i("SettingsActivity:", "Error with saved preferences");
            return prefs;
        }

        for (int i = 0; i < prefs.length; i++) {
            prefs[i] = loadedSettingPrefs.charAt(i) - '0';
        }

        Log.i("SettingsActivity:", "Preferences loaded from string: " + loadedSettingPrefs);

        return prefs;
    }
}
