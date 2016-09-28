package com.zhl.photopicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Picker {

    public static final int REQUEST_PICKER = 66;

    private static final String EXTRA_PREFIX = BuildConfig.APPLICATION_ID;

    public static final String EXTRA_MAX = EXTRA_PREFIX + ".Max";
    public static final String EXTRA_SELECTED_PHOTOS = EXTRA_PREFIX + ".SelectedPhotos";
    public static final String EXTRA_IS_SINGLE = EXTRA_PREFIX + ".IsSingle";
    public static final String EXTRA_SINGLE_URI = EXTRA_PREFIX + ".SingleUri";
    public static final String EXTRA_IS_SHOW_GIF = EXTRA_PREFIX + ".IsShowGif";
    public static final String EXTRA_WIDGET_COLOR_TOOLBAR = EXTRA_PREFIX + ".WidgetColorToolbar";

    private Intent mPickerIntent;
    private Bundle mPickerOptions;

    private Picker() {
        mPickerIntent = new Intent();
        mPickerOptions = new Bundle();
    }

    public static Picker create() {
        return new Picker();
    }

    public Picker max(@IntRange(from = 1) int max) {
        mPickerOptions.putInt(EXTRA_MAX, max);
        return this;
    }

    public Picker single() {
        mPickerOptions.putBoolean(EXTRA_IS_SINGLE, true);
        return this;
    }

    public Picker showGif() {
        mPickerOptions.putBoolean(EXTRA_IS_SHOW_GIF, true);
        return this;
    }

    public Picker widgetColorToolbar(@ColorInt int color) {
        mPickerOptions.putInt(EXTRA_WIDGET_COLOR_TOOLBAR, color);
        return this;
    }

    public void start(@NonNull Activity activity, int requestCode) {
        start(activity, PhotoPickerActivity.class, requestCode);
    }

    public void start(@NonNull Activity activity) {
        start(activity, REQUEST_PICKER);
    }

    public void start(@NonNull Activity activity, @NonNull Class clazz) {
        start(activity, clazz, REQUEST_PICKER);
    }

    public void start(@NonNull Activity activity, @NonNull Class clazz, int requestCode) {
        activity.startActivityForResult(getIntent(activity, clazz), requestCode);
    }

    public Intent getIntent(@NonNull Context context, Class clazz) {
        mPickerIntent.setClass(context, clazz);
        mPickerIntent.putExtras(mPickerOptions);
        return mPickerIntent;
    }

    public static ArrayList<String> getOutput(@NonNull Intent data) {
        return data.getStringArrayListExtra(EXTRA_SELECTED_PHOTOS);
    }

    public static Uri getSingleOutput(@NonNull Intent data) {
        return data.getParcelableExtra(EXTRA_SINGLE_URI);
    }
}
