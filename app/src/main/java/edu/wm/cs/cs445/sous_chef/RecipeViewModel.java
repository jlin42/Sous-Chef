package edu.wm.cs.cs445.sous_chef;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/*
 * Used to provide data to the UI from the Room DB
 * Acts as communication center between the repository and the UI
 * More information in the RecipeRepository class
 */
public class RecipeViewModel extends AndroidViewModel {
    private RecipeRepository repository;
    private final LiveData<List<Recipe>> allRecipes;
    public RecipeViewModel(Application application) {
        super(application);
        repository = new RecipeRepository(application);
        allRecipes = repository.getAllRecipes();
    }

    LiveData<List<Recipe>> getAllRecipes() { return allRecipes; }

    public void insert(Recipe recipe) { repository.insert(recipe); }
    public void deleteAll() { repository.deleteAll(); }
}