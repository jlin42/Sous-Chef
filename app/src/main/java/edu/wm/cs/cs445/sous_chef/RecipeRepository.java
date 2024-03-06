package edu.wm.cs.cs445.sous_chef;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

/*
 * Used to communicate with the DAO, which then interacts with the DB
 * This isn't necessary to communicate with the DAO but it makes code more readable
 * Generally this is supposed to coordinate multiple sources of data, like some local some server
 */
class RecipeRepository {
    private RecipeDAO recipeDAO;
    // LiveData -> the data is automatically updated when DB is updated
    private LiveData<List<Recipe>> allRecipes;
    private LiveData<List<Recipe>> savedRecipes;
    private LiveData<List<Recipe>> newRecipes;
    private LiveData<List<Recipe>> recipeHistory;

    //
    RecipeRepository(Application application) {
        RecipeRoomDatabase db = RecipeRoomDatabase.getDatabase(application);
        recipeDAO = db.recipeDAO();
        allRecipes = recipeDAO.getRecipes();
        savedRecipes = recipeDAO.getSavedRecipes();
        newRecipes = recipeDAO.getNewRecipes();
        recipeHistory = recipeDAO.getRecipeHistory();
    }

    // Room executes all queries on a separate thread
    // Observed LiveData will notify the observer when the data has changed
    LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    //
    LiveData<List<Recipe>> getSavedRecipes() { return savedRecipes; }

    //
    LiveData<List<Recipe>> getNewRecipes() { return newRecipes; }

    LiveData<List<Recipe>> getRecipeHistory() { return recipeHistory; }

    // Must be called on a non-UI thread
    // Room operations cannot be called on UI
    // DAO acts as the middleman between app and the database, it holds all the r/w operations
    void insert(Recipe recipe) {
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipeDAO.insert(recipe);
        });
    }

    void updateNewRecipe(Recipe recipe, Boolean newRecipe) {
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipeDAO.updateNewRecipe(recipe.getRecipe(), newRecipe);
        });
    }

    void updateSaved(Recipe recipe, Boolean recipeSaved){
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipeDAO.updateSaved(recipe.getRecipe(), recipeSaved);
        });
    }

    void updateHistory(Recipe recipe, Boolean inHistory){
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipeDAO.updateHistory(recipe.getRecipe(), inHistory);
        });
    }

    // Shouldn't be necessary after testing is done
    // Removes all recipe entries from the DB
    void deleteAll() {
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipeDAO.deleteAll();
        });
    }

    void delete(Recipe recipe) {
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipeDAO.delete(recipe);
        });
    }
}
