package example.android.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepListFragment extends Fragment {

    @BindView(R.id.sv_instructions)
    NestedScrollView instructionsContainer;

    @BindView(R.id.view_exo_player)
    SimpleExoPlayerView exoPlayerView;

    @BindView(R.id.tv_instructions)
    TextView instructionsTextView;

    public static final String STEP_KEY = "STEP";
    public static final String POSITION_KEY = "POSITION";
    public static final String PLAY_WHEN_READY_KEY = "PLAY_WHEN_READY";
    private Unbinder unbinder;
    private SimpleExoPlayer exoPlayer;
    private Steps step;
    private long currentPosition = 0;
    private boolean playWhenReady = true;
    private final String TAG = RecipeStepListFragment.class.getSimpleName();

    public RecipeStepListFragment() {
    }

    public static RecipeStepListFragment newInstance(Steps steps) {

        Bundle args = new Bundle();
        args.putParcelable(STEP_KEY, steps);

        RecipeStepListFragment fragment = new RecipeStepListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_list, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION_KEY)) {
            currentPosition = savedInstanceState.getLong(POSITION_KEY);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
        }

        unbinder = ButterKnife.bind(this, rootView);
        instructionsTextView.setText(step.getDescription());

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(STEP_KEY)) {
            step = getArguments().getParcelable(STEP_KEY);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exoPlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.MATCH_PARENT;
            exoPlayerView.setLayoutParams(params);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) exoPlayerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = params.WRAP_CONTENT;
            exoPlayerView.setLayoutParams(params);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(step.getVideoURL())) {
            initializePlayer(Uri.parse(step.getVideoURL()));
            Log.d(TAG, "Video URL: "+step.getVideoURL());
        } else {
            instructionsContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(POSITION_KEY, currentPosition);
        outState.putBoolean(PLAY_WHEN_READY_KEY, playWhenReady);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, new DefaultLoadControl());

            exoPlayerView.setPlayer(exoPlayer);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, dataSourceFactory, new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);

            if (currentPosition != 0)
                exoPlayer.seekTo(currentPosition);

            exoPlayer.setPlayWhenReady(playWhenReady);
            exoPlayerView.setVisibility(View.VISIBLE);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            playWhenReady = exoPlayer.getPlayWhenReady();
            currentPosition = exoPlayer.getCurrentPosition();

            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}