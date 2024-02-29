package edu.wm.cs.cs445.sous_chef;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteRecipesActivity extends AppCompatActivity {

    private RecipeViewModel recipeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_container, new BaseFrame())
                .commit();


        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final RecipeListAdapter adapter = new RecipeListAdapter(new RecipeListAdapter.RecipeDiff(),
                recipeViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeViewModel.getSavedRecipes().observe(this, recipes -> {
            //update cached copy of recipes in the adapter
            adapter.submitList(recipes);
        });
    }
}
