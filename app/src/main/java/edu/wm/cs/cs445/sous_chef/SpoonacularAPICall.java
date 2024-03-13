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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

//okhttp, which is what we need for android
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;


public class SpoonacularAPICall {
    //class used for interacting with the API
    //want to be able to input a SpoonacularAPIIngredient[] object
        //and get the SpoonacularAPIRecipe[] object back
    private final String API_KEY;

    public SpoonacularAPICall(String API_KEY) {
        this.API_KEY = API_KEY;
    }

    public CompletableFuture<SpoonacularAPIRecipe[]> getRecipeByIngredients(int num_recipes, String[] ingredients) {
        CompletableFuture<SpoonacularAPIRecipe[]> result = new CompletableFuture<>();
        OkHttpClient client = new OkHttpClient();

        String BASE_URL = "https://api.spoonacular.com/recipes/findByIngredients?";
        String ingredient_substring = this.buildIngredientSubstring(ingredients);

        //now we just put the url together
        String url =
            BASE_URL +
            "apiKey=" + this.API_KEY +
            "&number=" + num_recipes +
            "&ingredients=" + ingredient_substring;

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
                    SpoonacularAPIRecipe[] recipes = gson.fromJson(response.body().charStream(), SpoonacularAPIRecipe[].class);
                    result.complete(recipes); // Complete the CompletableFuture with the result
                }
            }
        });

        return result;
    }

    //helper function for the recipe name api call
    private String buildIngredientSubstring(String[] ingredients) {
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
        StringBuilder ingredient_substring = new StringBuilder();
        for (String ingredient : ingredients) {
            ingredient_substring.append(ingredient).append(",");

        }

        if (ingredient_substring.length() > 0) {
            //remove the extra "," at the end
            ingredient_substring = new StringBuilder(ingredient_substring.substring(0, ingredient_substring.length() - 1));
        }

        return ingredient_substring.toString();
    }

    public CompletableFuture<SpoonacularAPIRecipe[]> getRecipeComplex(int num_recipes,
                                                                      String[] ingredients,
                                                                      String[] diets,
                                                                      String[] intolerances) {

        CompletableFuture<SpoonacularAPIRecipe[]> result = new CompletableFuture<>();
        OkHttpClient client = new OkHttpClient();

        String ingredient_substring = this.buildCommaSeparatedSubstring(ingredients);
        String diet_substring = this.buildCommaSeparatedSubstring(diets);
        String intolerance_substring = this.buildCommaSeparatedSubstring(intolerances);


        String url = String.format(
            "https://api.spoonacular.com/recipes/complexSearch?query=&intolerances=%s&diet=%s&includeIngredients=%s&apiKey=%s&fillIngredients=true&ignorePantry=true&sort=max-used-ingredients&number=%d",
            intolerance_substring,
            diet_substring,
            ingredient_substring,
            this.API_KEY,
            num_recipes
        );

        System.out.println("API call #1 URL: " + url);

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
                    SpoonacularAPIRecipeComplex complex = gson.fromJson(response.body().charStream(), SpoonacularAPIRecipeComplex.class);
                    result.complete(complex.getRecipes()); // Complete the CompletableFuture with the result
                }
            }
        });

        return result;
    }

    private String buildCommaSeparatedSubstring(String[] ingredients) {
        StringBuilder ingredient_substring = new StringBuilder();
        for (String ingredient : ingredients) {
            ingredient_substring.append(ingredient).append(",");
        }

        if (ingredient_substring.length() > 0) {
            //remove the extra "," at the end
            ingredient_substring = new StringBuilder(
                    ingredient_substring.substring(0, ingredient_substring.length() - 1));
        }

        return ingredient_substring.toString();
    }


    //instructions api call
    public CompletableFuture<String> getInstructionsById(int id) {
        CompletableFuture<String> result = new CompletableFuture<>();
        OkHttpClient client = new OkHttpClient();

        /*
        example api all
        https://api.spoonacular.com/recipes/632197/analyzedInstructions?apiKey=b9b5e71ca3c740b8be89bd337d366ce0
        */

        //this url is a lot easier to build so lets one line it
        String url = String.format(
            "https://api.spoonacular.com/recipes/%d/analyzedInstructions?apiKey=%s",
            id,
            this.API_KEY
        );

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
                    String parsedInstructions = formatInstructions(response.body().string());
                    result.complete(parsedInstructions);
                }
            }
        });

        return result;
    }

    //helper function for the instructions api call
    private String formatInstructions(String instructions) {
        Gson gson = new Gson();
        SpoonacularAPIInstructionList[] instructionList = gson.fromJson(instructions, SpoonacularAPIInstructionList[].class);
        StringBuilder formattedInstructions = new StringBuilder();

        for (SpoonacularAPIInstruction instruction : instructionList[0].getInstructions()) {
            // Assuming 'number' is an appropriately formatted string or can be directly used here.
            // Append the instruction number and step to the StringBuilder.
            // Then append a newline character to separate each instruction.
            formattedInstructions
                    .append(instruction.getNumber())
                    .append(". ")
                    .append(instruction.getStep())
                    .append("\n");
        }

        // Convert the StringBuilder content to a String and return it
        return formattedInstructions.toString();
    }

    public CompletableFuture<SpoonacularAPIRecipeInfo> getRecipeInfoById(int id) {
        CompletableFuture<SpoonacularAPIRecipeInfo> result = new CompletableFuture<>();
        OkHttpClient client = new OkHttpClient();

        String url = String.format(
                "https://api.spoonacular.com/recipes/%d/information?includeNutrition=false&apiKey=%s",
                id,
                this.API_KEY
        );

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
                    Gson gson = new Gson();
                    SpoonacularAPIRecipeInfo recipeInfo
                            = gson.fromJson(response.body().string(), SpoonacularAPIRecipeInfo.class);
                    result.complete(recipeInfo);
                }
            }
        });

        return result;
    }

    public CompletableFuture<SpoonacularAPIRecipeInfo[]> getBulkRecipeInfoById(String[] idList) {
        CompletableFuture<SpoonacularAPIRecipeInfo[]> result = new CompletableFuture<>();
        OkHttpClient client = new OkHttpClient();

        //example url https://api.spoonacular.com/recipes/informationBulk?ids=715538,716429
        // https://api.spoonacular.com/recipes/informationBulk?ids=715538,716429

        String id_substring = this.buildCommaSeparatedSubstring(idList);

        String url = String.format(
                "https://api.spoonacular.com/recipes/informationBulk?ids=%s&includeNutrition=false&apiKey=%s",
                id_substring,
                this.API_KEY
        );

        System.out.println("API call #2 URL: " + url);

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
                    Gson gson = new Gson();
                    SpoonacularAPIRecipeInfo[] recipeInfo
                            = gson.fromJson(response.body().string(), SpoonacularAPIRecipeInfo[].class);
                    result.complete(recipeInfo);
                }
            }
        });

        return result;
    }

}

