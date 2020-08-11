package com.character.microblogapp.model;

public class MUser extends MBase {

    public class Result {
        public int uid;
        public String reg_time;
        public int login_type;
        public String sns_id;
        public String email;
        public String email_type;
        public String pwd;// 비밀번호
        public int gender;// 성별(1-남, 2-여)
        public String nickname;// 닉네임
        public int height;// 키
        public String address;// 주소
        public String school;// 학교
        public String job;// 직업
        public String religion;// 종교
        public String drink;// 음주
        public String smoke;// 흡연
        public String body_type;// 체형
        public String interest;// 관심사(여기서 이용하지 말것, 회원의 관심사를 별도로 블러 오는 부분필요)
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
        public String[] profile;// 프로필 이미지 String배렬
        public int profile_status;// 프로필상태(0-심사중, 1-통과, -1: 거절)
        public int release_date;// 정지해제일
        public int status;// 회원상태(1-정상, 0-탈퇴회원, 2-정지회원)
        public int age;
        public String birthday;
        public String status_memo;
        public String character;        //성격
        public String character_content1;        //성격
        public String character_content2;        //성격
        public String character_content3;        //성격
        public String ideal_character;  //성격특징
        public int is_liked = 0; //내가 좋아요 한 회원인가?
        public String major;// 전공
        public String character_info; //성격설명
        public String character_text; //성격설명
        public String character_director; //성격방향
        public String character_express; //성격표현
        public String character_reason; //성격요인
        public int is_rated_byme = 0; //내가 평가한 사용자인가?
        public int rate_byme = 0; //내가 평가한 점수?
        public int start_height = 0; //시작키
        public int end_height = 0; //종료키
        public String character_detail; //성격정보
    }

    public Result info = new Result();
}
