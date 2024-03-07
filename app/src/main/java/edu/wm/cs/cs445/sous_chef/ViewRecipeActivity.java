package edu.wm.cs.cs445.sous_chef;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewRecipeActivity extends AppCompatActivity {

    Button timerButton;
    private String recipeTitle;
    private int cookTimeMins;

    private String CHANNEL_ID;
    private String channel_name;
    NotificationChannel channel;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewrecipe);

        timerButton = (Button) findViewById(R.id.timerBtn);

        CHANNEL_ID = "recipeTimerChannel";
        createNotificationChannel();
//        channel = new NotificationChannel(CHANNEL_ID, "TimerChannel", NotificationManager.IMPORTANCE_DEFAULT);
//        notificationManager = getSystemService(NotificationManager.class);
//        notificationManager.createNotificationChannel(channel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            while (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.POST_NOTIFICATIONS }, 0);
            }
        }

        //TODO: Get recipe info and populate layouts with proper steps/ingredients
        // cookTimeMins will be set to 0 for now so timer will instantly end when started
        cookTimeMins = 0;

        //maybe need to pass the bundle that came with this activity into this intent as well
        Intent intent = new Intent(this, ViewRecipeActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Random rand = new Random();
        int notifID = rand.nextInt(100000000);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        timerButton.setOnClickListener(v -> {
            Toast timerStarted = Toast.makeText(ViewRecipeActivity.this, "Your timer for " + cookTimeMins + " minutes has started", Toast.LENGTH_SHORT);
            timerStarted.show();
            Log.i("ViewRecipeActivity:", "Timer has been started");

            executor.execute(() -> {
                try {
                    Thread.sleep(cookTimeMins * 60000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                builder = new NotificationCompat.Builder(ViewRecipeActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.carrot_512_1)
                        .setContentTitle("Your " + cookTimeMins + " minute timer is done!")
                        .setContentText("Time for " + recipeTitle)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
                builder.setContentIntent(pendingIntent);
                notificationManager.notify(notifID, builder.build());
                Log.i("ViewRecipeActivity:", "Timer has ended");
            });
        });

    }

    protected void createNotificationChannel() {
        channel_name = getString(R.string.channel_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        channel = new NotificationChannel(CHANNEL_ID, channel_name, importance);
        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}