package edu.wm.cs.cs445.sous_chef;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

class RecipeViewHolder extends RecyclerView.ViewHolder {
    private final TextView recipe_name;
    private final TextView recipe_description;
    private final TextView recipe_time;
    private final ImageView recipe_saved;


    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        this.recipe_name = itemView.findViewById(R.id.recipe_name_text);
        this.recipe_description = itemView.findViewById(R.id.recipe_description_text);
        this.recipe_time = itemView.findViewById(R.id.recipe_time_text);
        this.recipe_saved = itemView.findViewById(R.id.favorite_recipe_img);
    }

    public void bind(String name, String description, String time, Boolean favorite){
        recipe_name.setText(name);
        recipe_description.setText(description);
        recipe_time.setText(time);
        // If the recipe is one of the users favorites, color the star to be gold
        if (favorite){
            recipe_saved.setColorFilter(R.color.secondary);
        }
    }

    static RecipeViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_row, parent, false);
        return new RecipeViewHolder(view);
    }
}

class RecipeListAdapter extends ListAdapter<Recipe, RecipeViewHolder> {
    public RecipeListAdapter(@NonNull DiffUtil.ItemCallback<Recipe> diffCallback){
        super(diffCallback);
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return RecipeViewHolder.create(parent);
    }

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

