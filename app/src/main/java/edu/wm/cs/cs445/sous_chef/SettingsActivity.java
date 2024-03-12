package edu.wm.cs.cs445.sous_chef;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {

    protected CheckBox pref_keto;
    protected CheckBox pref_glutenFree;
    protected CheckBox pref_vegetarian;
    protected CheckBox pref_vegan;
    protected CheckBox alrg_peanuts;
    protected CheckBox alrg_shellfish;
    protected CheckBox alrg_eggs;
    protected CheckBox alrg_dairy;
    private SharedPreferences settingsPrefs;
    private SharedPreferences.Editor editor;

    /** This array gets initialized to a length of 8 populated by all 0s. Each cell represents 1 of 8
     *   preferences that the user can have enabled, where 0 = off and 1 = on.  The idea is that
     *   when the user searches for recipes, this array is packaged by an intent (or maybe just
     *   directly read from other classes??), and each cell is checked whether the preference is
     *   enabled or not to add to the api call along with any other filters that were specified.
     *
     *  Keto = index 0
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

        //Cannot call storePrefs() or loadPrefs() above these line
        settingsPrefs = SettingsActivity.this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        editor = settingsPrefs.edit();

        pref_keto = findViewById(R.id.check_keto);
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
                pref_keto, pref_glutenFree, pref_vegetarian, pref_vegan,
                alrg_peanuts, alrg_shellfish, alrg_eggs, alrg_dairy
        };

        prefsSelected = new int[8];

        int[] prefs = loadPrefs();
        Log.i("SettingsActivity:", "Preferences in storage: " + Arrays.toString(prefs));

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

        StringBuilder settingsPrefBuilder = new StringBuilder();
        for (int i = 0; i < prefsSelected.length; i++) {
            if (prefsSelected[i] == 0 || prefsSelected[i] == 1) {
                settingsPrefBuilder = settingsPrefBuilder.append(prefsSelected[i]);
            } else {
                Log.i("SettingsActivity: ", "Prefs array has value that is not 0 or 1 at index [" + i + "]. Setting preference to 0");
                settingsPrefBuilder = settingsPrefBuilder.append(0);
            }
        }

        editor.putString(getString(R.string.settings_prefs_key), settingsPrefBuilder.toString());
        editor.apply();
        Log.i("SettingsActivity:", "Preferences array saved as string: " + settingsPrefBuilder);
    }

    private int[] loadPrefs() {
//        String filename = "preferences.txt";
//        int[] prefs = null;
//
//        try {
//            FileInputStream fStream = openFileInput(filename);
//            ObjectInputStream oStream = new ObjectInputStream(fStream);
//            prefs = (int[]) oStream.readObject();
//            oStream.close();
//            fStream.close();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        //Tries to load any saved preferences, returns empty string if not found
        String loadedSettingPrefs = settingsPrefs.getString(getString(R.string.settings_prefs_key), "");
        int[] prefs = new int[8];

        //If loaded settings string is empty, just return a default array
        if (loadedSettingPrefs.equals("")) {
            Log.i("SettingsActivity:", "No saved preferences found");
            return prefs;
        }
        if (loadedSettingPrefs.length() != 8) {
            Log.i("SettingsActivity:", "Error with saved preferences");
            return prefs;
        }

        for (int i = 0; i < prefs.length; i++) {
            prefs[i] = loadedSettingPrefs.charAt(i) - '0';
        }

        Log.i("SettingsActivity:", "Preferences loaded from string: " + loadedSettingPrefs);

        return prefs;
    }

}
