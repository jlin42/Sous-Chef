package edu.wm.cs.cs445.sous_chef;

import androidx.annotation.NonNull;
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
    private final String recipe;

    // "Chicken, bread crumbs, ..."
    @NonNull
    private final String recipe_description;

    // The time text in the top right
    // Format: "2h10m"
    // because the text box is short so keep the abbreviations small
    @NonNull
    private final String recipe_time;

    // Associated with the star ImageView
    // true = color in star, false = leave it white
    @NonNull
    private final Boolean recipe_saved;

    // Used on the 'View' page
    @NonNull
    private final String recipe_instructions;

    // Visible on recipe_list screen or not
    // If true, this recipe should ONLY be visible on the recipe_list screen right after it was
    // pulled from the API. It should not be anywhere else unless the user saves it or
    // clicks "VIEW".
    @NonNull
    private final Boolean new_recipe;

    // If recipe should be visible on recipe history screen
    // True if the user has clicked "VIEW" button
    private final boolean recipe_in_history;


    public Recipe(@NonNull String recipe, @NonNull String recipe_description,
                  @NonNull String recipe_time, @NonNull Boolean recipe_saved,
                  @NonNull String recipe_instructions, @NonNull Boolean new_recipe,
                  @NonNull Boolean recipe_in_history) {
        this.recipe = recipe;
        this.recipe_description = recipe_description;
        this.recipe_time = recipe_time;
        this.recipe_saved = recipe_saved;
        this.recipe_instructions = recipe_instructions;
        this.new_recipe = new_recipe;
        this.recipe_in_history = recipe_in_history;
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
    public String getRecipe_instructions() {
        return recipe_instructions;
    }

    @NonNull
    public Boolean getNew_recipe() { return new_recipe; }

    @NonNull
    public Boolean getRecipe_in_history() { return recipe_in_history; }
}
