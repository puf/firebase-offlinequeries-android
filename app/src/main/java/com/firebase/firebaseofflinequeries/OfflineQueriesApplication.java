package com.firebase.firebaseofflinequeries;

import android.app.Application;
import android.util.Log;

import com.firebase.client.Firebase;

public class OfflineQueriesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("OfflineDinosaurs", "OfflineQueriesApplication.onCreate");

        Firebase.setAndroidContext(this);

        // Uncomment the next line to enable disk persistence, which means that the data will be available upon app restarts
        //Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
