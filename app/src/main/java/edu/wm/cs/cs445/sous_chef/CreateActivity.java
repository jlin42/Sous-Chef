package edu.wm.cs.cs445.sous_chef;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CreateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_create);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_container, new BaseActivity())
                .commit();

        Button infoBtn = (Button) findViewById(R.id.createRecipeButton);
        infoBtn.setOnClickListener(v -> {
            Toast inputInfo = Toast.makeText(CreateActivity.this, "Input ingredients to filter with. Leave blank to search with all of your ingredients", Toast.LENGTH_LONG);
            inputInfo.show();
        });

        AutoCompleteTextView ingredientsTextView = (AutoCompleteTextView) findViewById(R.id.ingredients_autocomplete);
        String ingredients[] = getResources().getStringArray(R.array.user_ingredients);
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        ingredientsTextView.setAdapter(ingredientsAdapter);

        ArrayList<String> filtersList = new ArrayList<>();
        List<String> ingredientsList = Arrays.asList(ingredients);

        //String[] filtersArr = new String[10];
        //String[] testArr = {"String", "test", "this sucks"};

        RecyclerView recyclerView = findViewById(R.id.SelectedFilters);
        IngredientAdapter filterAdapter = new IngredientAdapter(filtersList);
        recyclerView.setAdapter(filterAdapter);


        Button addFilter = (Button) findViewById(R.id.addFilterBtn);
        addFilter.setOnClickListener(v -> {
            String newFilter = ingredientsTextView.getText().toString();
            if (ingredientsList.contains(newFilter)) {
                filtersList.add(newFilter);
                filterAdapter.notifyDataSetChanged();
                ingredientsTextView.setText("");
            } else {
                Toast ingredNotFound = Toast.makeText(CreateActivity.this, "Ingredient is not in your list", Toast.LENGTH_SHORT);
                ingredNotFound.show();
            }

        });
    }
}
