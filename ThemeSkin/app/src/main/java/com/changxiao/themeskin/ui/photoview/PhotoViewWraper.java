package com.changxiao.themeskin.ui.photoview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.changxiao.themeskin.R;

public class PhotoViewWraper extends RelativeLayout {

	protected View loadingDialog;

	protected PhotoView photoView;

	protected Context mContext;
	
	public PhotoViewWraper(Context ctx) {
		super(ctx);
		mContext = ctx;
		init();
	}

	public PhotoViewWraper(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		mContext = ctx;
		init();
	}

	public PhotoView getImageView() {
		return photoView;
	}

	protected void init() {
		photoView = new PhotoView(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		photoView.setLayoutParams(params);
		this.addView(photoView);
		photoView.setVisibility(GONE);

		loadingDialog = LayoutInflater.from(mContext).inflate(R.layout.photo_view_zoom_progress, null);
		loadingDialog.setLayoutParams(params);
		this.addView(loadingDialog);
	}

	public void setUrl(String imageUrl) {
		Glide.with(mContext).load(imageUrl)
				.placeholder(R.drawable.default_image)
				.into(new SimpleTarget<GlideDrawable>() {
					@Override
					public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
						loadingDialog.setVisibility(View.GONE);
						photoView.setVisibility(VISIBLE);
					}

					@Override
					public void onLoadFailed(Exception e, Drawable errorDrawable) {
						photoView.setBackgroundResource(R.drawable.default_image);
					}
				});
	}
}