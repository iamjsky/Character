package com.character.microblogapp.model;

import java.util.ArrayList;

public class MCommunity extends MBase {
    public class Result {
        public int uid;
        public String reg_time;
        public int type;
        public int usr_uid;
        public String title;
        public String content;
        public ArrayList<String> add_file;
        public String nickname;
        public ArrayList<String> profile;
        public int review_cnt;
        public int comment_cnt;
        public int like_cnt;
        public int my_like_status;
    }

    //    public Result info;
    public ArrayList<Result> arr_list;
}
