package example.android.bakingapp;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.android.bakingapp.adapters.StepInfoAdapter;

public class RecipeStepInfoActivity extends AppCompatActivity {

    @BindView(R.id.recipe_step_viewpager)
    ViewPager viewPager;

    @BindView(R.id.next_step)
    Button nextStep;

    @BindView(R.id.prev_step)
    Button prevStep;

    private Recipe recipe;
    private int stepSelected;

    public static final String RECIPE = "recipe";
    public static final String STEP_SELECTED = "step";
    private final String TAG = RecipeStepInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_info);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent.hasExtra(RECIPE) && intent.hasExtra(STEP_SELECTED)) {
            recipe = intent.getParcelableExtra(RECIPE);
            stepSelected = intent.getIntExtra(STEP_SELECTED, 1);
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)  {
            actionBar.setTitle(recipe.getrName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        StepInfoAdapter adapter = new StepInfoAdapter(getApplicationContext(), recipe.getrSteps(), getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(stepSelected);
        Log.d(TAG, "Initial step: "+stepSelected);

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepSelected != recipe.getrSteps().size() - 1) {
                    stepSelected++;
                    adapter.notifyDataSetChanged();
                    viewPager.setCurrentItem(stepSelected);
                    Log.d(TAG, "Next Step selected: "+stepSelected);
                }
            }
        });

        prevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepSelected != 0) {
                    stepSelected--;
                    adapter.notifyDataSetChanged();
                    viewPager.setCurrentItem(stepSelected);

                    Log.d(TAG, "Prev Step selected: "+stepSelected);
                    //adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
