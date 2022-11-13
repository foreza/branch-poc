package com.foreza.branch.sample;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.foreza.branch.sample.MainActivity.TAG;

public class BranchDeepLinkHelper {

    public static BranchUniversalObject createUniversalObject () {
        BranchUniversalObject buo = new BranchUniversalObject()
                .setCanonicalIdentifier("content/12345")
                .setTitle("How neat is this?!")
                .setContentDescription("My Content Description")
                .setContentImageUrl("https://picsum.photos/200/300")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentMetadata(new ContentMetadata().addCustomMetadata("secretToLife", "42"));
        return buo;
    }

    public static void generateDeepLink (Context ctx, BranchUniversalObject buo, Branch.BranchLinkCreateListener listener) {
        LinkProperties lp = new LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing")
                .setCampaign("content 1234 launch scream vomit")
                .setStage("new user")
                .addControlParameter("$desktop_url", "https://jasonthechiu.com/")
                .addControlParameter("$deeplink_path", "open")
                .addControlParameter("custom", "data")
                .addControlParameter("custom_random", Long.toString(Calendar.getInstance().getTimeInMillis()));

        // Create the URL and put it back to the listener.s
        buo.generateShortUrl(ctx, lp, listener);
    }


    public static void updateLinkToView(String linkURL, View view) {
        TextView tv = (TextView) view;
        tv.setText(linkURL);
    }


    public static void addLinkToClipboard(String linkURL, Context ctx) {
        ClipboardManager clipBoard;
        clipBoard = (ClipboardManager)ctx.getSystemService(CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("simple text", linkURL);
        clipBoard.setPrimaryClip(clip);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
            Toast.makeText(ctx, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }


    public static void updateDeepLinkToView(View latestView1, View latestView2) {
        // latest

        try {
            JSONObject sessionParams = Branch.getInstance().getLatestReferringParams();
            // first
            JSONObject installParams = Branch.getInstance().getFirstReferringParams();

            ((TextView) latestView1).setText(sessionParams.toString());
            ((TextView) latestView2).setText(installParams.toString());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

}
