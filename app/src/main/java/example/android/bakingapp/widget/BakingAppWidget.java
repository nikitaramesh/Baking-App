package example.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import example.android.bakingapp.MainActivity;
import example.android.bakingapp.R;
import example.android.bakingapp.Recipe;
import example.android.bakingapp.SharedPref;

public class BakingAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Recipe recipe = SharedPref.getRecipe(context);
            if (recipe != null) {
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

                views.setTextViewText(R.id.recipe_widget_name_text, recipe.getrName());
                views.setOnClickPendingIntent(R.id.recipe_widget_name_text, pendingIntent);

                Intent intent = new Intent(context, BakingAppWidgetService.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                views.setRemoteAdapter(R.id.recipe_widget_listview, intent);
                appWidgetManager.updateAppWidget(appWidgetId, views);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_widget_listview);
            }
        }
    }

    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            Recipe recipe = SharedPref.getRecipe(context);
            if (recipe != null) {
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

                views.setTextViewText(R.id.recipe_widget_name_text, recipe.getrName());
                views.setOnClickPendingIntent(R.id.recipe_widget_name_text, pendingIntent);

                Intent intent = new Intent(context, BakingAppWidgetService.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                views.setRemoteAdapter(R.id.recipe_widget_listview, intent);
                appWidgetManager.updateAppWidget(appWidgetId, views);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_widget_listview);
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}

