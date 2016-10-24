package com.is.contacts;

import android.Manifest;
import android.app.Application;

import com.holidaycheck.permissify.DialogText;
import com.holidaycheck.permissify.PermissifyConfig;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class SeeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        PermissifyConfig permissifyConfig = new PermissifyConfig.Builder()
                .withDefaultTextForPermissions(new HashMap<String, DialogText>() {{
                    put(Manifest.permission_group.CONTACTS, new DialogText(R.string.contact_rationale, R.string.contact_deny_dialog));
                }})
                .build();

        PermissifyConfig.initDefault(permissifyConfig);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
