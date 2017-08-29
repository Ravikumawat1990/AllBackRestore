package backuprestore.udr.rk.allbackuprestore;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.support.multidex.MultiDex;

import java.io.IOException;

import backuprestore.udr.rk.allbackuprestore.database.DbHelper;
import backuprestore.udr.rk.allbackuprestore.util.CM;
import backuprestore.udr.rk.allbackuprestore.util.CV;


public class BaseApp extends Application {

    //initialize font typeface
    public Typeface mTypeFaceRobotoBlack;
    public Typeface mTypeFaceRobotoLight;

    public Context mContext;


    public static SQLiteDatabase sqLiteDatabase;

    private static BaseApp mInstance;

    public static synchronized BaseApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        // MultiDex.install(this);
        //Initialize font typeface and accessing instance font wise
        initializeTypeFace();

        //initialize Database
        mContext = getApplicationContext();

        CV.UDID = CM.getUDID(mContext);


        CV.ACCESS_TOKEN = (String) CM.getSp(mContext, CV.USER_ACCESS_TOKEN, "");

        //Log.e("FxNewsStand app", "isDayMode: " + (Boolean) CM.getSp(mContext, CV.IS_DAY_MODE, true));
//
        DbHelper dbHelper = new DbHelper(mContext);
        try {
            dbHelper.createDataBase();
            sqLiteDatabase = dbHelper.openDataBase();
            CM.copyDataBase(mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * Access direct font-face instance  in settypeface method
     */
    private void initializeTypeFace() {


        mTypeFaceRobotoBlack = Typeface.createFromAsset(getAssets(), getResources().getString(
                R.string.FontFace_DroidSerif_Bold));
        mTypeFaceRobotoLight = Typeface.createFromAsset(getAssets(), getResources().getString(
                R.string.FontFace_DroidSerif_Regular));


    }


}
