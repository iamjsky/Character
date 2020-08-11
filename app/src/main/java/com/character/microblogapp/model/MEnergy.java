package com.character.microblogapp.model;

import java.util.ArrayList;

public class MEnergy extends MBase {
    public class Result {
        public int uid;
        public String reg_time;
        public int usr_uid;
        public int type;
        public int energy;
        public String reason;
    }

    public ArrayList<Result> arr_list;
    public int energy;
}
