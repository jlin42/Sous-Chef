package edu.wm.cs.cs445.sous_chef;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to the Protein AutoCompleteTextView in the layout.
        AutoCompleteTextView proteinTextView = (AutoCompleteTextView) findViewById(R.id.protein_autocomplete);
        // Get the string array.
        String[] proteins = getResources().getStringArray(R.array.protein_array);
        // Create the adapter and set it to the AutoCompleteTextView.
        ArrayAdapter<String> proteinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, proteins);
        proteinTextView.setAdapter(proteinAdapter);

        // Get a reference to the Veggie AutoCompleteTextView in the layout.
        AutoCompleteTextView veggieTextView = (AutoCompleteTextView) findViewById(R.id.veggie_autocomplete);
        String[] veggies = getResources().getStringArray(R.array.veggie_array);
        ArrayAdapter<String> veggieAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, veggies);
        veggieTextView.setAdapter(veggieAdapter);

        // Get a reference to the Starch AutoCompleteTextView in the layout.
        AutoCompleteTextView starchTextView = (AutoCompleteTextView) findViewById(R.id.starch_autocomplete);
        String[] starches = getResources().getStringArray(R.array.starch_array);
        ArrayAdapter<String> starchAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, starches);
        starchTextView.setAdapter(starchAdapter);

    }
}