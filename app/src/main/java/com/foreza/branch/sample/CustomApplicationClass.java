package com.foreza.branch.sample;

import io.branch.referral.Branch;
import android.app.Application;

public class CustomApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Branch logging for debugging
        Branch.enableLogging();
        // Enable for integration testing..
//        Branch.enableTestMode();
        Branch.getAutoInstance(this);
    }
}