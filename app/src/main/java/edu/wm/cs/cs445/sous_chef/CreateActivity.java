package edu.wm.cs.cs445.sous_chef;

import android.os.Bundle;
import android.util.Log;
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
    AutoCompleteTextView ingredientsTextView;
    Button addFilter;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_create);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_container, new BaseFrame())
                .commit();

        ingredientsTextView = (AutoCompleteTextView) findViewById(R.id.ingredients_autocomplete);
        String ingredients[] = getResources().getStringArray(R.array.user_ingredients);
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        ingredientsTextView.setAdapter(ingredientsAdapter);

        // Holds inputs
        ArrayList<String> filtersList = new ArrayList<>();
        List<String> ingredientsList = Arrays.asList(ingredients);

        inputBox = findViewById(R.id.ingredientInputs);
        inputAdapter = new InputAdapter(filtersList);
        inputBox.setAdapter(inputAdapter);
        inputBox.setLayoutManager(new LinearLayoutManager(this));


        addFilter = (Button) findViewById(R.id.addFilterBtn);
        addFilter.setOnClickListener(v -> {
            String newFilter = ingredientsTextView.getText().toString();
            Log.v("test", "test");
            if (ingredientsList.contains(newFilter)) {
//                filtersList.add(String.valueOf(ingredientsTextView.getText()));
                //TODO: Implement code to add from input to filterview
                inputAdapter.addInput(String.valueOf(ingredientsTextView.getText()));

//                inputAdapter.notifyDataSetChanged();
                ingredientsTextView.setText("");
            } else {
                Toast ingredNotFound = Toast.makeText(CreateActivity.this, "Ingredient is not in your list", Toast.LENGTH_SHORT);
                ingredNotFound.show();
            }

        });
    }
}
