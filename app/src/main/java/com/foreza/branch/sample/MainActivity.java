package com.foreza.branch.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.BRANCH_STANDARD_EVENT;
import io.branch.referral.util.BranchEvent;
import io.branch.referral.util.LinkProperties;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.UUID;

import static com.foreza.branch.sample.BranchDeepLinkHelper.updateDeepLinkToView;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "JC_Branch_Test";

    Boolean branchInitialized = false;

    Button registrationButton;
    Button rateOnly5StarsButton;
    String sampleDeepLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // JC: Test creating buttons that fire off some events.
    private void createEventButtons() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.buttonArea);

        // For buttons created during session, bind a UUID for everything
        String sessionUUID = UUID.randomUUID().toString();

        BranchEvent registrationEventExample = new BranchEvent(BRANCH_STANDARD_EVENT.COMPLETE_REGISTRATION)
                .setTransactionID(sessionUUID)
                .setDescription("User might have created an account")
                .addCustomDataProperty("registrationID", "Jason");

        BranchEvent rateCommerceEventExample = new BranchEvent(BRANCH_STANDARD_EVENT.RATE)
                .setTransactionID(sessionUUID)
                .setDescription("User gave me 5 stars (not that I let them choose..)")
                .addCustomDataProperty("rating", "5");

        registrationButton = BranchButtonHelper.createBranchEventClickButton(this, "Register", registrationEventExample);
        rateOnly5StarsButton = BranchButtonHelper.createBranchEventClickButton(this, "Give me 5 stars", rateCommerceEventExample);
        BranchButtonHelper.addBranchButtonToView(registrationButton, layout);
        BranchButtonHelper.addBranchButtonToView(rateOnly5StarsButton, layout);
    }

    // JC: Test deep linking. Partially works. Unable to open the app directly from link still.
    private void createDeepLink() {
        BranchUniversalObject buo = BranchDeepLinkHelper.createUniversalObject(); // TODO: Make this more configurable
        BranchDeepLinkHelper.generateDeepLink(this, buo, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("BRANCH SDK", "got my Branch link to share: " + url);
                    sampleDeepLink = url;
                    findViewById(R.id.deepLinkText);
                    BranchDeepLinkHelper.updateLinkToView(sampleDeepLink, findViewById(R.id.deepLinkText));
                    BranchDeepLinkHelper.addLinkToClipboard(sampleDeepLink, MainActivity.this);
                }
            }
        });
    }



    // Such a simple app that luckily didn't yet crash.
    private void reportUnlockAchievement() {
        new BranchEvent(BRANCH_STANDARD_EVENT.UNLOCK_ACHIEVEMENT)
                .setCustomerEventAlias("my_lucky_day")
                .setDescription("User opened the app and it didn't crash!")
                .addCustomDataProperty("ANR", "not_today")
                .logEvent(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Don't initialize multiple times
        if (branchInitialized) {
            Log.d(TAG,"Branch SDk already initialized");
            return;
        }


        Branch.sessionBuilder(this).withCallback(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                if (error != null) {
                    Log.e(TAG, "branch init failed. Caused by -" + error.getMessage());
                } else {
                    Log.e(TAG, "branch init complete!");

                    if (branchUniversalObject != null) {
                        Log.d(TAG, "title " + branchUniversalObject.getTitle());
                        Log.d(TAG, "CanonicalIdentifier " + branchUniversalObject.getCanonicalIdentifier());
                        Log.d(TAG, "metadata " + branchUniversalObject.getContentMetadata().convertToJson());
                    }

                    if (linkProperties != null) {
                        Log.d(TAG, "Channel " + linkProperties.getChannel());
                        Log.d(TAG, "control params " + linkProperties.getControlParams());
                    }

                    // If we opened the app via deep link, we'll find out now...
                    updateDeepLinkToView(findViewById(R.id.readDeepLinkFirst), findViewById(R.id.readDeepLinkLast));

                    branchInitialized = true;

                    // JC: Getting this far without crashes could be an achievement, so here we go!
                    reportUnlockAchievement();

                    // Create 2 test buttons to prove that lifecycle/commerce events work
                    createEventButtons();

                    // Create a test deep link (async operation, need to wait before the link itself is created?)
                    createDeepLink();

                    // JC: Consider this proof that I "validated" the integration.
                    // IntegrationValidator.validate(MainActivity.this);
                }
            }
        }).withData(this.getIntent().getData()).init();

    }


    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
        Branch.sessionBuilder(this).withCallback(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error != null) {
                    Log.e(TAG, error.getMessage());
                } else if (referringParams != null) {
                    Log.e(TAG, referringParams.toString());
                }
            }
        }).reInit();
    }
}