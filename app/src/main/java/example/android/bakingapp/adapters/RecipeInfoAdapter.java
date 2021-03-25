package example.android.bakingapp.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.android.bakingapp.Ingredients;
import example.android.bakingapp.R;
import example.android.bakingapp.Recipe;

public class RecipeInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Recipe recipe;
    private RecipeInfoListItemClickListener listener;

    public interface RecipeInfoListItemClickListener {
        void onListItemClick(int position);
    }

    public RecipeInfoAdapter(Context context, Recipe recipe, RecipeInfoListItemClickListener listItemClickListener) {
        this.context = context;
        this.recipe = recipe;
        this.listener = listItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new IngredientsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredient_list_item, parent, false));
        } else {
            return new StepViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_step_list_item, parent, false));
        }
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof IngredientsViewHolder) {
            IngredientsViewHolder viewHolder = (IngredientsViewHolder) holder;
            for (int i=0; i<recipe.getrIngredients().size(); i++) {
                Ingredients ingredients = recipe.getrIngredients().get(i);
                String value1 = (i+1) + ". " + ingredients.getIngredient() + " (" + ingredients.getQuantity() + " " + ingredients.getMeasure() + ")" + "\n";
                viewHolder.ingredientsTextView.append(value1);
            }
        } else if (holder instanceof StepViewHolder) {
            StepViewHolder stepViewHolder = (StepViewHolder) holder;
            stepViewHolder.stepOrderTextView.setText(String.valueOf(position - 1) + ".");
            stepViewHolder.stepNameTextView.setText(recipe.getrSteps().get(position - 1).getShortDescription());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onListItemClick(position - 1);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return recipe.getrSteps().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredients)
        public TextView ingredientsTextView;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(IngredientsViewHolder.this, itemView);
        }
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_step_order)
        public TextView stepOrderTextView;

        @BindView(R.id.tv_step_name)
        public TextView stepNameTextView;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(StepViewHolder.this, itemView);
        }
    }
}
