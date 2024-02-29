package edu.wm.cs.cs445.sous_chef;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseFrame extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_base, container, false);

        BottomNavigationView bottomNav = rootView.findViewById(R.id.navbar);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_btn:
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    return true;
                case R.id.create_btn:
                    startActivity(new Intent(getActivity(), CreateActivity.class));
                    return true;
                case R.id.settings_btn:
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    return true;
            }
            return false;
        });

        return rootView;
    }
}
