package edu.wm.cs.cs445.sous_chef;

class SpoonacularAPIRecipeComplex {
    //a wrapper class needed because of formatting
    //in the complex recipe search API call
    private SpoonacularAPIRecipe[] results;
    public SpoonacularAPIRecipe[] getRecipes() {
        return this.results;
    }
}


class SpoonacularAPIRecipe {
    //just all the different fields we are extracting from the json,
    //and getters and setters for all of them bc java is awesome
    private int id;
    private String title;
    private String image;
    private String imageType;
    private int usedIngredientCount;
    private int missedIngredientCount;
    private int likes;
    //see the Ingredient class below for how to access these
    private SpoonacularAPIIngredient[] missedIngredients;
    private SpoonacularAPIIngredient[] usedIngredients;
    private SpoonacularAPIIngredient[] unusedIngredients;

    // awesome getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getImageType() {
        return imageType;
    }

    public int getUsedIngredientCount() {
        return usedIngredientCount;
    }

    public int getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public int getLikes() {
        return likes;
    }

    public SpoonacularAPIIngredient[] getMissedIngredients() {
        return missedIngredients;
    }

    public SpoonacularAPIIngredient[] getUsedIngredients() {
        return usedIngredients;
    }

    public SpoonacularAPIIngredient[] getUnusedIngredients() {
        return unusedIngredients;
    }

    // awesome setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public void setUsedIngredientCount(int usedIngredientCount) {
        this.usedIngredientCount = usedIngredientCount;
    }

    public void setMissedIngredientCount(int missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setMissedIngredients(SpoonacularAPIIngredient[] missedIngredients) {
        this.missedIngredients = missedIngredients;
    }

    public void setUsedIngredients(SpoonacularAPIIngredient[] usedIngredients) {
        this.usedIngredients = usedIngredients;
    }

    public void setUnusedIngredients(SpoonacularAPIIngredient[] unusedIngredients) {
        this.unusedIngredients = unusedIngredients;
    }
}