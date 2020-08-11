package com.character.microblogapp.fcm;

// should be opened

// should be opened

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.character.microblogapp.MyApplication;
import com.character.microblogapp.R;
import com.character.microblogapp.ui.page.intro.Intro2Activity;
import com.character.microblogapp.ui.page.intro.IntroActivity;
import java.util.List;

import com.character.microblogapp.ui.page.intro.SplashActivity;
import com.character.microblogapp.ui.page.main.MainActivity;
import com.character.microblogapp.ui.page.profile.ProfileActivity;
import com.character.microblogapp.ui.page.setting.AlarmListActivity;
import com.character.microblogapp.ui.page.setting.ChatActivity;
import com.character.microblogapp.util.PrefMgr;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = FirebaseMessagingService.class.getSimpleName();

        @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().containsKey("title") ? remoteMessage.getData().get("title") : "알수없는 메세지";
        String type = remoteMessage.getData().containsKey("type") ? remoteMessage.getData().get("type") : "";
        String message = remoteMessage.getData().containsKey("message") ? remoteMessage.getData().get("message") : "";
        String usr_uid = remoteMessage.getData().containsKey("usr_uid") ? remoteMessage.getData().get("usr_uid") : "";
        String other_uid = remoteMessage.getData().containsKey("other_uid") ? remoteMessage.getData().get("other_uid") : "";
        String nick_name = remoteMessage.getData().containsKey("nick_name") ? remoteMessage.getData().get("nick_name") : "";

        sendNotification(title, type, message, usr_uid, other_uid, nick_name);
    }

    private void sendNotification(String title, String type, String message, String usr_id, String other_id, String nick_name) {
        Log.d(TAG, "title: " + title);
        Log.d(TAG, "message: " + message);
        Log.d(TAG, "type: " + type);

//        type = 1 ? 관리자가 보낸 push
//        type = 10001 ? 오늘의 만남
//        type = 10002 ? 이상형 소개
//        type = 10003 ? 공지사항
//        type = 10004 ? 이벤트
//        type = 10005 ? 호감받았을때
//        type = 10006 ? 높은점수 받았을 때
//        type = 10007 ? 채팅요청 받았을 때
//        type = 10008 ? 채팅방이 열렸을 때
//        type = 10009 ? 신고 받았을 때
//        type = 10010 ? 포인트 사용
//        type = 10011 ? 포인트 획득
//        type = 10012 ? 포인트 구매
//        type = 10013 ? 약관 변경
//        type = 10014 ? 회원승인대기
//        type = 10015 ? 회원승인
//        type = 10016 ? 회원부결
//        type = 10017 ? 활동정지

        Intent intent = new Intent(MyApplication.getInstance().getBaseContext(), Intro2Activity.class);

        if (!isApplicationRunningBackground(getApplicationContext()) && type.equals("10015")) {
            SharedPreferences prefs = getApplicationContext().getSharedPreferences(PrefMgr.APP_PREFS,
                    Context.MODE_PRIVATE);
            PrefMgr prefMgr = new PrefMgr(prefs);
            prefMgr.put(PrefMgr.WATCHED_PROFILE_ACCEPTED, true);
            EventBus.getDefault().post(new EventMessage(10015, null));
        } else if (!isApplicationRunningBackground(getApplicationContext()) && type.equals("10005")) {
            EventBus.getDefault().post(new EventMessage(10005, null));
            intent = new Intent(MyApplication.getInstance().getBaseContext(), AlarmListActivity.class);
            intent.putExtra("id", Integer.parseInt(other_id));
            intent.putExtra("other_id", Integer.parseInt(usr_id));
            intent.putExtra("nickname", nick_name);
        } else if (!isApplicationRunningBackground(getApplicationContext()) && type.equals("10007")) {
            EventBus.getDefault().post(new EventMessage(10007, null));
            intent = new Intent(MyApplication.getInstance().getBaseContext(), ChatActivity.class);
            intent.putExtra("id", Integer.parseInt(other_id));
            intent.putExtra("other_id", Integer.parseInt(usr_id));
            intent.putExtra("nickname", nick_name);
        } else if(isApplicationRunningBackground(getApplicationContext())) {
            //앱을 실행합니다.
            if (type.equals("10008")) {
                intent = new Intent(MyApplication.getInstance().getBaseContext(), MainActivity.class);
                intent.putExtra("tab", 13000);
            } else if (type.equals("10005")){
                intent = new Intent(MyApplication.getInstance().getBaseContext(), MainActivity.class);
                intent.putExtra("tab", 11000);
            } else if (type.equals("10006")){
                intent = new Intent(MyApplication.getInstance().getBaseContext(), MainActivity.class);
                intent.putExtra("tab", 11300);
            }

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        } else {
            if (type.equals("10008")) {
                intent.putExtra("tab", 13000);
            } else if (type.equals("10005")){
                intent.putExtra("tab", 11000);
            } else if (type.equals("10006")){
                intent.putExtra("tab", 11300);
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        }

        EventBus.getDefault().post(new EventMessage(20000, null));


        PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getInstance().getBaseContext(),
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        /**
         * 오레오 버전부터는 Notification Channel이 없으면 푸시가 생성되지 않는 현상이 있습니다.
         * **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channel = "my_channel_01";
            String channel_nm = MyApplication.getInstance().getBaseContext().getResources().getString(R.string.app_name);

            NotificationManager notichannel = (NotificationManager) MyApplication.getInstance().getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channelMessage.setDescription(MyApplication.getInstance().getBaseContext().getResources().getString(R.string.app_name));
            channelMessage.enableLights(true);
            channelMessage.enableVibration(true);
            channelMessage.setShowBadge(false);
            channelMessage.setVibrationPattern(new long[]{100, 200, 100, 200});
            notichannel.createNotificationChannel(channelMessage);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(MyApplication.getInstance().getBaseContext(), channel)
                            .setSmallIcon(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.noticon : R.drawable.ic_icon_notification)
                            .setColor(0xffb0b0b0)
                            .setContentTitle(MyApplication.getInstance().getBaseContext().getResources().getString(R.string.app_name))
                            .setContentText(message)
                            .setChannelId(channel)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager) MyApplication.getInstance().getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

//            notificationBuilder.setSound(defaultSoundUri);
            notificationManager.notify(9999, notificationBuilder.build());

        } else {
            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                    MyApplication.getInstance().getBaseContext()).setSmallIcon(R.drawable.ic_icon_notification)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

//            if (MyInfo.getInstance().userInfo.alarm == 1) {
//                notificationBuilder.setSound(defaultSoundUri);
//            }

            NotificationManager notificationManager = (NotificationManager) MyApplication.getInstance().getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }
	
	static boolean isApplicationRunningBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(am.getRunningAppProcesses().size());
        boolean isBackground = true;
        for (ActivityManager.RunningTaskInfo info : tasks) {
            if (info.topActivity.getPackageName().equals(context.getPackageName())) {
                isBackground = false;
                break;
            }
        }
        return isBackground;
    }

}
