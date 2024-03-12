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
    private final RecipeRepository repository;
    private final LiveData<List<Recipe>> allRecipes;
    private final LiveData<List<Recipe>> savedRecipes;
    private final LiveData<List<Recipe>> newRecipes;
    private final LiveData<List<Recipe>> recipeHistory;
    public RecipeViewModel(Application application) {
        super(application);
        repository = new RecipeRepository(application);
        allRecipes = repository.getAllRecipes();
        savedRecipes = repository.getSavedRecipes();
        newRecipes = repository.getNewRecipes();
        recipeHistory = repository.getRecipeHistory();
    }

    LiveData<List<Recipe>> getAllRecipes() { return allRecipes; }

    LiveData<List<Recipe>> getSavedRecipes() { return savedRecipes; }
    LiveData<List<Recipe>> getNewRecipes() {return newRecipes; }
    LiveData<List<Recipe>> getRecipeHistory() {return recipeHistory; }
    LiveData<Recipe> findRecipe(String title) { return repository.findRecipe(title); }

    public void insert(Recipe recipe) { repository.insert(recipe); }

    public void updateSaved(Recipe recipe, Boolean recipeSaved) { repository.updateSaved(
                                                                  recipe, recipeSaved); }
    public void updateHistory(Recipe recipe, Boolean inHistory) { repository.updateHistory(
                                                                  recipe, inHistory); }

    public void updateNewRecipe(Recipe recipe, Boolean newRecipe) { repository.updateNewRecipe(
                                                                    recipe, newRecipe); }
    public void deleteAll() { repository.deleteAll(); }

    public void clearUnusedNewRecipes() { repository.clearUnusedNewRecipes(); }

    public void delete(Recipe recipe) { repository.delete(recipe); }
}
