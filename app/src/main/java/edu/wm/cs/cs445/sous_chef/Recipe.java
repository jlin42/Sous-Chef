package edu.wm.cs.cs445.sous_chef;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 * This class acts as the layout of the SQLite table
 * Each of these declarations is a column in the table
 * Each "recipe object" is a row in this table
 */

// Entity = an SQLite table
@Entity(tableName="recipe_table")
public class Recipe {
    // The title, i.e. "Chicken Pot Pie"
    // PrimaryKey = what the database identifies this row as being
    @PrimaryKey
    @NonNull
    private String recipe;

    // "Chicken, bread crumbs, ..."
    @NonNull
    private String recipe_description;

    // The time text in the top right
    // Format: "2h10m"
    // because the text box is short so keep the abbreviations small
    @NonNull
    private String recipe_time;

    // Associated with the star ImageView
    // true = color in star, false = leave it white
    @NonNull
    private Boolean recipe_saved;

    // Could be useful for the "VIEW" button
    // Also might be needed for deciding if this recipe should go into the
    // users recipe history
    @NonNull
    private String recipe_link;

    // Visible on recipe_list screen or not
    // If true, this recipe should ONLY be visible on the recipe_list screen right after it was
    // pulled from the API. It should not be anywhere else unless the user saves it or
    // clicks "VIEW".
    @NonNull
    private Boolean new_recipe;


    public Recipe(@NonNull String recipe, @NonNull String recipe_description,
                  @NonNull String recipe_time, @NonNull Boolean recipe_saved,
                  @NonNull String recipe_link, @NonNull Boolean new_recipe) {
        this.recipe = recipe;
        this.recipe_description = recipe_description;
        this.recipe_time = recipe_time;
        this.recipe_saved = recipe_saved;
        this.recipe_link = recipe_link;
        this.new_recipe = new_recipe;
    }

    @NonNull
    public String getRecipe() {
        return recipe;
    }

    @NonNull
    public String getRecipe_description() {
        return recipe_description;
    }

    @NonNull
    public String getRecipe_time() {
        return recipe_time;
    }

    @NonNull
    public Boolean getRecipe_saved() {
        return recipe_saved;
    }

    @NonNull
    public String getRecipe_link() {
        return recipe_link;
    }

    @NonNull
    public Boolean getNew_recipe() { return new_recipe; }
}
