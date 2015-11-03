package weico.gyx.org.person_comment_client.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by manager on 2015/8/27.
 */
public class ShareUtils {
    //SharedPreferences专用的标签
    private static final String FILE_NAME = "personComment";
    //只用于判断是否为第一次打开app
    private static final String MODE_NAME = "welcome";
    //保存城市名
    public static final String CITY_NAME = "cityName";

    public static final String USER_NAME = "username";

    public static final String FIRST_OPEN_GPS = "loc";

    public static final String OLD_CITY = "oldCity";

    /***
     * 判断是否第一次打开app
     */
    //获取逻辑
    public static boolean getWelcomeBoolean(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context
                .MODE_PRIVATE).getBoolean(MODE_NAME, false);
    }

    //写入逻辑
    public static void putWelcomeBoolean(Context context, boolean isFirst) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
        editor.putBoolean(MODE_NAME, isFirst);
        editor.commit();
    }

    //写入一个String类型的数据
    public static void putCityName(Context context, String cityName) {
        SharedPreferences.Editor editor = context.getSharedPreferences(FILE_NAME
                , Context.MODE_APPEND).edit();
        editor.putString(CITY_NAME, cityName);
        editor.commit();
    }

    //获取String类型的数据
    public static String getCityName(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .getString(CITY_NAME, "选择城市");
    }

    public static void putUserName(Context context, String userName) {
        SharedPreferences.Editor editor =
                context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .getString(USER_NAME, "点击登陆");
    }

    public static void putFirstOpen(Context context, boolean isFirst) {
        SharedPreferences.Editor editor =
                context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
        editor.putBoolean(FIRST_OPEN_GPS, isFirst);
        editor.commit();
    }

    public static boolean getFirstOpen(Context context){
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .getBoolean(FIRST_OPEN_GPS, false);
    }

    public static void putOldCity(Context context, String text){
        SharedPreferences.Editor editor =
                context.getSharedPreferences(FILE_NAME, Context.MODE_APPEND).edit();
        editor.putString(OLD_CITY, text);
        editor.commit();
    }

    public static String getOldCity(Context context){
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
                .getString(OLD_CITY, "正在定位...");
    }


}
