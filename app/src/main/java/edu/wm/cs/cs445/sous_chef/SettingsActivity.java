package edu.wm.cs.cs445.sous_chef;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {

    protected CheckBox pref_noSpice;
    protected CheckBox pref_glutenFree;
    protected CheckBox pref_vegetarian;
    protected CheckBox pref_vegan;
    protected CheckBox alrg_peanuts;
    protected CheckBox alrg_shellfish;
    protected CheckBox alrg_eggs;
    protected CheckBox alrg_dairy;

    // Used for Logger
    private String msg;

    /** This array gets initialized to a length of 8 populated by all 0s. Each cell represents 1 of 8
     *   preferences that the user can have enabled, where 0 = off and 1 = on.  The idea is that
     *   when the user searches for recipes, this array is packaged by an intent (or maybe just
     *   directly read from other classes??), and each cell is checked whether the preference is
     *   enabled or not to add to the api call along with any other filters that were specified.
     *
     *  No Spices = index 0
     *  Gluten Free = index 1
     *  Vegetarian = index 2
     *  Vegan = index 3
     *  Peanut Allergy = index 4
     *  Shellfish Allergy = index 5
     *  Egg Allergy = index 6
     *  Dairy Allergy = index 7
     */
    public int[] prefsSelected;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_settings);

        pref_noSpice = findViewById(R.id.check_noSpice);
        Log.v("SettingsActivity:", "No Spices CheckBox initialized");
        pref_glutenFree = findViewById(R.id.check_glutenFree);
        Log.v("SettingsActivity:", "Gluten Free CheckBox initialized");
        pref_vegetarian = findViewById(R.id.check_vegetarian);
        Log.v("SettingsActivity:", "Vegetarian CheckBox initialized");
        pref_vegan = findViewById(R.id.check_vegan);
        Log.v("SettingsActivity:", "Vegan CheckBox initialized");
        alrg_peanuts = findViewById(R.id.allergy_peanuts);
        Log.v("SettingsActivity:", "Peanut Allergy CheckBox initialized");
        alrg_shellfish = findViewById(R.id.allergy_shellfish);
        Log.v("SettingsActivity:", "Shellfish Allergy CheckBox initialized");
        alrg_eggs = findViewById(R.id.allergy_eggs);
        Log.v("SettingsActivity:", "Egg Allergy CheckBox initialized");
        alrg_dairy = findViewById(R.id.allergy_dairy);
        Log.v("SettingsActivity:", "Dairy Allergy CheckBox initialized");


        prefsSelected = new int[8];

        pref_noSpice.setOnCheckedChangeListener((buttonView, isChecked) -> {
            msg = "No Spices ";
            if (pref_noSpice.isChecked()) {
                if (prefsSelected[0] != 1) { prefsSelected[0] = 1; }
                Log.v("SettingsActivity:", msg + "was added to recipe preferences.");
            } else {
                if (prefsSelected[0] != 0) { prefsSelected[0] = 0; }
                Log.v("SettingsActivity:", msg + "was removed to recipe preferences.");
            }
            Log.v("SettingsActivity:", "Prefs Array: " + Arrays.toString(prefsSelected));
        });

        pref_glutenFree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            msg = "Gluten Free ";
            if (pref_glutenFree.isChecked()) {
                if (prefsSelected[1] != 1) { prefsSelected[1] = 1; }
                Log.v("SettingsActivity:", msg + "was added to recipe preferences.");
            } else {
                if (prefsSelected[1] != 0) { prefsSelected[1] = 0; }
                Log.v("SettingsActivity:", msg + "was removed to recipe preferences.");
            }
            Log.v("SettingsActivity:", "Prefs Array: " + Arrays.toString(prefsSelected));
        });

        pref_vegetarian.setOnCheckedChangeListener((buttonView, isChecked) -> {
            msg = "Vegetarian ";
            if (pref_vegetarian.isChecked()) {
                if (prefsSelected[2] != 1) { prefsSelected[2] = 1; }
                Log.v("SettingsActivity:", msg + "was added to recipe preferences.");
            } else {
                if (prefsSelected[2] != 0) { prefsSelected[2] = 0; }
                Log.v("SettingsActivity:", msg + "was removed to recipe preferences.");
            }
            Log.v("SettingsActivity:", "Prefs Array: " + Arrays.toString(prefsSelected));
        });

        pref_vegan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            msg = "Vegan ";
            if (pref_vegan.isChecked()) {
                if (prefsSelected[3] != 1) { prefsSelected[3] = 1; }
                Log.v("SettingsActivity:", msg + "was added to recipe preferences.");
            } else {
                if (prefsSelected[3] != 0) { prefsSelected[3] = 0; }
                Log.v("SettingsActivity:", msg + "was removed to recipe preferences.");
            }
            Log.v("SettingsActivity:", "Prefs Array: " + Arrays.toString(prefsSelected));
        });

        alrg_peanuts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            msg = "Peanut Allergy ";
            if (alrg_peanuts.isChecked()) {
                if (prefsSelected[4] != 1) { prefsSelected[4] = 1; }
                Log.v("SettingsActivity:", msg + "was added to recipe preferences.");
            } else {
                if (prefsSelected[4] != 0) { prefsSelected[4] = 0; }
                Log.v("SettingsActivity:", msg + "was removed to recipe preferences.");
            }
            Log.v("SettingsActivity:", "Prefs Array: " + Arrays.toString(prefsSelected));
        });

        alrg_shellfish.setOnCheckedChangeListener((buttonView, isChecked) -> {
            msg = "Shellfish Allergy ";
            if (alrg_shellfish.isChecked()) {
                if (prefsSelected[5] != 1) { prefsSelected[5] = 1; }
                Log.v("SettingsActivity:", msg + "was added to recipe preferences.");
            } else {
                if (prefsSelected[5] != 0) { prefsSelected[5] = 0; }
                Log.v("SettingsActivity:", msg + "was removed to recipe preferences.");
            }
            Log.v("SettingsActivity:", "Prefs Array: " + Arrays.toString(prefsSelected));
        });

        alrg_eggs.setOnCheckedChangeListener((buttonView, isChecked) -> {
            msg = "Egg Allergy ";
            if (alrg_eggs.isChecked()) {
                if (prefsSelected[6] != 1) { prefsSelected[6] = 1; }
                Log.v("SettingsActivity:", msg + "was added to recipe preferences.");
            } else {
                if (prefsSelected[6] != 0) { prefsSelected[6] = 0; }
                Log.v("SettingsActivity:", msg + "was removed to recipe preferences.");
            }
            Log.v("SettingsActivity:", "Prefs Array: " + Arrays.toString(prefsSelected));
        });

        alrg_dairy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            msg = "Dairy Allergy ";
            if (alrg_dairy.isChecked()) {
                if (prefsSelected[7] != 1) { prefsSelected[7] = 1; }
                Log.v("SettingsActivity:", msg + "was added to recipe preferences.");
            } else {
                if (prefsSelected[7] != 0) { prefsSelected[7] = 0; }
                Log.v("SettingsActivity:", msg + "was removed to recipe preferences.");
            }
            Log.v("SettingsActivity:", "Prefs Array: " + Arrays.toString(prefsSelected));
        });
    }

}
