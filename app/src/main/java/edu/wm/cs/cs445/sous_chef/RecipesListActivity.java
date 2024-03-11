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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
                getString(R.string.preferences_file_key), Context.MODE_PRIVATE);

        // Retrieve ingredients data
        ArrayList<String> ingredients = (ArrayList<String>) getIntent().getSerializableExtra("ingredients");
        Log.v("filters",String.valueOf(ingredients));

        // Retrieve preferences
        int[] prefs = loadPrefs();
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


        //update cached copy of recipes in the adapter
        recipeViewModel.getNewRecipes().observe(this, adapter::submitList);

        // How to add to the recipe list screen:
        // Format: String recipe_name,
        //         String recipe_description,
        //         String recipe_time,
        //         Boolean recipe_saved, (<- true for recipes on favorite_recipes screen, false otherwise)
        //         String recipe_instructions,
        //         Boolean new_recipe (<- true for recipes on the recipe_list screen, false otherwise)
        //         Boolean recipe_in_history (<- true for recipes that have been viewed)
        // recipeViewModel.insert(recipe);



        //START OF API CODE
        //This code does not update the recipes list, it only makes an example call to see if it
        //will return a recipe ID for the 3 ingredients below to see if the APIs are working
        String[] newIngre = {"ground beef", "pork", "rice"};
        SpoonacularAPICall apiGet = new SpoonacularAPICall("b9b5e71ca3c740b8be89bd337d366ce0");
        List<SpoonacularAPIRecipe> sharedRecipeList = Collections.synchronizedList(new ArrayList<>());
        CompletableFuture<SpoonacularAPIRecipe[]> futureRecipes =
                apiGet.getRecipeByIngredients(1, newIngre);
        futureRecipes.thenAccept(recipes -> {
            Log.v("RecipeListActivity", "Entered");
            if (recipes != null && recipes.length > 0) {
                //successful api call: add the recipes to the sync list
                synchronized(sharedRecipeList) {
                    Collections.addAll(sharedRecipeList, recipes);
                }
                Log.v("RecipeListActivity", "Recipe found and added to shared list!");
                System.out.println("Recipe found and added to shared list!");
            } else {
                Log.v("RecipeListActivity", "No recipes found or error occurred.");
                System.out.println("No recipes found or error occurred.");
            }
        }).exceptionally(ex -> {
            Log.e("RecipeListActivity", "An error occurred: " + ex.getMessage());
            System.err.println("An error occurred: " + ex.getMessage());
            return null;
        });
        int recipeId;
        synchronized(sharedRecipeList) {
            if (sharedRecipeList.size() > 0) {
                recipeId = sharedRecipeList.get(0).getId();
            } else {
                recipeId = 0;
            }
        }
        Log.i("RecipeListActivity", "RecipeID: " + recipeId);


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
            Log.i("RecipeListActivity", "No saved preferences found");
            return prefs;
        }
        if (loadedSettingPrefs.length() != 8) {
            Log.i("RecipeListActivity", "Error with saved preferences");
            return prefs;
        }

        for (int i = 0; i < prefs.length; i++) {
            prefs[i] = loadedSettingPrefs.charAt(i) - '0';
        }

        Log.i("RecipeListActivity", "Preferences loaded from string: " + loadedSettingPrefs);

        return prefs;
    }
}
