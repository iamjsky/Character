package com.character.microblogapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.character.microblogapp.model.MCharacterInfo;
import com.character.microblogapp.model.MUser;
import com.character.microblogapp.util.PrefMgr;

public class MyInfo extends BaseUserInfo {

    private static MyInfo singleton;

    public int uid;
    public String fcm_token;
    public String email;
    public String pwd;
    public int login_type;

    public String sns_id;
    public String email_type;
    public String school;// 학교
    public String religion;// 종교
    public String drink;// 음주
    public String smoke;// 흡연
    public String body_type;// 체형
    public String interest;// 관심사(,으로 구분하여 다중)
    public String love_style;// 연애스타일(,으로 구분하여 다중)
    public String intro;// 소개글
    public int energy;// 보유에너지
    public int alarm_sound;// 소리및진동(1-On)
    public int alarm_recommend;// 오늘의 추천(1-On)
    public int alarm_high_score;// 나에게 온 높은 점수(1-On)
    public int alarm_favor;// 나에게 온 호감(1-On)
    public int alarm_match;// 만남성공(1-On)
    public int alarm_chat;// 채팅(1-On)
    public int alarm_notice;// 이벤트/공지(1-On)
    public int profile_status;// 프로필상태(0-심사중, 1-통과, -1: 거절)
    public int release_date;// 정지해제일
    public int status;// 회원상태(1-정상, 0-탈퇴회원, 2-정지회원)
    public String birthday;
    public String status_memo;
    public MCharacterInfo.Info result;

    public int select_man = 0;
    public int star_value = 0;

    public static MyInfo getInstance() {
        if (singleton == null) {
            singleton = new MyInfo();
        }

        return singleton;
    }

    public void load(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        PrefMgr prefMgr = new PrefMgr(prefs);

        fcm_token = prefMgr.getString(PrefMgr.FCM_TOKEN, "");
        email = prefMgr.getString(PrefMgr.USER_EMAIL, "");
        pwd = prefMgr.getString(PrefMgr.USER_PWD, "");
        login_type = prefMgr.getInt(PrefMgr.USER_LOGIN_TYPE, 0);
        uid = prefMgr.getInt(PrefMgr.USER_UID, 0);
    }

    public void save(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(PrefMgr.USER_UID, uid);
        editor.putString(PrefMgr.USER_EMAIL, email);
        editor.putString(PrefMgr.USER_PWD, pwd);
        editor.putInt(PrefMgr.USER_LOGIN_TYPE, login_type);
        editor.apply();
    }

    public void saveFcmToken(Context context, String fcm_token) {
        this.fcm_token = fcm_token;
        SharedPreferences prefs = context.getSharedPreferences(PrefMgr.APP_PREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PrefMgr.FCM_TOKEN, this.fcm_token);
        editor.apply();
    }

    public void transformData(MUser user) {
        this.uid = user.info.uid;
        this.sns_id = user.info.sns_id;
        this.email_type = user.info.email_type;
        this.gender = user.info.gender;
        this.nickname = user.info.nickname;
        this.height = user.info.height;
        this.address = user.info.address;
        this.school = user.info.school;
        this.job = user.info.job;
        this.religion = user.info.religion;
        this.drink = user.info.drink;
        this.smoke = user.info.smoke;
        this.body_type = user.info.body_type;
        this.interest = user.info.interest;
        this.love_style = user.info.love_style;
        this.intro = user.info.intro;
        this.energy = user.info.energy;
        this.alarm_sound = user.info.alarm_sound;
        this.alarm_recommend = user.info.alarm_recommend;
        this.alarm_high_score = user.info.alarm_high_score;
        this.alarm_favor = user.info.alarm_favor;
        this.alarm_match = user.info.alarm_match;
        this.alarm_chat = user.info.alarm_chat;
        this.alarm_notice = user.info.alarm_notice;
        this.profile = user.info.profile;
        this.profile_status = user.info.profile_status;
        this.release_date = user.info.release_date;
        this.status = user.info.status;
        this.age = user.info.age;
        this.birthday = user.info.birthday;
        this.character = user.info.character;
        this.ideal_character = user.info.ideal_character;
        this.status_memo = user.info.status_memo;
    }
}
