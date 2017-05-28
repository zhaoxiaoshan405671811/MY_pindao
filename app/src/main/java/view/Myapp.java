package view;

import android.app.Application;

import org.xutils.x;

/**
 * Created by 1 on 2017/5/27.
 */

public class Myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
