package com.character.microblogapp.model;

public class MCurrentEstimateUser extends MBase {

    public class User {
        public int uid;
        public String nickname;
        public String address;
        public String job;
        public int age;
        public String school; // add yj 2020-04-13
        public String character;
        public String ideal_character;
        public String[] profile;// 프로필 이미지 String배렬
        public String body_type;
        public String intro;
        public float score;
    }



    public int overflow_oneday_cnt;
    public User info;


}
