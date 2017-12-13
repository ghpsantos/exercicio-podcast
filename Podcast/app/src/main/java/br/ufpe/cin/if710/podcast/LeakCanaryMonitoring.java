package br.ufpe.cin.if710.podcast;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by albert on 12/12/17.
 */

public class LeakCanaryMonitoring extends Application {
    public static LeakCanaryMonitoring instance;
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        refWatcher = LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher(Context context) {
        LeakCanaryMonitoring application = (LeakCanaryMonitoring) context.getApplicationContext();
        return application.refWatcher;
    }
    public void mustDie(Object object) {
        if (refWatcher != null) {
            refWatcher.watch(object);
        }
    }
}
