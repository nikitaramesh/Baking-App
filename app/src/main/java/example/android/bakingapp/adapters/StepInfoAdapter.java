package example.android.bakingapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

import example.android.bakingapp.RecipeStepListFragment;
import example.android.bakingapp.Steps;

public class StepInfoAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Steps> steps;
    private final String TAG = StepInfoAdapter.class.getSimpleName();

    public StepInfoAdapter(Context context, List<Steps> steps, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
        this.steps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeStepListFragment.STEP_KEY, steps.get(position));
        Log.d(TAG, "Position: "+position);
        RecipeStepListFragment fragment = new RecipeStepListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return steps.size();
    }
}
