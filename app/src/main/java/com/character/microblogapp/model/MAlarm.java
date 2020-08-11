package com.character.microblogapp.model;

import java.util.ArrayList;

public class MAlarm extends MBase {
    public class Result {
    public int uid;
    public String reg_time;
    public int usr_uid;
    public int target_uid;
    public int other_uid;
    public int type;
    public int checked;
    public String nickname;
    }

    public ArrayList<Result> arr_list;
}
