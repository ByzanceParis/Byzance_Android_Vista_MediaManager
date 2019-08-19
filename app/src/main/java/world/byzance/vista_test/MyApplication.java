package world.byzance.vista_test;

import android.app.Application;
import android.content.Context;

/**
 * Created by maelle on 09/07/2019.
 */


public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        VistaManager.getInstance();
    }
    public static Context getAppContext(){
        return mContext;
    }

}