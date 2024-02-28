package edu.wm.cs.cs445.sous_chef;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class RecipeRepository {
    private RecipeDAO recipeDAO;
    private LiveData<List<Recipe>> allRecipes;

    RecipeRepository(Application application) {
        RecipeRoomDatabase db = RecipeRoomDatabase.getDatabase(application);
        recipeDAO = db.recipeDAO();
        allRecipes = recipeDAO.getRecipes();
    }

    // Room executes all queries on a separate thread
    // Observed LiveData will notify the observer when the data has changed
    LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    // Must be called on a non-UI thread
    // Room operations cannot be called on UI
    // DAO acts as the middleman between app and the database, it holds all the r/w operations
    void insert(Recipe recipe) {
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipeDAO.insert(recipe);
        });
    }

    void deleteAll() {
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipeDAO.deleteAll();
        });
    }
}
