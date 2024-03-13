package edu.wm.cs.cs445.sous_chef;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LoadingActivity extends AppCompatActivity {
    SharedPreferences settingsPrefs;

    RecipeViewModel recipeViewModel;

    private final String LOG_TAG = "LoadingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        settingsPrefs = LoadingActivity.this.getSharedPreferences(
                getString(R.string.preferences_file_key), Context.MODE_PRIVATE);

        // Retrieve ingredients data
        ArrayList<String> filters = (ArrayList<String>) getIntent().getSerializableExtra("ingredients");
        Log.v(LOG_TAG,"Filters: " +  String.valueOf(filters));

        // Retrieve preferences
        int[] prefs = loadPrefs();
        Log.v(LOG_TAG, "Preferences: " + Arrays.toString(prefs));

        // Retrieve pantry
        String[] pantry = loadPantry();

        // TODO - figure out how to connect JSON output from API to RecyclerView
        // Add recipes to database (like how they are added in the history java file),
        // marking the "new_recipe" field as true. These recipes will only be shown in this screen

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//        final RecipeListAdapter adapter = new RecipeListAdapter(new RecipeListAdapter.RecipeDiff(),
//                recipeViewModel);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//
//        //update cached copy of recipes in the adapter
//        recipeViewModel.getNewRecipes().observe(this, adapter::submitList);

        // How to add to the recipe list screen:
        // Format: String recipe_name,
        //         String recipe_description,
        //         String recipe_time,
        //         Boolean recipe_saved, (<- true for recipes on favorite_recipes screen, false otherwise)
        //         String recipe_instructions,
        //         Boolean new_recipe (<- true for recipes on the recipe_list screen, false otherwise)
        //         Boolean recipe_in_history (<- true for recipes that have been viewed)
        // recipeViewModel.insert(recipe);



        int NUM_RECIPES = 1;

        ArrayList<String> dietsForAPIList = new ArrayList<>();
        ArrayList<String> intolerancesForAPIList = new ArrayList<>();

        //Preferences Array order: Keto, GF, Vegetarian, Vegan, Peanuts, Shellfish, Egg, Dairy
        if (prefs[0] == 1) {
            dietsForAPIList.add("ketogenic");
        }
        if (prefs[1] == 1) {
            dietsForAPIList.add("gluten%20free");
        }
        if (prefs[2] == 1) {
            dietsForAPIList.add("vegetarian");
        }
        if (prefs[3] == 1) {
            dietsForAPIList.add("vegan");
        }
        if (prefs[4] == 1) {
            intolerancesForAPIList.add("peanut");
        }
        if (prefs[5] == 1) {
            intolerancesForAPIList.add("shellfish");
        }
        if (prefs[6] == 1) {
            intolerancesForAPIList.add("egg");
        }
        if (prefs[7] == 1) {
            intolerancesForAPIList.add("dairy");
        }

        String[] dietsForAPI = new String[4];
        dietsForAPIList.toArray(dietsForAPI);
        String[] intolerancesForAPI = new String[4];
        intolerancesForAPIList.toArray(intolerancesForAPI);
        Log.i(LOG_TAG, "DietsForAPI arr: " + dietsForAPI);
        Log.i(LOG_TAG, "IntolerancesForAPI arr: " + intolerancesForAPI);

        //START OF API CODE
        //This code does not update the recipes list, it only makes an example call to see if it
        //will return a recipe ID for the 3 ingredients below to see if the APIs are working
