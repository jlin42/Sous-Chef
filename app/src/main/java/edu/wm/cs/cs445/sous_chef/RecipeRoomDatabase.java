package edu.wm.cs.cs445.sous_chef;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * The first layer above the SQLite database
 * Uses the DAO to issue SQL queries to the DB
 * Does so asynchronously for performance
 */
@Database(entities = {Recipe.class}, version=1, exportSchema=false)
public abstract class RecipeRoomDatabase extends RoomDatabase {
    public abstract RecipeDAO recipeDAO();
    private static volatile RecipeRoomDatabase INSTANCE;
    public static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Creates and returns Room database instance
    static RecipeRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipeRoomDatabase.class, "recipe_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
