package com.character.microblogapp.model;

import java.util.ArrayList;

public class MQna extends MBase {
    public class Result {
    public int uid;
    public String reg_time;
    public int usr_uid;
    public String title;
    public String content;
    public String reply;
    public String reply_time;
    public int status = 0;
    public boolean isSelected = false;
    }

    public Result info;
    public ArrayList<Result> arr_list;
}
