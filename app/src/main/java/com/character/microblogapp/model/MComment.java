package com.character.microblogapp.model;

import java.util.ArrayList;

public class MComment extends MBase {
    public class Comment {
        public int uid;
        public int usr_uid;
        public String reg_time;
        public String content;
        public String nickname;
        public String profile;
    }

    public int total_cnt;
    public ArrayList<Comment> comment;
}
