package app.mobilecontests.onlinegcapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

    public static final String PREFERENCES_NAME = "OGC";
    private static final String DEFAULT_VALUE_STRING = "";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void addString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = getPreferences(context);
        return sharedPreferences.getString(key, DEFAULT_VALUE_STRING);
    }

    public static void removeKey(Context context, String key) {
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();

    }

    public static void clearPreferences(Context context) {
        SharedPreferences sharedPreferences = getPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
