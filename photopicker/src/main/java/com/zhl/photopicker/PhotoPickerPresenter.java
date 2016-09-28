package com.zhl.photopicker;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoPickerPresenter implements PhotoPickerContract.Presenter{

    private int mMax;
    private PhotoPickerContract.View mView;
    private List<Album> mAlbumList;
    private ArrayList<String> mSelectedPathList = new ArrayList<>();
    private Album mSelectedAlbum;

    private boolean mIsSingle;
    private boolean mIsShowGif;

    public PhotoPickerPresenter(PhotoPickerContract.View view, Bundle options) {
        mView = view;

        mMax = options.getInt(Picker.EXTRA_MAX, 1);
        mIsSingle = options.getBoolean(Picker.EXTRA_IS_SINGLE, false);
        mIsShowGif = options.getBoolean(Picker.EXTRA_IS_SHOW_GIF, false);
    }

    @Override
    public void getData(FragmentActivity activity) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(MediaStoreHelper.KEY_IS_SHOW_GIF, mIsShowGif);
        MediaStoreHelper.getAlbums(activity, bundle, new MediaStoreHelper.AlbumsResultCallback() {
            @Override
            public void onResultCallback(List<Album> albums) {
                if (albums != null && !albums.isEmpty()) {
                    mAlbumList = albums;
                    mSelectedAlbum = albums.get(0);
                    mSelectedAlbum.isSelected = true;
                    mView.showAlbumList(albums, mSelectedAlbum.name);
                    mView.showPictureList(albums.get(0).pictures);
                } else {
                    mView.showEmpty();
                }
            }
        });
    }

    @Override
    public void changeAlbum(int position) {
        mSelectedAlbum.isSelected = false;
        mSelectedAlbum = mAlbumList.get(position);
        mSelectedAlbum.isSelected = true;
        mView.showPictureList(mSelectedAlbum.pictures);
        mView.showAlbumList(mAlbumList, mSelectedAlbum.name);
    }

    @Override
    public void selectPicture(boolean isSelect, int position) {
        Photo picture = mSelectedAlbum.pictures.get(position);
        if (mIsSingle) {
            Intent intent = new Intent();
            intent.putExtra(Picker.EXTRA_SINGLE_URI, Uri.fromFile(new File(picture.path)));
            mView.pickFinish(intent);
        } else {
            picture.isSelected = isSelect;
            if (isSelect) {
                if (mSelectedPathList.size() == mMax) {
                    picture.isSelected = false;
                    mView.showMaxSelectTip(mMax, position);
                } else {
                    if (!mSelectedPathList.contains(picture.path)) {
                        mSelectedPathList.add(picture.path);
                    }
                }
            } else {
                if (mSelectedPathList.contains(picture.path)) {
                    mSelectedPathList.remove(picture.path);
                }
            }
        }

        mView.setPreviewCount(mSelectedPathList.size(), mMax);
    }

    @Override
    public ArrayList<String> getSelectedPathList() {
        return mSelectedPathList;
    }

    @Override
    public ArrayList<String> getAllPictureList() {
        ArrayList<String> allPictureList = new ArrayList<>();
        for (Photo picture : mSelectedAlbum.pictures) {
            allPictureList.add(picture.path);
        }
        return allPictureList;
    }

    @Override
    public boolean isSingle() {
        return mIsSingle;
    }
}
