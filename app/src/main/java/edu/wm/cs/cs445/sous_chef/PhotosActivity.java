package edu.wm.cs.cs445.sous_chef;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PhotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_container, new BaseFrame())
                .commit();
    }
}
