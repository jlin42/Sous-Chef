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
    private final LiveData<List<Recipe>> savedRecipes;
    private final LiveData<List<Recipe>> newRecipes;
    public RecipeViewModel(Application application) {
        super(application);
        repository = new RecipeRepository(application);
        allRecipes = repository.getAllRecipes();
        savedRecipes = repository.getSavedRecipes();
        newRecipes = repository.getNewRecipes();
    }

    LiveData<List<Recipe>> getAllRecipes() { return allRecipes; }

    LiveData<List<Recipe>> getSavedRecipes() { return savedRecipes; }
    LiveData<List<Recipe>> getNewRecipes() {return newRecipes; }

    public void insert(Recipe recipe) { repository.insert(recipe); }
    public void updateSaved(Recipe recipe, Boolean recipeSaved) { repository.updateSaved(
                                                                  recipe, recipeSaved); }

    public void updateNewRecipe(Recipe recipe, Boolean newRecipe) { repository.updateNewRecipe(
                                                                    recipe, newRecipe); }
    public void deleteAll() { repository.deleteAll(); }

    public void delete(Recipe recipe) { repository.delete(recipe); }
}
