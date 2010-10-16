package com.pivotallabs.tracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.pivotallabs.R;
import com.pivotallabs.api.ApiGateway;

public class RecentActivityActivity extends Activity {

    ApiGateway apiGateway = new ApiGateway();
    SignInDialog signInDialog;
    private TrackerAuthenticator trackerAuthenticator;
    private RecentActivities recentActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker_recent_activity_layout);

        trackerAuthenticator = new TrackerAuthenticator(apiGateway, this);

        recentActivities = new RecentActivities(apiGateway, trackerAuthenticator);
        if (!trackerAuthenticator.isAuthenticated()) {
            showSignInDialog();
        } else {
            update();
        }
    }

    private void update() {
        recentActivities.update();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        MenuItem signOutMenuItem = menu.add("Sign Out");
        signOutMenuItem.setEnabled(trackerAuthenticator.isAuthenticated());
        signOutMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                trackerAuthenticator.signOut();
                finish();
                return true;
            }
        });
        return true;
    }

    private void showSignInDialog() {
        signInDialog = new SignInDialog(this, trackerAuthenticator);
        signInDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (!trackerAuthenticator.isAuthenticated()) {
                    finish();
                }
            }
        });
        signInDialog.show();
    }
}