package com.zhl.photopicker;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhl.commonadapter.BaseViewHolder;

public class PhotoVH extends BaseViewHolder<Photo> {

    ImageView mImgPhoto;
    View mViewMark;
    CheckBox mCheckBox;

    private boolean mIsSingle = false;
    private OnPictureSelectListener mOnPictureSelectListener;

    @Override
    public void bindView(View view) {
        super.bindView(view);
        mImgPhoto = (ImageView) view.findViewById(R.id.img_photo);
        mViewMark = view.findViewById(R.id.view_mark);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkBox);
    }

    public PhotoVH(@NonNull OnPictureSelectListener onPictureSelectListener, boolean isSingle) {
        mOnPictureSelectListener = onPictureSelectListener;
        mIsSingle = isSingle;
    }

    @Override
    public void updateView(final Photo data, final int position) {
        if (data != null) {
            Glide.with(mContext).load(data.path).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImgPhoto);
            mCheckBox.setChecked(data.isSelected);
            mViewMark.setAlpha(data.isSelected ? 0.6f : 0.2f);

            if (mIsSingle) {
                mCheckBox.setVisibility(View.GONE);
            } else {
                mCheckBox.setVisibility(View.VISIBLE);
                mCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCheckBox.setSelected(!data.isSelected);
                        mViewMark.setAlpha(!data.isSelected ? 0.6f : 0.2f);
                        mOnPictureSelectListener.onSelect(!data.isSelected, position);
                    }
                });
            }
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_picture;
    }

    public interface OnPictureSelectListener {
        void onSelect(boolean isSelect, int position);
    }
}
