package edu.wm.cs.cs445.sous_chef;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

    // Shouldn't be necessary after testing is done
    // Removes all recipe entries from the DB
    @Query("DELETE FROM recipe_table")
    void deleteAll();

    // Returns a LiveData list of the rows in the DB
    // LiveData - updates the associated view when a change in the DB occurs
    // Could sort these by adding "ORDER BY [sort]" into query
    @Query("SELECT * FROM recipe_table")
    LiveData<List<Recipe>> getRecipes();

    //TODO - implement a @Delete method
    //TODO - only store the first 10-ish recipes, when inserting check how many are currently stored
}
