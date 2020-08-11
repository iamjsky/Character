package com.character.microblogapp.model;

import java.util.ArrayList;

public class MEvent extends MBase {
    public class Result {
    public int uid;
    public String reg_time;
    public String title;
    public String content;
    public String image;
    public String link_url;
    public int type;
    public boolean isSelected = false;
    }

    public ArrayList<Result> arr_list;
}
