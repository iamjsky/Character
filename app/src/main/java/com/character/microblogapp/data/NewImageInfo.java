package com.character.microblogapp.data;

import java.io.File;

public class NewImageInfo extends BaseInfo implements Comparable<NewImageInfo> {
    public File image;
    public String img_url;
    public int imgPosition;

    public NewImageInfo(File file) {
        image = file;
    }
    public NewImageInfo(String url) {
        img_url = url;
    }
    public NewImageInfo(){

    }


    @Override
    public int compareTo(NewImageInfo o) {
        if (this.imgPosition < o.imgPosition) {
            return -1;
        } else if (this.imgPosition > o.imgPosition) {
            return 1;
        }

        return 0;
    }
}