//        String[] newIngre = {"chocolate", "flour", "sugar"};
        SpoonacularAPICall apiGet = new SpoonacularAPICall("b9b5e71ca3c740b8be89bd337d366ce0");
        List<SpoonacularAPIRecipe> sharedRecipeList = Collections.synchronizedList(new ArrayList<>());
        CompletableFuture<SpoonacularAPIRecipe[]> futureRecipes =
                apiGet.getRecipeByIngredients(NUM_RECIPES, pantry);
        futureRecipes.thenAccept(recipes -> {
            if (recipes != null && recipes.length > 0) {
                //successful api call: add the recipes to the sync list
                synchronized(sharedRecipeList) {
                    Collections.addAll(sharedRecipeList, recipes);
                }
                Log.i(LOG_TAG, "Recipe found and added to shared list!");
            } else {
                Log.i(LOG_TAG, "No recipes found or error occurred.");
            }
        }).exceptionally(ex -> {
            Log.e(LOG_TAG, "An error occurred: " + ex.getMessage());
            return null;
        });

        Log.i(LOG_TAG, "Waiting for API...");
        futureRecipes.join();

        for (int i = 0; i < NUM_RECIPES; i++) {

            //a big brain while loop (scuffed as hell)
            //keep checking if we got api output yet
            //in my testing this doesnt add any lag
            // -carlo
            int recipeId = 0;
            int while_loops = 0;
            while (recipeId == 0) {
                synchronized (sharedRecipeList) {
                    if (sharedRecipeList.size() > 0) {
                        recipeId = sharedRecipeList.get(i).getId();
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        while_loops += 1;
                    }
                }
            }
            boolean failed = false;
            assert filters != null;
            if (filters.size() > 0) {
                for (int j = 0; j < filters.size(); j++) {
                    for (int k = 0; k < sharedRecipeList.get(i).getUnusedIngredients().length; k++) {
                        Log.v(LOG_TAG, "Unused ingredient: " + sharedRecipeList.get(i).getUnusedIngredients()[k].getName());
                        if (sharedRecipeList.get(i).getUnusedIngredients()[k].getName().equalsIgnoreCase(filters.get(j))) {
                            failed = true;
                        }
                    }
                }
                if (failed) {
                    Log.i(LOG_TAG, "Missing required ingredient, skip adding to RecipeList");
                    continue;
                }
            }
            Log.i(LOG_TAG, "Took " + while_loops + " while loops to get recipe ID");
            Log.i(LOG_TAG, "RecipeID: " + recipeId);



            //CALL 2: getting the instructions for a recipe using its ID
            //we have the id from the api call above

            //again we need to use a weird evil version of a String bc of threads
            AtomicReference<String> sharedFormattedInstructions = new AtomicReference<>();
            AtomicReference<String> sharedFormattedIngredients = new AtomicReference<>();
            AtomicInteger sharedReadyInMinutes = new AtomicInteger(0);

            //check that we have a recipe ID to use, then make the api call
            if (recipeId > 0) {
                Log.v(LOG_TAG, "Getting recipe instructions...");

                //here is where we make the API call
                CompletableFuture<SpoonacularAPIRecipeInfo> futureRecipeInfo = apiGet.getRecipeInfoById(recipeId);

                //once we get a response we handle it here
                futureRecipeInfo.thenAccept(info -> {
                    if (info != null && info.getReadyInMinutes() > 0) {
                        sharedFormattedInstructions.set(info.getFormattedInstructions());
                        sharedFormattedIngredients.set(info.getFormattedIngredients());
                        sharedReadyInMinutes.set(info.getReadyInMinutes());
                    } else {
                        System.out.println("No recipes found or error occurred.");
                    }
                }).exceptionally(ex -> {
                    System.err.println("An error occurred: " + ex.getMessage());
                    return null;
                });

                //wait until the call finishes, then resume the thread
                Log.v(LOG_TAG, "Waiting for API...");
                futureRecipeInfo.join(); //stop the main thread from ending until async is done

                int waiter = 0;
                while (sharedFormattedInstructions.get() == null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    waiter += 1;
                }
                Log.i(LOG_TAG, "Took " + waiter + " loops");
            }

//        System.out.println(sharedFormattedInstructions.get());

            String recipeTitle = sharedRecipeList.get(i).getTitle();
            String recipeDesc = sharedFormattedIngredients.get(); //NEED REQUIRED INGREDIENTS FROM API
            int cooktime = sharedReadyInMinutes.get(); //NEED TO GET TIME FROM API (TIME IS IN MINUTES)
            String recipeInstructions = sharedFormattedInstructions.get();
            Recipe foundRecipe = new Recipe(recipeTitle, recipeDesc, String.valueOf(cooktime), false, recipeInstructions, true, false);

            recipeViewModel.insert(foundRecipe);

        }

        Intent intent = new Intent(LoadingActivity.this, RecipesListActivity.class);
        startActivity(intent);
    }

    private int[] loadPrefs() {

        //Tries to load any saved preferences, returns empty string if not found
        String loadedSettingPrefs = settingsPrefs.getString(getString(R.string.settings_prefs_key), "");
        int[] prefs = new int[8];

        //If loaded settings string is empty, just return a default array
        if (loadedSettingPrefs.equals("")) {
            Log.i(LOG_TAG, "No saved preferences found");
            return prefs;
        }
        if (loadedSettingPrefs.length() != 8) {
            Log.i(LOG_TAG, "Error with saved preferences");
            return prefs;
        }

        for (int i = 0; i < prefs.length; i++) {
            prefs[i] = loadedSettingPrefs.charAt(i) - '0';
        }

        Log.i(LOG_TAG, "Preferences loaded from string: " + loadedSettingPrefs);

        return prefs;
    }

    private String[] loadPantry() {
        String loadedPantryStr = settingsPrefs.getString(getString(R.string.pantry_ingredients_key), "");
        if (loadedPantryStr.equals("")) {
            Log.e(LOG_TAG, "Error loading pantry. No pantry found, using default");
            return getResources().getStringArray(R.array.user_ingredients);
        }
        String[] loadedPantry = loadedPantryStr.split(",");
        Log.i(LOG_TAG, "Pantry loaded [" + getString(R.string.pantry_ingredients_key) + ": "+ loadedPantryStr + "]");
        return loadedPantry;
    }
}
