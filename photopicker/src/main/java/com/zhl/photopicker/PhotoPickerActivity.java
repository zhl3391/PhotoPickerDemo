package com.zhl.photopicker;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhl.commonadapter.BaseViewHolder;
import com.zhl.commonadapter.CommonAdapter;

import java.io.File;
import java.util.List;

public class PhotoPickerActivity extends AppCompatActivity implements PhotoPickerContract.View,
        PhotoVH.OnPictureSelectListener, View.OnClickListener{

    private static final int CODE_FOR_WRITE_PERMISSION = 10;

    TextView mTvTitle;
    TextView mTvOk;
    GridView mGvPhotos;
    TextView mTvAlbum;
    TextView mTvPreview;
    View mViewMark;
    ListView mLvAlbum;
    View mLayoutBottom;
    View mLayoutAlbum;

    private CommonAdapter<Album> mAlbumAdapter;
    private CommonAdapter<Photo> mPhotoAdapter;

    private PhotoPickerContract.Presenter mPresenter;

    private boolean mIsOpenAlbum;
    private int mLvAlbumHeight;

    private int mWidgetColorToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_picker);

        Bundle extras = getIntent().getExtras();

        mWidgetColorToolbar = extras.getInt(Picker.EXTRA_WIDGET_COLOR_TOOLBAR, Color.WHITE);

        mPresenter = new PhotoPickerPresenter(this, extras);

        initView();
        initPhotoAlbum();

        if (mPresenter.isSingle()) {
            mTvPreview.setVisibility(View.GONE);
            mTvOk.setVisibility(View.GONE);
        }

        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            mPresenter.getData(this);
        } else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_FOR_WRITE_PERMISSION);
        }

    }

    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvOk = (TextView) findViewById(R.id.tv_ok);
        mGvPhotos = (GridView) findViewById(R.id.gv_pictures);
        mTvAlbum = (TextView) findViewById(R.id.tv_album);
        mTvPreview = (TextView) findViewById(R.id.tv_preview);
        mViewMark = findViewById(R.id.view_mark);
        mLvAlbum = (ListView) findViewById(R.id.lv_album);
        mLayoutBottom = findViewById(R.id.layout_bottom);
        mLayoutAlbum = findViewById(R.id.layout_album);

        mTvTitle.setText(R.string.picker_all_picture);
        mTvOk.setText(R.string.picker_send);
        mTvOk.setEnabled(false);
        mTvPreview.setVisibility(View.GONE);

        mTvTitle.setTextColor(mWidgetColorToolbar);
        mTvOk.setTextColor(mWidgetColorToolbar);

        mLayoutBottom.setOnClickListener(this);
        mTvOk.setOnClickListener(this);
        mLayoutAlbum.setOnClickListener(this);
        mTvPreview.setOnClickListener(this);
    }

    private void initPhotoAlbum() {
        mAlbumAdapter = new CommonAdapter<Album>() {
            @Override
            public BaseViewHolder<Album> createViewHolder(int type) {
                return new AlbumVH();
            }
        };
        mPhotoAdapter = new CommonAdapter<Photo>() {
            @Override
            public BaseViewHolder<Photo> createViewHolder(int type) {
                return new PhotoVH(PhotoPickerActivity.this, mPresenter.isSingle());
            }
        };

        mLvAlbum.setAdapter(mAlbumAdapter);
        mGvPhotos.setAdapter(mPhotoAdapter);

        mLvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.changeAlbum(position);
                showAlbumView(false);
            }
        });
        mGvPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPresenter.isSingle()) {
                    Intent intent = new Intent();
                    intent.putExtra(Picker.EXTRA_SINGLE_URI, Uri.fromFile(new File(mPhotoAdapter.getItem(position).path)));
                    pickFinish(intent);
                } else {
                    Intent it =new Intent(Intent.ACTION_VIEW);
                    Uri mUri = Uri.parse("file://" + mPhotoAdapter.getItem(position).path);
                    it.setDataAndType(mUri, "image/*");
                    startActivity(it);
                }
            }
        });
    }

    @Override
    public void showPictureList(List<Photo> photoList) {
        mPhotoAdapter.setDatas(photoList);
    }

    @Override
    public void showAlbumList(List<Album> albumList, String albumName) {
        mAlbumAdapter.setDatas(albumList);
        mTvAlbum.setText(albumName);
        mTvTitle.setText(albumName);
    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showAlbumView(boolean isShow) {
        if (isShow) {
            final ViewTreeObserver vto = mLvAlbum.getViewTreeObserver();
            if (mLvAlbumHeight == 0) {
                mLvAlbum.setVisibility(View.VISIBLE);
                mLvAlbum.setAlpha(0);
                vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        vto.removeOnPreDrawListener(this);
                        mLvAlbumHeight = mLvAlbum.getMeasuredHeight();
                        mLvAlbum.setY(mLayoutBottom.getTop());
                        mLvAlbum.animate().yBy(-mLvAlbumHeight).alpha(1)
                                .setInterpolator(new DecelerateInterpolator()).setDuration(300).setListener(null).start();
                        return true;
                    }
                });
            } else {
                mLvAlbum.setVisibility(View.VISIBLE);
                mLvAlbum.setAlpha(0);
                mLvAlbum.setY(mLayoutBottom.getTop());
                mLvAlbum.animate().yBy(-mLvAlbumHeight).alpha(1)
                        .setInterpolator(new DecelerateInterpolator()).setDuration(300).setListener(null).start();
            }
            mViewMark.setVisibility(View.VISIBLE);
            mViewMark.animate().alpha(0.8f).setDuration(300).start();
            mIsOpenAlbum = true;
        } else {
            mLvAlbum.animate().yBy(mLvAlbumHeight).alpha(0)
                    .setInterpolator(new DecelerateInterpolator()).setDuration(300).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mLvAlbum.setVisibility(View.GONE);
                    mViewMark.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).start();
            mViewMark.animate().alpha(0).setDuration(300).start();

            mIsOpenAlbum = false;
        }
    }

    @Override
    public void showMaxSelectTip(int max, int position) {
        Toast.makeText(this, getString(R.string.picker_select_max, max), Toast.LENGTH_SHORT).show();
        mPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPreviewCount(int count, int max) {
        if (count > 0) {
            mTvPreview.setText(getString(R.string.picker_preview_count, count));
            mTvPreview.setEnabled(true);
            mTvOk.setText(getString(R.string.picker_send_count, count, max));
            mTvOk.setEnabled(true);
        } else {
            mTvPreview.setText(R.string.picker_preview);
            mTvOk.setText(R.string.picker_send);
            mTvOk.setEnabled(false);
            mTvPreview.setEnabled(false);
        }
    }

    @Override
    public void onSelect(boolean isSelect, int position) {
        mPresenter.selectPicture(isSelect, position);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.layout_album) {
            showAlbumView(!mIsOpenAlbum);
        } else if (i == R.id.tv_ok) {
            Intent intent = new Intent();
            intent.putExtra(Picker.EXTRA_SELECTED_PHOTOS, mPresenter.getSelectedPathList());
            pickFinish(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_FOR_WRITE_PERMISSION){
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mPresenter.getData(this);
            }else{
                finish();
            }
        }
    }

    private void pickFinish(Intent intent) {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
