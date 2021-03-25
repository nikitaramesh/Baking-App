package example.android.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.recipe_img)
    public ImageView imgRecipe;

    @BindView(R.id.tv_recipe_name)
    public TextView recipeNameTextView;

    @BindView(R.id.tv_servings)
    public TextView servingsTextView;

    public RecipeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
