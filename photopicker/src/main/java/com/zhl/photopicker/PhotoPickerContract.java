package com.zhl.photopicker;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

public interface PhotoPickerContract {

    interface View {

        void showPictureList(List<Photo> photoList);

        void showAlbumList(List<Album> albumList, String albumName);

        void showEmpty();

        void showAlbumView(boolean isShow);

        void showMaxSelectTip(int max, int position);

        void setPreviewCount(int count, int max);

        void pickFinish(Intent intent);
    }

    interface Presenter {
        void getData(FragmentActivity activity);

        void changeAlbum(int position);

        void selectPicture(boolean isSelect, int position);

        ArrayList<String> getSelectedPathList();

        ArrayList<String> getAllPictureList();

        boolean isSingle();
    }
}
