package pro.gofman.mega;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by roman on 10.07.16.
 */

public class Mega extends Application {

    protected static final String SERVICE_SYNCDATA = "pro.gofman.trade.action.syncdata";
    protected static final String SERVICE_LOGCOORD = "pro.gofman.trade.action.logcoord";
    protected static final String SERVICE_PARAM = "pro.gofman.trade.extra.param";



    private static Mega sInstance;
    private static DB db;
    //private static FastItemAdapter fia;


    public static Mega getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public synchronized static DB getWritableDatabase() {
        if ( db == null ) {
            db = new DB( getAppContext() );
        }
        return db;
    }

//    public synchronized static FastItemAdapter getFastItemAdapter() {
//        if ( fia == null ) {
//            fia = new FastItemAdapter();
//        }
//        return fia;
//    }

    @Override
    public void onCreate() {
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException e) {

        }
        super.onCreate();
        sInstance = this;
        db = new DB(this);
        //fia = new FastItemAdapter();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static void saveToPreferences(Context context, String preferenceName, boolean preferenceValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    public static boolean readFromPreferences(Context context, String preferenceName, boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean(preferenceName, defaultValue);
    }
}
