package edu.wm.cs.cs445.sous_chef;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/*
 * Grabs the views from the recycler_view_row layout file
 */
class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView recipe_name;
    private final TextView recipe_description;
    private final TextView recipe_time;
    private final ImageView recipe_saved;

    ClickListener listener;
    ImageView trash;
    Button viewBtn;



    public RecipeViewHolder(@NonNull View itemView, ClickListener listener) {
        super(itemView);
        this.recipe_name = itemView.findViewById(R.id.recipe_name_text);
        this.recipe_description = itemView.findViewById(R.id.recipe_description_text);
        this.recipe_time = itemView.findViewById(R.id.recipe_time_text);
        this.recipe_saved = itemView.findViewById(R.id.favorite_recipe_img);
        this.listener = listener;

        this.trash = itemView.findViewById(R.id.trash_recipe_btn);
        this.viewBtn = itemView.findViewById(R.id.view_btn);

        trash.setOnClickListener(this);
        recipe_saved.setOnClickListener(this);
        viewBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.trash_recipe_btn:
                listener.onDelete(this.getLayoutPosition());
                break;
            case R.id.favorite_recipe_img:
                listener.onSave(this.getLayoutPosition());
                break;
            case R.id.view_btn:
                listener.onView(this.getLayoutPosition());
                break;
        }
    }

    public void bind(String name, String description, String time, Boolean favorite){
        recipe_name.setText(name);
        recipe_description.setText(description);
        time += "min";
        recipe_time.setText(time);
        // If the recipe is one of the users favorites, color the star to be gold
        if (favorite){
            recipe_saved.setColorFilter(Color.argb(255, 255, 215, 0));
        }
        // Otherwise, set the color to be white
        else {
            recipe_saved.setColorFilter(Color.argb(255, 255, 255, 255));
        }
    }
}

class RecipeListAdapter extends ListAdapter<Recipe, RecipeViewHolder> {
    private final RecipeViewModel recipeViewModel;


    public RecipeListAdapter(@NonNull DiffUtil.ItemCallback<Recipe> diffCallback,
                             RecipeViewModel recipeViewModel){
        super(diffCallback);
        this.recipeViewModel = recipeViewModel;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row, parent, false);

        RecipeViewHolder holder;
        holder = new RecipeViewHolder(view, new ClickListener() {
            @Override
            public void onDelete(int p) {
                Recipe current = getItem(p);

                // may be unnecessary - remove recipe from 'saved' before
                // removing it from the database entirely
                recipeViewModel.updateSaved(current, false);

                //REMOVE ITEM FROM DATABASE
                recipeViewModel.delete(current);
            }

            @Override
            public void onSave(int p) {
                Recipe current = getItem(p);

                // check if recipe is a 'new' recipe
                // if it is, remove it from the 'new' section
                // so it can be added to the 'saved' section
                if (current.getNew_recipe()){
                    recipeViewModel.updateNewRecipe(current, false);
                }

                // update this recipe to switch it between being saved or not saved
                // updateSaved takes the recipe to be changed and the Boolean value
                // that it WILL be set to - so send it the opposite of what
                // the recipe's saved value currently is
                recipeViewModel.updateSaved(current, !current.getRecipe_saved());

            }

            // When the user clicks 'VIEW' ->
                // take user to a page with the description of the recipe
                // move recipe from RecipeListActivity to RecipeHistoryActivity (when needed)
            @Override
            public void onView(int p) {
                Recipe current = getItem(p);

                // if this recipe is on the RecipeList page, remove the 'new recipe' qualifier
                if(current.getNew_recipe()){
                    recipeViewModel.updateNewRecipe(current, false);
                }

                // otherwise, this recipe must be on either the 'history' or 'favorites' screen
                // therefore we must add it to the history list if it is not already in it
                if(!current.getRecipe_in_history()){
                    recipeViewModel.updateHistory(current, true);
                }

                // Change intent to view recipe screen, passing in the
                // recipe title through the intent
                Context context = parent.getContext();
                Intent intent = new Intent(context, ViewRecipeActivity.class);
                intent.putExtra("recipe", current.getRecipe());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    // Assigning values to the views created in recycler_view_row.xml, based
    // on the current position of the recycler view
    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position){
        Recipe current = getItem(position);
        holder.bind(current.getRecipe(), current.getRecipe_description(), current.getRecipe_time(),
                    current.getRecipe_saved());
    }

    static class RecipeDiff extends DiffUtil.ItemCallback<Recipe> {
        @Override
        public boolean areItemsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem){
            return oldItem.getRecipe().equals(newItem.getRecipe());
        }
    }
}

