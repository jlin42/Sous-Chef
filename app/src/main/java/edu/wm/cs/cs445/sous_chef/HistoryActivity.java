package edu.wm.cs.cs445.sous_chef;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_container, new BaseFrame())
                .commit();

        RecipeViewModel recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final RecipeListAdapter adapter = new RecipeListAdapter(new RecipeListAdapter.RecipeDiff(),
                recipeViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //update cached copy of recipes in the adapter
        recipeViewModel.getRecipeHistory().observe(this, adapter::submitList);

        // For testing:
        // Adds 4 recipes
        // use recipeViewModel.deleteAll() to clear them if needed
        Recipe recipe = new Recipe("Chicken Pot Pie", "Chicken, bread crumbs, assorted veggies, ...",
                "30m", false, "recipe_instructions", false, true);
        recipeViewModel.insert(recipe);

        recipe = new Recipe("Meatloaf", "Ground beef, bread crumbs, ketchup, onions, ...",
                "30m", false, "recipe_instructions", false, true);
        recipeViewModel.insert(recipe);

        recipe = new Recipe("Mac and Cheese", "Macaroni noodles, milk, butter, flour, cheese",
                "1h", true, "recipe_instructions", false, true);
        recipeViewModel.insert(recipe);

        recipe = new Recipe("Easy Weeknight Spaghetti and Meat Sauce", "Spaghetti noodles, jarred sauce, ground beef, onions, garlic, ...",
                "2h30m", false, "recipe_instructions", false, true);
        recipeViewModel.insert(recipe);

    }
}
