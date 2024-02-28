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

    //
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

    // Shouldn't be necessary after testing is done
    // Removes all recipe entries from the DB
    void deleteAll() {
        RecipeRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipeDAO.deleteAll();
        });
    }
}
