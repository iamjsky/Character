package com.character.microblogapp.model;

import java.util.ArrayList;

public class MChatRoom extends MBase {
    public class Result {
    public int uid;
    public String reg_time;
    public int status;
    public int other_usr_uid;
    public int open_usr_uid;
    public String nickname;
    public ArrayList<String> profile;
    public String last_chat_content;
    public int last_chat_type;
    public String last_chat_time;
    public int unread_count;
    }

    public ArrayList<Result> arr_list;
}
