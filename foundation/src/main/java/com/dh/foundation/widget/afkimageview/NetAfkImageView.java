package com.dh.foundation.widget.afkimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.dh.foundation.utils.ImageNetLoader;

/**
 * 带网络加载功能AfkImageView
 * Created By: Seal.Wu
 * Date: 2015/10/9
 * Time: 10:19
 */
public class NetAfkImageView extends AfkImageView {

    public NetAfkImageView(Context context) {
        super(context);
    }

    public NetAfkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setImageUrl(String url) {
        ImageNetLoader.getBitmap(url, new ImageNetLoader.BitmapReceiver() {
            @Override
            public void onReceiveBitmap(Bitmap bitmap, boolean isImmediate) {

                setTransitionAnimationEnable(!isImmediate);

                setImageBitmap(bitmap);
            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }


    public void setImageUrl(String url, final int errorImageResId, final int defaultImageResId) {

        setTransitionAnimationEnable(false);

        setImageResource(defaultImageResId);

        ImageNetLoader.getBitmap(url, new ImageNetLoader.BitmapReceiver() {
            @Override
            public void onReceiveBitmap(Bitmap bitmap, boolean isImmediate) {

                setTransitionAnimationEnable(!isImmediate);

                setImageBitmap(bitmap);
            }

            @Override
            public void onError(Throwable error) {

                setTransitionAnimationEnable(false);

                setImageResource(errorImageResId);
            }
        });
    }

}
