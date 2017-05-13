package com.aiton.administrator.shane_library.shane.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.aiton.administrator.shane_library.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class UILUtils {
    private static DisplayImageOptions options;
    private static DisplayImageOptions optionsIsNoCache;
    private static DisplayImageOptions optionsCorner;
    private static ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 3000);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
    public static void displayImageNoAnim(String imageUrls, ImageView mImageView) {
        initOptions();
        ImageLoader.getInstance().displayImage(imageUrls, mImageView, options,
                null);

    }

    public static void displayImageNoAnimNoCache(String imageUrls, ImageView mImageView, boolean isCache) {
        initOptionsNoCache(isCache);
        ImageLoader.getInstance().displayImage(imageUrls, mImageView, optionsIsNoCache,
                null);
    }

    public static void displayImage(String imageUrls, ImageView mImageView) {
        initOptions();
        ImageLoader.getInstance().displayImage(imageUrls, mImageView, options,
                animateFirstListener);
    }

    public static void displayImageWithRounder(String imageUrls,
                                               ImageView mImageView, int cornerRadiusPixels) {
        initOptions(cornerRadiusPixels);
        ImageLoader.getInstance().displayImage(imageUrls, mImageView, optionsCorner,
                animateFirstListener);
    }

    private static void initOptions() {
        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_stub)
                    .showImageForEmptyUri(R.mipmap.ic_empty)
                    .showImageOnFail(R.mipmap.ic_error).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true)
                    .build();
        }
    }

    private static void initOptionsNoCache(boolean isCache) {
        optionsIsNoCache = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_stub)
                .showImageForEmptyUri(R.mipmap.ic_empty)
                .showImageOnFail(R.mipmap.ic_error).cacheInMemory(isCache)
                .cacheOnDisk(isCache).considerExifParams(isCache)
                .build();
    }

    private static void initOptions(int cornerRadiusPixels) {
        if (optionsCorner == null) {
            optionsCorner = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_stub)
                    .showImageForEmptyUri(R.mipmap.ic_empty)
                    .showImageOnFail(R.mipmap.ic_error).cacheInMemory(true)
                    .cacheOnDisk(true).considerExifParams(true)
                    .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels))
                    .build();
        }
    }

}
