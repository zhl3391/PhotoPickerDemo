package com.zhl.photopicker;

import java.util.ArrayList;

/**
 * Created by zhouhl on 2016/7/14.
 * Album
 */
class Album {

    public String id;
    public String coverPath;
    public String name;
    public long   dateAdded;
    public ArrayList<Photo> pictures = new ArrayList<>();
    public boolean isSelected;
}
