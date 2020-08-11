package com.character.microblogapp.model;

import java.util.ArrayList;

public class MChat extends MBase {
    public class Result {
    public int uid;
    public String reg_time;
    public String nickname;
    public int sender_id;
    public int receiver_id;
    public int room_uid;
    public String content;
    public int type;//1-본문
    public String profile_url;
    }

    public ArrayList<Result> arr_list;
}
