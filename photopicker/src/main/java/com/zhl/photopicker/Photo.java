package com.zhl.photopicker;

/**
 * Created by zhouhl on 2016/7/14.
 * Photo
 */
class Photo {

    public int id;
    public String path;
    public boolean isSelected;

    public Photo(int id, String path) {
        this.id = id;
        this.path = path;
    }
}
