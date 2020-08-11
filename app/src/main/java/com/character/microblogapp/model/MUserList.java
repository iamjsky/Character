package com.character.microblogapp.model;

public class MUserList extends MBase {

    public static class User {
        public int uid;
        public String[] profile;// 프로필 이미지
        public int gender;// 성별(1-남, 2-여)
        public String nickname;
        public String address;
        public String job;
        public String school;
        public int dday;
        public int age;
        public int height;
        public String ideal_character;
        public String character;
        public String body_type;
        public boolean isPublic = false;    // 프로필 공개여부
        public int is_choose_state = 0; //홈에서 선택상태, 0:초기, 1:뒤면, 2:선택
        public int visit = 0; //방문상태
        public int login_type = 4; // 1 ? facebook 2 ? kakao 3 ? neiver 4 ? email
        public int is_pay = 0; //초기 1 ? 지불
        public String pay_title = "";

        public Boolean diplay_state_home = false;
    }

    public User[] arr_list;
    public int page_cnt;
    public int remain_seconds; //홈에서 오늘의 만남 목록을 내려보낼때 오늘의 만남까지 남은 시간을 초로 계산하여 내려보낸다.- 반드시 서버시간에 맞춰 서버에서 내려보내야 함.
    public int unread_noti_cnt; // 읽지 않은 알람갯수
}
