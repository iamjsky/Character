package com.character.microblogapp.model;

public class MEstimateResult extends MBase {

    public class User {
        public int uid;
        public String nickname;
        public String address;
        public String job;
        public int age;
        public String school;
        public String character;
        public String ideal_character;
        public String[] profile;// 프로필 이미지 String배렬
        public String body_type;
        public String intro;
        public float score;
    }

    /**
     * 1:다음회원존재,
     * 0:다음회원 존재안함
     */
    public int next_usr_exist;

    //평가후 지급에너지
    public int plus_energy;

    //하루평가회원 넘침깃발(1:overflow,0:정상)
    public int overflow_oneday_cnt;
    public User next_usr_info;

}
