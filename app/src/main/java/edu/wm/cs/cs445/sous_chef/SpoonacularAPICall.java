package edu.wm.cs.cs445.sous_chef;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

//use gson for json parsing, its like a google library
//we will need to add this to our dependencies pom.xml or equivalent
import com.google.gson.Gson;

//for async
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

//okhttp, which is what we need for android
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;

public class SpoonacularAPICall {
    //class used for interacting with the API
    //want to be able to input a SpoonacularAPIIngredient[] object
        //and get the SpoonacularAPIRecipe[] object back
    private String API_KEY;
    private String BASE_URL = "https://api.spoonacular.com/recipes/findByIngredients?";
    private int num_recipes;
    private String[] ingredients;

    public SpoonacularAPICall(String API_KEY, int num_recipes, String[] ingredients) {
        this.API_KEY = API_KEY;
        this.num_recipes = num_recipes;
        this.ingredients = ingredients;
    }

    public CompletableFuture<Recipe[]> makeCallAsyncOk() {
        CompletableFuture<Recipe[]> result = new CompletableFuture<>();
        OkHttpClient client = new OkHttpClient();

        // Build the URL with HttpUrl
        HttpUrl.Builder urlBuilder = HttpUrl.parse(this.buildUrl()).newBuilder();
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .build();

        // Enqueue the asynchronous call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Complete the CompletableFuture exceptionally in case of failure
                result.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    result.completeExceptionally(new IOException("Unexpected code " + response));
                } else {
                    // Use Gson to parse the response
                    Gson gson = new Gson();
                    Recipe[] recipes = gson.fromJson(response.body().charStream(), Recipe[].class);
                    result.complete(recipes); // Complete the CompletableFuture with the result
                }
            }
        });

        return result;
    }

    private String buildUrl() {
		/*
		awesome string concatenation
		example: "https://api.spoonacular.com/recipes/
			findByIngredients?number=1&ingredients=apples,+flour,+sugar&apiKey=123"
		*/

		/*
		first we put the ingredient substring together
		example: "apples,+flour,+sugar"
		this is a bit crude but should be fine for our purposes
		as long as theres no special characters and there arent a million ingredients
		*/
        String ingredient_substring = "";
        for (String ingredient : this.ingredients) {
            ingredient_substring += ingredient + ",+";

        }
        if (ingredient_substring.length() > 0) {
            //remove the extra ",+" at the end
            ingredient_substring = ingredient_substring.substring(0, ingredient_substring.length() - 2);
        }

        //now we just put the url together
        String url =
            this.BASE_URL +
            "apiKey=" + this.API_KEY +
            "&number=" + this.num_recipes +
            "&ingredients=" + ingredient_substring;

        return url;
    }
}
