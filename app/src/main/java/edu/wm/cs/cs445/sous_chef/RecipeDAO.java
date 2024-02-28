package edu.wm.cs.cs445.sous_chef;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Recipe recipe);

    //TODO - unnecessary
    @Query("DELETE FROM recipe_table")
    void deleteAll();

    // TODO - sort recipes somehow? (ORDER BY [sort])
    @Query("SELECT * FROM recipe_table")
    LiveData<List<Recipe>> getRecipes();

    //TODO - implement a @Delete method
    //TODO - only store the first 10-ish recipes, when inserting check how many are currently stored
}
