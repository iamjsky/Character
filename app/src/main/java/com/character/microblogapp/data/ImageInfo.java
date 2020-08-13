package com.character.microblogapp.data;

import java.io.File;

public class ImageInfo extends BaseInfo {
    public File image;
    public String img_url;


    public ImageInfo(File file) {
        image = file;
    }
    public ImageInfo(String url) {
        img_url = url;
    }




}
