package com.foreza.branch.sample;

import io.branch.referral.Branch;
import android.app.Application;

public class CustomApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Branch logging for debugging
        Branch.enableLogging();
//        Branch.enableTestMode();

//
//        // Branch object initialization
        Branch.getAutoInstance(this);
    }
}