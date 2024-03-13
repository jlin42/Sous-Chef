package edu.wm.cs.cs445.sous_chef;

import static androidx.core.app.ShareCompat.getCallingActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseFrame extends Fragment {
    RecipeViewModel recipeViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        View rootView = inflater.inflate(R.layout.activity_base, container, false);

        BottomNavigationView bottomNav = rootView.findViewById(R.id.navbar);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_btn:
                    if (!(getActivity() instanceof MainActivity)) {
                        if (getActivity() instanceof PantryActivity) { ((PantryActivity) getActivity()).storePrefs(); }
                        clearUnusedNewRecipes();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        return true;
                    }
                    break;
                case R.id.create_btn:
                    if (!(getActivity() instanceof CreateActivity)) {
                        if (getActivity() instanceof PantryActivity) { ((PantryActivity) getActivity()).storePrefs(); }
                        clearUnusedNewRecipes();
                        startActivity(new Intent(getActivity(), CreateActivity.class));
                        return true;
                    }
                    break;
                case R.id.settings_btn:
                    if (!(getActivity() instanceof SettingsActivity)) {
                        if (getActivity() instanceof PantryActivity) { ((PantryActivity) getActivity()).storePrefs(); }
                        clearUnusedNewRecipes();
                        startActivity(new Intent(getActivity(), SettingsActivity.class));
                        return true;
                    }
            }
            return false;
        });

        return rootView;
    }

    private void clearUnusedNewRecipes(){
        // remove generated new recipes from db when leaving recipes list or view recipe screens
        if (getActivity() instanceof ViewRecipeActivity | getActivity() instanceof RecipesListActivity){
            recipeViewModel.clearUnusedNewRecipes();
        }
    }
}
