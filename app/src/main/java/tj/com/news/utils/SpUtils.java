package tj.com.news.utils;

/**
 * Created by Jun on 17/4/13.
 */

import android.content.Context;
import android.content.SharedPreferences;


/**
 * SharedPreferences 的封装
 */
public class SpUtils {
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }
    public static void putBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,defValue).commit();
    }
    public static String getString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getString(key, value);
    }
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }
    public static int getInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getInt(key, value);
    }
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }
}
