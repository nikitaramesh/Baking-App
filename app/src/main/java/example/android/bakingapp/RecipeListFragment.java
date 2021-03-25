package example.android.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import example.android.bakingapp.adapters.RecipesAdapter;
import example.android.bakingapp.retrofit.RetrofitClientInstance;
import example.android.bakingapp.widget.BakingAppWidgetService;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecipeListFragment extends Fragment {

    @BindView(R.id.rv_recipe_list)
    public RecyclerView recyclerView;
    @BindView(R.id.pb_recipe_list)
    public ProgressBar progressBar;
    @BindView(R.id.connectivity_error)
    public RelativeLayout connectivityError;

    private Unbinder unbinder;
    private ArrayList<Recipe> recipes;
    private OnRecipeClickListener recipeClickListener;
    private static String RECIPES = "recipes";
    private final String TAG = RecipeListFragment.class.getSimpleName();

    public RecipeListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        progressBar.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        if (isNetworkAvailable(getActivity().getApplicationContext())) {
            connectivityError.setVisibility(View.GONE);
            loadRecipes();
        } else {
            connectivityError.setVisibility(View.VISIBLE);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPES)) {
            recipes = savedInstanceState.getParcelable(RECIPES);

            recyclerView.setAdapter(new RecipesAdapter(getActivity().getApplicationContext(), recipes, new RecipesAdapter.ListItemClickListener() {
                @Override
                public void onListItemClick(int position) {
                    recipeClickListener.onRecipeSelected(recipes.get(position));
                }
            }));
            dataLoadCheck();
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeClickListener) {
            recipeClickListener = (OnRecipeClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnRecipeClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recipeClickListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (recipes != null && !recipes.isEmpty()) {
            outState.putParcelableArrayList(RECIPES, recipes);
        }
    }

    /**
     * In case of change in connectivity
     */
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (recipes == null) {
                loadRecipes();
            }
        }
    };

    private void loadRecipes() {
        if (isNetworkAvailable(getActivity().getApplicationContext())) {

            RetrofitClientInstance.getInstance().getRecipes(new RetrofitClientInstance.RecipesCallback<List<Recipe>>() {
                @Override
                public void onResponse(final List<Recipe> result) {
                    if (result != null) {
                        Log.d(TAG, "result not null " + result.size());
                        recipes = (ArrayList) result;
                        recyclerView.setAdapter(new RecipesAdapter(getActivity().getApplicationContext(), recipes, new RecipesAdapter.ListItemClickListener() {
                            @Override
                            public void onListItemClick(int position) {
                                recipeClickListener.onRecipeSelected(recipes.get(position));
                                Log.d(TAG, "Sending recipe "+position);
                            }
                        }));

                        if (SharedPref.getRecipe(getActivity().getApplicationContext()) == null) {
                            BakingAppWidgetService.updateWidget(getActivity(), recipes.get(0));
                        }

                    } else {
                        Log.e(TAG, "result null");
                        final Snackbar snackbar;
                        snackbar = Snackbar.make(getView(), getString(R.string.error_data_not_fetched), Snackbar.LENGTH_LONG);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setMaxLines(3);

                        snackbar.show();
                    }

                    dataLoadCheck();
                }

                @Override
                public void onCancel() {
                    dataLoadCheck();
                }

            });
        } else {
            final Snackbar snackbar2;
            snackbar2 = Snackbar.make(getView(), getString(R.string.no_internet), Snackbar.LENGTH_LONG);
            View snackBarView = snackbar2.getView();
            snackBarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
            TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(3);
            snackbar2.show();
        }
    }

    private void dataLoadCheck() {
        boolean loaded = recipes != null && recipes.size() > 0;
        recyclerView.setVisibility(loaded ? View.VISIBLE : View.GONE);
        connectivityError.setVisibility(loaded ? View.GONE : View.VISIBLE);
    }

    public interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
