package edu.wm.cs.cs445.sous_chef;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="recipe_table")
public class Recipe {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="recipe")
    private String recipe;
    @NonNull
    private String recipe_description;
    @NonNull
    private String recipe_time;
    @NonNull
    private Boolean recipe_saved;
    @NonNull
    private String recipe_link;

    public Recipe(@NonNull String recipe, @NonNull String recipe_description,
                  @NonNull String recipe_time, @NonNull Boolean recipe_saved,
                  @NonNull String recipe_link) {
        this.recipe = recipe;
        this.recipe_description = recipe_description;
        this.recipe_time = recipe_time;
        this.recipe_saved = recipe_saved;
        this.recipe_link = recipe_link;
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
}
