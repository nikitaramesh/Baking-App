package example.android.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements RecipeListFragment.OnRecipeClickListener {

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, RecipeInfoActivity.class);
        intent.putExtra(RecipeInfoActivity.RECIPE_KEY, (Parcelable) recipe);
        Log.d(TAG, "starting activity RecipeInfo with recipe "+recipe.getrId());
        startActivity(intent);
    }
}
