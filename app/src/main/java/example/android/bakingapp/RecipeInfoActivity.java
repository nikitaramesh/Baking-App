package example.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.android.bakingapp.adapters.RecipeInfoAdapter;
import example.android.bakingapp.widget.BakingAppWidgetService;

public class RecipeInfoActivity extends AppCompatActivity {
    public static final String RECIPE_KEY="recipe";

    @BindView(R.id.rv_recipe_step_list)
    RecyclerView recyclerView;

    @BindView(android.R.id.content)
    View parentLayout;

    private Recipe recipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra(RECIPE_KEY)) {
            recipe = intent.getParcelableExtra(RECIPE_KEY);

            setContentView(R.layout.activity_recipe_info);
            ButterKnife.bind(this);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(recipe.getrName());
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            mTwoPane = getResources().getBoolean(R.bool.twoPaneMode);
            if (mTwoPane) {
                if (savedInstanceState == null && !recipe.getrSteps().isEmpty()) {
                    onClick(0);
                }
            }
        }

        setupRecyclerView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupRecyclerView() {
        recyclerView.setAdapter(new RecipeInfoAdapter(this, recipe, new RecipeInfoAdapter.RecipeInfoListItemClickListener() {
            @Override
            public void onListItemClick(int position) {
                onClick(position);
            }
        }));
    }

    private void onClick(int position) {
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(RecipeStepListFragment.STEP_KEY, recipe.getrSteps().get(position));
            RecipeStepListFragment fragment = new RecipeStepListFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_step_info_container, fragment).commit();
        } else {
            Intent intent = new Intent(this, RecipeStepInfoActivity.class);
            intent.putExtra(RecipeStepInfoActivity.RECIPE, recipe);
            intent.putExtra(RecipeStepInfoActivity.STEP_SELECTED, position);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_widget, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_widget) {
            BakingAppWidgetService.updateWidget(this, recipe);
            Toast.makeText(this, R.string.added_to_widget, Toast.LENGTH_SHORT).show();

            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
