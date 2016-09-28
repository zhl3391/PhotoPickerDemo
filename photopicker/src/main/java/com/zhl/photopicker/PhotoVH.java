package com.zhl.photopicker;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhl.commonadapter.BaseViewHolder;

public class PhotoVH extends BaseViewHolder<Photo> {

    ImageView mImgPhoto;
    CheckBox mCheckBox;

    private boolean mIsSingle = false;
    private OnPictureSelectListener mOnPictureSelectListener;

    @Override
    public void bindView(View view) {
        super.bindView(view);
        mImgPhoto = (ImageView) view.findViewById(R.id.img_photo);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkBox);
    }

    public PhotoVH(@NonNull OnPictureSelectListener onPictureSelectListener, boolean isSingle) {
        mOnPictureSelectListener = onPictureSelectListener;
        mIsSingle = isSingle;
    }

    @Override
    public void updateView(final Photo data, final int position) {
        if (data != null) {
            setMark(mImgPhoto, data.isSelected);
            Glide.with(mContext).load(data.path).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mImgPhoto);
            mCheckBox.setChecked(data.isSelected);

            if (mIsSingle) {
                mCheckBox.setVisibility(View.GONE);
            } else {
                mCheckBox.setVisibility(View.VISIBLE);
                mCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCheckBox.setSelected(!data.isSelected);
                        setMark(mImgPhoto, !data.isSelected);
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

    private void setMark(ImageView imageView, boolean isSelected) {
        if (imageView != null) {
            imageView.clearColorFilter();
            if (isSelected) {
                imageView.setColorFilter(new PorterDuffColorFilter(
                        ContextCompat.getColor(mContext, R.color.picker_mark_selected),
                        PorterDuff.Mode.SRC_OVER));
            } else {
                imageView.setColorFilter(new PorterDuffColorFilter(
                        ContextCompat.getColor(mContext, R.color.picker_mark),
                        PorterDuff.Mode.SRC_OVER));
            }
        }
    }

    public interface OnPictureSelectListener {
        void onSelect(boolean isSelect, int position);
    }
}