class SpoonacularAPIRecipeInfo {
    public int readyInMinutes;
    public SpoonacularAPIInstructionList[] analyzedInstructions;
    public SpoonacularAPIIngredient[] extendedIngredients;

    public int getReadyInMinutes() {
        return this.readyInMinutes;
    }

    public String getFormattedInstructions() {
        Gson gson = new Gson();
        SpoonacularAPIInstructionList[] instructionList = this.analyzedInstructions;
        StringBuilder formattedInstructions = new StringBuilder();

        for (SpoonacularAPIInstruction instruction : instructionList[0].getInstructions()) {
            formattedInstructions
                    .append(instruction.getNumber())
                    .append(". ")
                    .append(instruction.getStep())
                    .append("\n");
        }

        //convert the StringBuilder content to a String and return it
        return formattedInstructions.toString();
    }

    public String getFormattedIngredients() {
        Gson gson = new Gson();
        SpoonacularAPIIngredient[] ingredientList = this.extendedIngredients;
        StringBuilder formattedInstructions = new StringBuilder();

        for (SpoonacularAPIIngredient ingredient : ingredientList) {
            formattedInstructions
                    .append(ingredient.getName())
                    .append(", ");
        }

        if (formattedInstructions.length() > 0) {
            //remove the extra ", " at the end
            formattedInstructions = new StringBuilder(
                    formattedInstructions.substring(0, formattedInstructions.length() - 2));
        }

        //convert the StringBuilder content to a String and return it
        return formattedInstructions.toString();
    }

}


//the InstructionList and Instruction subclasses are used for Gson parsing
//low key ignore them
class SpoonacularAPIInstructionList {
    public SpoonacularAPIInstruction[] steps;

    public SpoonacularAPIInstruction[] getInstructions() {
        return steps;
    }

    public void setInstructions(SpoonacularAPIInstruction[] steps) {
        this.steps = steps;
    }
}

//and another subclass for Gson
class SpoonacularAPIInstruction {
    public String number;
    public String step;

    public String getNumber() {
        return number;
    }

    public String getStep() {
        return step;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setStep(String step) {
        this.step = step;
    }
}