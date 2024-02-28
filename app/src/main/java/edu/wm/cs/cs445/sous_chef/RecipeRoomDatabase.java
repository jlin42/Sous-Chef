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
    //TODO -
    // The purpose of this in the tutorial is to clear the database and
    // fill it with sample data
    // Tried to do this, but it didn't work
    // Currently this does not ever get called - but leaving it for now as a reference
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                //populate db in background with sample data
                //only for testing - delete later
                RecipeDAO dao = INSTANCE.recipeDAO();
                dao.deleteAll();
                Recipe recipe = new Recipe("Chicken Pot Pie", "Chicken, bread crumbs, assorted veggies, ...",
                        "30min", false, "recipe link");
                dao.insert(recipe);

                recipe = new Recipe("Meatloaf", "Ground beef, bread crumbs, ketchup, onions, ...",
                        "30min", false, "link");
                dao.insert(recipe);

                recipe = new Recipe("Mac and Cheese", "Macaroni noodles, milk, butter, flour, cheese",
                        "1hr", true, "link");
                dao.insert(recipe);

                recipe = new Recipe("Easy Weeknight Spaghetti and Meat Sauce", "Spaghetti noodles, jarred sauce, ground beef, onions, garlic, ...",
                        "2hr30m", false, "link");
                dao.insert(recipe);

            });
        }
    };
}
