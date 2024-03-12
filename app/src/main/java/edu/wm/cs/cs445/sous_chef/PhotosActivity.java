package edu.wm.cs.cs445.sous_chef;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PhotosActivity extends AppCompatActivity {
    private Button takePhoto;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    Bitmap photo;
    LinearLayout photoPreview;

    ArrayList<Bitmap> photosArr;

    int photoPointer = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.base_container, new BaseFrame())
                .commit();

        takePhoto = findViewById(R.id.takePhoto);
        photoPreview = findViewById(R.id.photoPreview);

        refreshView();

        takePhoto.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                startCamera();
            } else {
                requestCameraPermission();
            }
        });

    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA_PERMISSION);
    }

    private void startCamera() {
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(photoIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // The photo was successfully captured
            Bundle extras = data.getExtras();
            // Retrieve the captured photo as a Bitmap object
            photo = (Bitmap) extras.get("data");

            Bitmap scaledPhoto = scaleBitmap(photo);

            savePhoto(scaledPhoto);

            refreshView();
        }
    }

    private Bitmap scaleBitmap(Bitmap bitmap) {
        // Get the width of the device's screen
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int targetWidth = (int) (screenWidth * 0.8f);
        float scale = (float) targetWidth / bitmap.getWidth();
        int targetHeight = (int) (bitmap.getHeight() * scale);
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
    }

    private void createPhoto(Bitmap photo) {
        // Create a new FrameLayout to hold the ImageView and Delete Button
        FrameLayout layout = new FrameLayout(this);


        Button deleteButton = new Button(this);
        deleteButton.setText("Delete");


        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(photo);

        // Set layout parameters for the ImageView
        FrameLayout.LayoutParams imageParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        imageView.setLayoutParams(imageParams);

        // Set layout parameters for the Button
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        deleteButton.setLayoutParams(buttonParams);
        deleteButton.setBackgroundColor(
                ContextCompat.getColor(this, R.color.accent)
        );

        File directory = getDir("photos", Context.MODE_PRIVATE);
        File[] files = directory.listFiles();

        deleteButton.setTag(files[photoPointer].getName());
        photoPointer += 1;

        deleteButton.setOnClickListener(v -> {
            photoPreview.removeView(layout);
            photosArr.remove(photo);

            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().equals(v.getTag())) {
                    files[i].delete();
                    break;
                }
            }
            refreshView();
        });

        layout.addView(deleteButton);
        layout.addView(imageView);

        // Add the custom layout to the LinearLayout
        photoPreview.addView(layout);
    }

    private void savePhoto(Bitmap photo) {
        File directory = getDir("photos", Context.MODE_PRIVATE);
        File path;

        // Prevent overwriting files
        while (true) {
            String id = getUniqueBitmapValue();
            path = new File(directory, "photo_" + id);
            if (!path.exists()) {
                break;
            }
        }


        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            photo.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private ArrayList<Bitmap> loadPhotos() {
        ArrayList<Bitmap> loadedPhotos = new ArrayList<>();
        File directory = getDir("photos", Context.MODE_PRIVATE);
        File[] files = directory.listFiles();

        if (files == null) {
            return null;
        }

        for (File file : files) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                loadedPhotos.add(bitmap);
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loadedPhotos;
    }

    // Generate a file name to store the photo
    // This method is called again if the file exists already
    private String getUniqueBitmapValue() {
        Random random = new Random();
        int randomValue = random.nextInt(900000000) + 100000000;
        return String.valueOf(randomValue);
    }

    // When data is updated, refresh the entire view
    private void refreshView() {
        photoPreview.removeAllViews();
        photoPointer = 0;
        photosArr = loadPhotos();
        if (photosArr == null) {
            photosArr = new ArrayList<>();
        } else {
            for (Bitmap photo : photosArr) {
                createPhoto(photo);
            }
        }
    }
}