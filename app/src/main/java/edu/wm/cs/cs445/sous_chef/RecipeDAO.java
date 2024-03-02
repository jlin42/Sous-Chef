package edu.wm.cs.cs445.sous_chef;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.Update;

import java.util.List;

/*
 * DAO - data access object
 * Maps SQL queries to functions, which lets us call Java methods
 * instead of having to use SQL
 * The DAO object does not get called directly, instead call the RecipeViewModel or
 * the Repository to interact with the DB
 */
@Dao
public interface RecipeDAO {
    // onConflict = if a recipe with this name is already in the DB,
    // do not try to add this to the DB
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    // Switch whether or not this recipe is 'saved'
    @Query("UPDATE recipe_table SET recipe_saved = :recipeSaved WHERE recipe = :recipe")
    void updateSaved(String recipe, Boolean recipeSaved);

    @Query("UPDATE recipe_table SET new_recipe = :newRecipe WHERE recipe = :recipe")
    void updateNewRecipe(String recipe, Boolean newRecipe);

    // Shouldn't be necessary after testing is done
    // Removes all recipe entries from the DB
    @Query("DELETE FROM recipe_table")
    void deleteAll();

    // Returns a LiveData list of the rows in the DB
    // LiveData - updates the associated view when a change in the DB occurs
    // Could sort these by adding "ORDER BY [sort]" into query
    @Query("SELECT * FROM recipe_table WHERE new_recipe = false")
    LiveData<List<Recipe>> getRecipes();

    // Used for Saved Recipes screen
    @Query("SELECT * FROM recipe_table WHERE recipe_saved = true AND new_recipe = false")
    LiveData<List<Recipe>> getSavedRecipes();

    // Used for Recipe List screen - returns only the recipes which
    // have been marked as 'new'
    @Query("SELECT * FROM recipe_table WHERE new_recipe = true")
    LiveData<List<Recipe>> getNewRecipes();
}
