package com.character.microblogapp.util;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.character.microblogapp.GlideApp;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author keisoft
 */

public class GlideUtil {
    private static final String TAG = GlideUtil.class.getSimpleName();

    public interface OnException {
        boolean onException(Exception exception, String url, Target<Drawable> target, boolean z);
    }

    public interface OnResourceReady {
        boolean onResourceReady(Drawable bitmap, String url, Target<Drawable> target, DataSource dateSource, boolean isFirstResource);
    }

    private static RequestBuilder createRequestBuilder(Context context, String url, final OnResourceReady onResourceReady, final OnException onException) {
        return createRequestBuilder(context, url, onResourceReady, onException, null);
    }

    private static RequestBuilder createRequestBuilder(Context context, String url, final OnResourceReady onResourceReady, final OnException onException, Drawable placeholder) {
        if (context == null || ((context instanceof Activity) && ((Activity) context).isFinishing())) {
            return null;
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
        if (placeholder != null)
            requestOptions.placeholder(placeholder);

        return GlideApp.with(context).load(url).apply(requestOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Log.e("GlideHelper Error", String.format("%s / %s / %s / %s", e, model, target, isFirstResource));
                return onException != null && onException.onException(e, (String) model, target, isFirstResource);
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                Log.i("GlideHelper Ready", String.format("MemoryCache: %s / FirstResource: %s / URL: %s / %s / %s", resource, model, target, dataSource, isFirstResource));
                return onResourceReady != null && onResourceReady.onResourceReady(resource, (String) model, target, dataSource, isFirstResource);
            }
        });
    }

    public static void loadImage(ImageView view, String url, OnResourceReady onResourceReady, OnException onException) {
        setImageView(createRequestBuilder(view.getContext(), url, onResourceReady, onException), view);
    }

    public static void loadImage(ImageView view, String url) {
        loadImage(view, url, null, null);
    }

    public static void loadRoundImage(ImageView view, String url, int radius) {
        RequestBuilder requestBuilder = createRequestBuilder(view.getContext(), url, null, null, null);
        if (requestBuilder != null) {
            RequestOptions options = new RequestOptions();
            options = options.centerCrop().transform(new RoundedCorners(radius));
            requestBuilder = requestBuilder.apply(options);
            setImageView(requestBuilder, view);
        }
    }

    public static void loadThumbImage(ImageView view, String url) {
        loadImage(view, makeImageUrl(url));
    }

    public static void loadBlurImage(ImageView view, String url, OnResourceReady onResourceReady, OnException onException) {
        RequestBuilder requestBuilder = createRequestBuilder(view.getContext(), url, onResourceReady, onException, null);
        if (requestBuilder != null) {
            requestBuilder = requestBuilder
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation()));
            setImageView(requestBuilder, view);
        }
    }

    public static void loadBlurImage(ImageView view, String url, int radius, int sampling) {
        RequestBuilder requestBuilder = createRequestBuilder(view.getContext(), url, null, null, null);
        if (requestBuilder != null) {
            requestBuilder = requestBuilder
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(radius)));
            setImageView(requestBuilder, view);
        }
    }

    public static void loadCircleImage(ImageView view, String url, OnResourceReady onResourceReady, OnException onException, Drawable placeholder) {
        RequestBuilder requestBuilder = createRequestBuilder(view.getContext(), url, onResourceReady, onException, placeholder);
        if (requestBuilder != null) {
            requestBuilder = requestBuilder
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()));
            setImageView(requestBuilder, view);
        }
    }


    public static void loadCircleImage(ImageView view, String url, Drawable placeholder) {
        loadCircleImage(view, url, null, null, placeholder);
    }

    public static void loadCircleDimmedImage(ImageView view, String url) {
        RequestBuilder requestBuilder = createRequestBuilder(view.getContext(), url, null, null, null);
        if (requestBuilder != null) {
            requestBuilder = requestBuilder
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()));
            setImageView(requestBuilder, view);
        }
    }

    public static void loadCircleCropImage(ImageView view, String url) {
        RequestBuilder requestBuilder = createRequestBuilder(view.getContext(), url, null, null, null);
        if (requestBuilder != null) {
            requestBuilder = requestBuilder
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()));
            setImageView(requestBuilder, view);
        }
    }

    public static void loadCircleThumbImage(ImageView view, String url, Drawable placeholder) {
        loadCircleImage(view, makeImageUrl(url), placeholder);
    }

    private static void setImageView(RequestBuilder RequestBuilder, ImageView view) {
        setImageView(RequestBuilder, view, 0);
    }

    private static void setImageView(RequestBuilder RequestBuilder, ImageView view, int placeholder) {
        if (RequestBuilder != null) {
            RequestBuilder.into(view);
        }
    }

    private static String makeImageUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        String lastPathSegment = Uri.parse(url).getLastPathSegment();
        if (lastPathSegment.contains(".jpg")) {
            return makeThumbUrl(url, ".jpg");
        }
        if (lastPathSegment.contains(".png")) {
            return makeThumbUrl(url, ".png");
        }
        return url;
    }

    private static String makeThumbUrl(String url, String ext) {
        if (url.contains(ext)) {
            return url.substring(0, url.indexOf(ext)) + "_thumb" + url.substring(url.indexOf(ext) + ext.length()) + ext;
        }
        return "";
    }
}
