package com.zhl.photopicker;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhl.commonadapter.BaseViewHolder;

public class AlbumVH extends BaseViewHolder<Album> {

    private ImageView mImgCover;
    private TextView mTvName;
    private TextView mTvCount;
    private ImageView mImgSelected;

    @Override
    public void bindView(View view) {
        super.bindView(view);
        mImgCover = (ImageView) view.findViewById(R.id.img_cover);
        mTvName = (TextView) view.findViewById(R.id.tv_name);
        mTvCount = (TextView) view.findViewById(R.id.tv_count);
        mImgSelected = (ImageView) view.findViewById(R.id.img_selected);
    }

    @Override
    public void updateView(Album data, int position) {
        if (data != null) {
            Glide.with(mContext).load(data.coverPath).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImgCover);
            mImgSelected.setVisibility(data.isSelected ? View.VISIBLE : View.GONE);
            mTvName.setText(data.name);
            mTvCount.setText(mContext.getString(R.string.picker_count, data.pictures.size()));
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_picture_album;
    }
}
