package example.android.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import example.android.bakingapp.R;
import example.android.bakingapp.Recipe;
import example.android.bakingapp.RecipeViewHolder;

public class RecipesAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    private Context context;
    private List<Recipe> recipes;
    private final ListItemClickListener mOnItemClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int position);
    }

    public RecipesAdapter(Context context, List<Recipe> recipes, ListItemClickListener listItemClickListener) {
        this.context = context;
        this.recipes = recipes;
        this.mOnItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_list_item, viewGroup, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, final int i) {
        recipeViewHolder.recipeNameTextView.setText(recipes.get(i).getrName());
        recipeViewHolder.servingsTextView.setText(context.getString(R.string.servings, recipes.get(i).getrServings()));

        recipeViewHolder.imgRecipe.setImageResource(getImage(recipes.get(i).getrId()));

        recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onListItemClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public int getImage(int id) {
        int imgRes;
        switch (id) {
            case 1:
                imgRes = R.drawable.nutella_pie;
                break;
            case 2:
                imgRes = R.drawable.brownies;
                break;
            case 3:
                imgRes = R.drawable.yellow_cake;
                break;
            case 4:
                imgRes = R.drawable.cheesecake;
                break;
            default:
                imgRes = R.drawable.image_not_found;
                break;
        }
        return imgRes;
    }

}
