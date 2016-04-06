package com.daylon.numbers;

import com.firebase.client.Firebase;

/**
 * Created by Daylon on 4/5/2016.
 */
public class NumbersApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
