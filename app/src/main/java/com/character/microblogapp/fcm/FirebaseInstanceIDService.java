package com.character.microblogapp.fcm;

import android.util.Log;

import com.character.microblogapp.data.Constant;
import com.character.microblogapp.data.MyInfo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService implements Constant{

    private static final String TAG = FirebaseInstanceIDService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        MyInfo.getInstance().saveFcmToken(getApplicationContext(), token);
    }
}