package example.android.bakingapp.retrofit;

import example.android.bakingapp.Recipe;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClientInstance {
    private static volatile RetrofitClientInstance sharedInstance = new RetrofitClientInstance();
    private GetDataService getDataService;
    private final String TAG = RetrofitClientInstance.class.getSimpleName();

    public interface RecipesCallback<T> {
        void onResponse(T result);

        void onCancel();
    }

    private RetrofitClientInstance() {
        if (sharedInstance != null) {
            throw new RuntimeException("Singleton class");
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        getDataService = retrofit.create(GetDataService.class);
    }

    public static RetrofitClientInstance getInstance() {
        if (sharedInstance == null) {
            synchronized (RetrofitClientInstance.class) {
                if (sharedInstance == null) {
                    sharedInstance = new RetrofitClientInstance();
                }
            }
        }
        return sharedInstance;
    }

    public void getRecipes(final RecipesCallback<List<Recipe>> recipesCallback) {
        getDataService.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipesCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.e(TAG,"Request was cancelled");
                    recipesCallback.onCancel();
                } else {
                    Log.e(TAG, t.getMessage());
                    recipesCallback.onResponse(null);
                }
            }
        });
    }
}
