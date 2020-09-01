package com.character.microblogapp.data;

import android.support.v4.content.ContextCompat;

import com.character.microblogapp.BuildConfig;
import com.character.microblogapp.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public interface Constant {

    int PERMISSIONS_MEDIA_REQUEST = 1002;       // 갤러리 퍼미션 코드
    int PERMISSIONS_LOCATION_REQUEST = 1006;        // GPS 주소얻기 퍼미션 코드
    int PERMISSIONS_IMAGE_REQUEST = 1002;
    int PERMISSIONS_PHONE_STATE_REQUEST = 1015;

    boolean IS_UITEST = false;

    String SDCARD_FOLDER = "MicroBlogApp";
    String SERVER_URL = "http://175.126.62.103/index.php/api/";
    String DEV_SERVER_URL = BuildConfig.WORK_ENV.equals("YJWORK") ? SERVER_URL : "http://192.168.0.58:8089/index.php/api/";
    String FCM_TOKEN = ""; //fcm_token

    int LOGIN_FACEBOOK = 1;
    int LOGIN_KAKAO = 2;
    int LOGIN_NAVER = 3;
    int LOGIN_EMAIL = 4;

    int PRFILE_STATUS_CHECKING = 0;
    int PRFILE_STATUS_ACCEPT = 1;
    int PRFILE_STATUS_REJECT = -1;

    boolean PAY_TEST = false;
    /// 결제 Key
    String billingKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtvG2RN5yFbW9JDPZHSR8C2DmjoR3CPNu9vcL7NZYwtYU4Pv6hld2RgI8rg3ns5Zy7k+eEBwzXFda7xnWrPYCdXqz87a9DCyTOPcpq+/oMWI+amS0GFJ3O1wXZR1QUiJc+A/JZMJQBYyILf2VABQPPELWATfzguyI9ZzG2+Ib1tRHNpxG9I7DLheGj3RMEmAd0uYU3/V/O+NlKRqWz1qZqeNVWBp/IShkfI0sUIQJW7YwcvYrlsZi3MnXXxi/VSZ4poa9MYCNGXc//RO01vaZDB4h3tvJ2tlGhzJnndkyuTd6YA1B0TPFCt9zWimCvtpDt7n/c+P2xGXjXT5GJMkWTwIDAQAB";

    // 결제 아이템 정리
    String PURCHARSE_ITEM_50 = "purcharse_item_50";
    String PURCHARSE_ITEM_100 = "purcharse_item_100";
    String PURCHARSE_ITEM_200 = "purcharse_item_200";
    String PURCHARSE_ITEM_300 = "purcharse_item_300";
    String PURCHARSE_ITEM_500 = "purcharse_item_500";
    String PURCHARSE_ITEM_1000 = "purcharse_item_1000";
    String PURCHARSE_ITEM_3000 = "purcharse_item_3000";

    int PAGE_COUNT = 20;

    enum LOGIN {
        EMAIL(1),       // 이메일로 로그인
        FACEBOOK(2),    // 페이스북으로 로그인
        NAVEr(3),       // 네이버로 로그인
        KAKAO(4);       // 카카오로 로그인

        public final int value;

        LOGIN(int value) {
            this.value = value;
        }
    }

    enum GENDER {
        MALE(1),       // 남성
        FEMALE(2);       // 여성

        public final int value;

        GENDER(int value) {
            this.value = value;
        }
    }

    String CHARACTER_D = "D";
    String CHARACTER_I = "I";
    String CHARACTER_S = "S";
    String CHARACTER_C = "C";

    int CHARACTER_D_COLOR = ContextCompat.getColor(getApplicationContext(), R.color.char_d_color);
    int CHARACTER_I_COLOR = ContextCompat.getColor(getApplicationContext(), R.color.char_i_color);
    int CHARACTER_S_COLOR = ContextCompat.getColor(getApplicationContext(), R.color.char_s_color);
    int CHARACTER_C_COLOR = ContextCompat.getColor(getApplicationContext(), R.color.char_c_color);

    String[] BLAME_REASON = new String[]{"불쾌감을 주는 언행", "불쾌감을 주는 사진", "사진 도용"};

    boolean CERT_TEST = true; // 납기시에는 false로 설정해야 함.
    String SERVER_CERT_URL = "http://175.126.62.103/cert.php";
//        String SERVER_CERT_URL = "http://175.126.62.103/cert_test1.php";
    int GO_ACT_CERT_PHONE = 2000;
}