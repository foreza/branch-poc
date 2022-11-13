package com.foreza.branch.sample;

// I make the buttons; I return the buttons.

import android.app.ActionBar;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import io.branch.referral.util.BranchEvent;

public class BranchButtonHelper {

    final static String LOGTAG = "BranchButton";

    public static Button createBranchEventClickButton(Context ctx, String btnText, BranchEvent event) {

        Button btn = new Button(ctx);
        btn.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        btn.setText(btnText);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOGTAG, "Clicked");
                Toast.makeText(view.getContext(), "Button Pressed: " + event.getEventName(), Toast.LENGTH_SHORT).show();
                event.logEvent(view.getContext());
            }
        });
        return btn;
    }


    public static void addBranchButtonToView(Button branchButton, LinearLayout layout) {
        layout.addView(branchButton);
    }


}
