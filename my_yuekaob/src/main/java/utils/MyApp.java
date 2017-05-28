package utils;

import android.app.Application;

import org.xutils.x;

/**
 * Created by 1 on 2017/5/28.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
