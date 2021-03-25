package example.android.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SharedPref {
    public static final String PREFS_NAME = "PREFS";
    public static final String WIDGET_RECIPE = "WIDGET_RECIPE";

    public static void saveRecipe(Context context, Recipe recipe) {
        ObjectMapper mapper = new ObjectMapper();
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        try {
            prefs.putString(WIDGET_RECIPE, mapper.writeValueAsString(recipe));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        prefs.apply();
    }

    public static Recipe getRecipe(Context context) {
        ObjectMapper mapper = new ObjectMapper();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String recipeBase64 = prefs.getString(WIDGET_RECIPE, "");

        try {
            return "".equals(recipeBase64) ? null : mapper.readValue(prefs.getString(WIDGET_RECIPE, ""), Recipe.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
