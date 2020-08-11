package com.character.microblogapp;

import android.os.Environment;
import android.support.multidex.MultiDexApplication;

import com.character.microblogapp.data.MyInfo;
import com.character.microblogapp.ui.page.setting.ChatActivity;
import com.character.microblogapp.util.KaKaoSDKAdapter;
import com.character.microblogapp.util.Util;
import com.kakao.auth.KakaoSDK;

import java.io.File;

import static com.character.microblogapp.data.Constant.SDCARD_FOLDER;

public class MyApplication extends MultiDexApplication {

    private static volatile MyApplication instance = null;

    private ChatActivity chatActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // SD 카드안의 파일 모두 삭제
        deleteSDCard();

        // 로그인한 유저정보 로드
        MyInfo.getInstance().load(this);

        KakaoSDK.init(new KaKaoSDKAdapter());
    }

    @Override
    public void onTerminate() {
        instance = null;
        super.onTerminate();
    }

    private void deleteSDCard() {
        File rootDir = new File(Environment.getExternalStorageDirectory()
                + File.separator + SDCARD_FOLDER);
        if (rootDir.exists()) {
            Util.deleteDir(rootDir.getPath());
        }
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }


    public ChatActivity getChatActivity() {
        return chatActivity;
    }

    public void setChatActivity(ChatActivity chatActivity) {
        this.chatActivity = chatActivity;
    }
}
