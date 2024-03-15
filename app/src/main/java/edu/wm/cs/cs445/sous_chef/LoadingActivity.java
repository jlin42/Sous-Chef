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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
        recipeViewModel.clearUnusedNewRecipes();

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


        // Recipes may be hard to find because of API call limits
        int NUM_RECIPES = 16;

        ArrayList<String> dietsForAPIList = new ArrayList<>();
        ArrayList<String> intolerancesForAPIList = new ArrayList<>();

        //Preferences Array order: Keto, GF, Vegetarian, Vegan, Peanuts, Shellfish, Egg, Dairy
        int num_intolerances = 0;
        int num_diets = 0;
        if (prefs[0] == 1) {
            dietsForAPIList.add("ketogenic");
            num_diets += 1;
        }
        if (prefs[1] == 1) {
            dietsForAPIList.add("gluten%20free");
            num_diets += 1;
        }
        if (prefs[2] == 1) {
            dietsForAPIList.add("vegetarian");
            num_diets += 1;
        }
        if (prefs[3] == 1) {
            dietsForAPIList.add("vegan");
            num_diets += 1;
        }
        if (prefs[4] == 1) {
            intolerancesForAPIList.add("peanut");
            num_intolerances += 1;
        }
        if (prefs[5] == 1) {
            intolerancesForAPIList.add("shellfish");
            num_intolerances += 1;
        }
        if (prefs[6] == 1) {
            intolerancesForAPIList.add("egg");
            num_intolerances += 1;
        }
        if (prefs[7] == 1) {
            intolerancesForAPIList.add("dairy");
            num_intolerances += 1;
        }

        String[] dietsForAPI = new String[num_diets];
        dietsForAPIList.toArray(dietsForAPI);

        String[] intolerancesForAPI = new String[num_intolerances];
        intolerancesForAPIList.toArray(intolerancesForAPI);

        Log.i(LOG_TAG, "DietsForAPI arr: " + dietsForAPI);
        Log.i(LOG_TAG, "IntolerancesForAPI arr: " + intolerancesForAPI);

        //START OF API CODE
        //This code does not update the recipes list, it only makes an example call to see if it
        //will return a recipe ID for the 3 ingredients below to see if the APIs are working

        //api key 1: b9b5e71ca3c740b8be89bd337d366ce0
        //api key 2: 06170498fac84749ad52e4f6c48b2785
        SpoonacularAPICall apiGet = new SpoonacularAPICall(BuildConfig.API_KEY);
        List<SpoonacularAPIRecipe> sharedRecipeList = Collections.synchronizedList(new ArrayList<>());

        //this method performs the api call
        CompletableFuture<SpoonacularAPIRecipe[]> futureRecipes = apiGet.getRecipeComplex(
            NUM_RECIPES,
            pantry,
            dietsForAPI,
            intolerancesForAPI
        );

        futureRecipes.thenAccept(recipes -> {
            if (recipes != null && recipes.length > 0) {
                //successful api call: add the recipes to the sync list
                synchronized(sharedRecipeList) {
                    Collections.addAll(sharedRecipeList, recipes);
                }
                Log.i(LOG_TAG, "Recipe(s) found and added to shared list!");
            } else {
                Log.i(LOG_TAG, "No recipes found or error occurred.");
            }
        }).exceptionally(ex -> {
            Log.e(LOG_TAG, "An error occurred: " + ex.getMessage());
            return null;
        });

        Log.i(LOG_TAG, "Waiting for API call #1...");
        futureRecipes.join();

        //api call #1 should be complete at this point but just to make sure
        //lets use a classic while loop
        int testRecipeId = 0;
        int testWhileLoops = 0;
        while (testRecipeId == 0) {
            synchronized (sharedRecipeList) {
                if (sharedRecipeList.size() > 0) {
                    testRecipeId = sharedRecipeList.get(0).getId();
                } else {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    testWhileLoops += 1;
                }
            }
        }
        Log.i(LOG_TAG, "API call #1 complete! Extra while loop iterations: " + testWhileLoops);

        /*=============== step 1.5: filter recipes by required ingredients
        iterating through all recipes
        we want a list of all the recipe IDs that fit our filters
        spoonacular already filters by diet and intolerances
        but our api call only grabs ingredients based on pantry inventory,
        which does not give special preference to the ingredients that
        the user selected in the create screen.
        if the user types "chicken," then every recipe that comes up
        should contain chicken.

        we can perform this filter by iterating through all of the recipes,
        and checking if their "unusedIngredients" list contains any of our
        required items. if it does, we remove it from the list.

        by the end we will have a String[] containing the IDs of
        valid recipes. this will be passed to the Bulk Recipe Info API call.
        */

        //need filters to be a String[] object for this
        assert filters != null;
        String[] filtersList = filters.toArray(new String[0]);

        //this array list will hold our valid recipes
        List<String> validRecipeIds = new ArrayList<>();
        List<String> allRecipeIds = new ArrayList<>();

        //if there's nothing in the filters, then ignore this step
        if (filters.size() > 0) {
            //iterate through all found recipes, get the valid recipes' ids
            System.out.println("Filtering recipes...");
            for (int i = 0; i < NUM_RECIPES; i++) {
                int id = 0;
                synchronized (sharedRecipeList) {
                    id = sharedRecipeList.get(i).getId();

                    if (id > 0) {
                        //check if unusedIngredients contains filters
                        boolean recipeIsValid = !checkIfFiltersAreUnused(
                                filtersList,
                                sharedRecipeList.get(i).getUnusedIngredientsNames()
                        );
                        //if filters are not unused, add id to valid list
                        if (recipeIsValid) {
                            System.out.println("Recipe " + id + " is valid!");
                            validRecipeIds.add(String.valueOf(id));
                        } else {
                            Log.i(LOG_TAG, "Recipe " + id + " is not valid");
                        }
                        allRecipeIds.add(String.valueOf(id));
                    }
                }
            }
        } else {
            System.out.println("No required ingredients, skipping filter step");
            for (int i = 0; i < NUM_RECIPES; i++) {
                int id = 0;
                synchronized (sharedRecipeList) {
                    id = sharedRecipeList.get(i).getId();
                    if (id > 0) {
                        validRecipeIds.add(String.valueOf(id));
                        allRecipeIds.add(String.valueOf(id));
                    }
                }
            }
        }
        String[] validRecipeIdList = validRecipeIds.toArray(new String[0]);


        //initialize storage objects for api call #2
        AtomicReference<String[]> sharedFormattedInstructions;
        AtomicReference<String[]> sharedFormattedIngredients;
        AtomicInteger[] sharedReadyInMinutes;

        //if we have valid recipes, populate our storage objects
        //if no valid recipes, create a dummy recipe, and then populate the next best recipes
        if (validRecipeIdList.length == 0) {
            System.out.println("No valid recipes retrieved based on filters! Skill issue!");
            //dummy recipe card
            String recipeTitle = "No recipes found matching required ingredients! Showing next best recipes...";
            String recipeDesc = "Spoonacular could not match your filters. Try a different filter on the last page, or try no filters to find all recipes compatible with your pantry.";
            int cookTime = 0;
            String recipeInstructions = "";
            Recipe foundRecipe = new Recipe(recipeTitle, recipeDesc, String.valueOf(cookTime), false, recipeInstructions, true, false);
            recipeViewModel.insert(foundRecipe);

            //we've warned the user, let's go ahead and print the next best recipes
            //aka ignore the filter
            validRecipeIdList = allRecipeIds.toArray(new String[0]);
        }

        //now we have the valid id's and we can move on to performing api call #2
        //=====================
        //CALL 2: getting the instructions, preptime, and ingredient list from recipe ids
        //we use the bulk recipe info to process all the ids

        //again we need to use a weird evil version of these objects bc of async thread stuff
        sharedFormattedInstructions = new AtomicReference<>(new String[validRecipeIdList.length]);
        sharedFormattedIngredients = new AtomicReference<>(new String[validRecipeIdList.length]);
        sharedReadyInMinutes = new AtomicInteger[validRecipeIdList.length];
        for (int i = 0; i < validRecipeIdList.length; i++) {
            sharedReadyInMinutes[i] = new AtomicInteger();
        }

        Log.v(LOG_TAG, "Getting recipe instructions...");
        CompletableFuture<SpoonacularAPIRecipeInfo[]> futureRecipeInfo = apiGet.getBulkRecipeInfoById(validRecipeIdList);

        //once we get a response we handle it here
        futureRecipeInfo.thenAccept(info -> {
            if (info != null && info[0].getReadyInMinutes() > 0) {

                //here we loop through atomic references and update them with api data

                //init string arrays to hold instructions and ingredients
                String[] formattedInstructions = new String[info.length];
                String[] formattedIngredients = new String[info.length];

                for (int i = 0; i < info.length; i++) {
                    formattedInstructions[i] = info[i].getFormattedInstructions();
                    formattedIngredients[i] = info[i].getFormattedIngredients();
                    sharedReadyInMinutes[i].set(info[i].getReadyInMinutes());
                }

                //update atomic references with the complete arrays
                sharedFormattedInstructions.set(formattedInstructions);
                sharedFormattedIngredients.set(formattedIngredients);

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

        //api should be done but do a while loop to really make sure
        int waiter = 0;
        while (sharedFormattedInstructions.get() == null) {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            waiter += 1;
        }
        Log.i(LOG_TAG, "API call #2 complete! Extra while loop iterations: " + waiter);


        //now we have all the info we need to populate the recipe view
        //add the recipes to recipeViewModel
        for (int i = 0; i < validRecipeIdList.length; i++) {
            System.out.println("Recipe name!!" + sharedRecipeList.get(i).getTitle());

            String recipeTitle = sharedRecipeList.get(i).getTitle();
            String recipeDesc = sharedFormattedIngredients.get()[i]; //NEED REQUIRED INGREDIENTS FROM API
            int cookTime = sharedReadyInMinutes[i].get(); //NEED TO GET TIME FROM API (TIME IS IN MINUTES)
            String recipeInstructions = sharedFormattedInstructions.get()[i];

            //create a Recipe object and insert into view
            Recipe foundRecipe = new Recipe(recipeTitle, recipeDesc, String.valueOf(cookTime), false, recipeInstructions, true, false);

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

    private boolean checkIfFiltersAreUnused(String[] filters, String[] unusedIngredients) {
        // Convert the unusedIngredients array to a set for faster lookups
        Set<String> unusedSet = new HashSet<>(Arrays.asList(unusedIngredients));

        // Check each filter ingredient to see if it's in the set of unused ingredients
        for (String filter : filters) {
            if (unusedSet.contains(filter)) {
                // If any filter ingredient is found in the unused ingredients, return true
                return true;
            }
        }

        // If we get through all the filters without a match, return false
        return false;
    }
}
