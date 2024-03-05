package edu.wm.cs.cs445.sous_chef;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CreateActivity extends AppCompatActivity {
    RecyclerView inputBox;
    InputAdapter inputAdapter;
    ArrayList<String> filtersList;
    List<String> ingredientsList;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_create);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_container, new BaseFrame())
                .commit();

        AutoCompleteTextView ingredientsTextView = (AutoCompleteTextView) findViewById(R.id.ingredients_autocomplete);
        String ingredients[] = getResources().getStringArray(R.array.user_ingredients);
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        ingredientsTextView.setAdapter(ingredientsAdapter);

        // Holds inputs
        filtersList = new ArrayList<>();
        ingredientsList = Arrays.asList(ingredients);

        inputBox = findViewById(R.id.ingredientInputs);
        inputAdapter = new InputAdapter(filtersList, CreateActivity.this);
        inputBox.setAdapter(inputAdapter);
        inputBox.setLayoutManager(new LinearLayoutManager(this));


        Button addFilter = (Button) findViewById(R.id.addFilterBtn);
        addFilter.setOnClickListener(v -> {
            //First letter capitalized, all else lowercase
            String newFilter = ingredientsTextView.getText().toString().toUpperCase().charAt(0) + ingredientsTextView.getText().toString().toLowerCase().substring(1);
            if (ingredientsList.contains(newFilter)) {
                if (!filtersList.contains(newFilter)) {
                    filtersList.add((ingredientsTextView.getText().toString()));
                    inputAdapter.notifyDataSetChanged();
                    ingredientsTextView.setText("");
                } else {
                    Toast alreadyAdded = Toast.makeText(CreateActivity.this, "Ingredient is already in your current filter", Toast.LENGTH_SHORT);
                    alreadyAdded.show();
                }
            } else {
                Toast ingredNotFound = Toast.makeText(CreateActivity.this, "Ingredient is not in your list", Toast.LENGTH_SHORT);
                ingredNotFound.show();
            }
        });

        Button findRecipe = (Button) findViewById(R.id.findRecipeBtn);
        //TODO: Add bundle with selected filters and grab preferences from SettingsActivity to make api call for RecipesListActivity
        findRecipe.setOnClickListener(v -> {
            if (inputAdapter.getItemCount() == 0) {
                //If user selects no filters, then the api call does not need to check the unused ingredients
                //The filters are the just ingredients that cannot be listed as unused
                Intent recipeIntent = new Intent(CreateActivity.this, RecipesListActivity.class);

                //passing a new arraylist is probably a temporary fix, but just checking if the list.size() > 0
                // on the recipes end would also make this solution work
                recipeIntent.putExtra("ingredients", new ArrayList<String>());
                startActivity(recipeIntent);
            } else {
                Intent recipeIntent = new Intent(CreateActivity.this, RecipesListActivity.class);
                recipeIntent.putExtra("ingredients", filtersList);
                startActivity(recipeIntent);
            }

        });
    }
}
