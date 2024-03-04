package edu.wm.cs.cs445.sous_chef;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_container, new BaseFrame())
                .commit();

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

        CheckBox[] options = {
                pref_noSpice, pref_glutenFree, pref_vegan, pref_vegetarian,
                alrg_dairy, alrg_eggs, alrg_peanuts, alrg_shellfish
        };

        prefsSelected = new int[8];

        int prefs[] = loadPrefs();
        Log.i("Preferences in storage: ", Arrays.toString(prefs));

        // Set onClickListeners and also load from storage
        for (int i = 0; i < prefs.length; i++) {
            int index = i;
            options[i].setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (options[index].isChecked()) {
                    if (prefsSelected[index] != 1) { prefsSelected[index] = 1; }
                } else {
                    if (prefsSelected[index] != 0) { prefsSelected[index] = 0; }
                }
                storePref();
            });
            options[i].setChecked(prefs[i] == 1);
        }

    }
    // Preferences storage
    private void storePref() {
        String filename ="preferences.txt";
        Log.i("preferences: ", Arrays.toString(prefsSelected));

        try {
            FileOutputStream fStream = openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oStream = new ObjectOutputStream(fStream);
            oStream.writeObject(prefsSelected);
            oStream.close();
            fStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[] loadPrefs() {
        String filename = "preferences.txt";
        int[] prefs = null;

        try {
            FileInputStream fStream = openFileInput(filename);
            ObjectInputStream oStream = new ObjectInputStream(fStream);
            prefs = (int[]) oStream.readObject();
            oStream.close();
            fStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return prefs;
    }

}
