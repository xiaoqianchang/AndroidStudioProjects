package com.changxiao.themeskin.ui.photoview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class PhotoViewAdapter extends PagerAdapter {

	protected final Context mContext;
	private List<String> imgUrls;
	protected OnItemChangeListener mOnItemChangeListener;

	public PhotoViewAdapter(Context mContext, List<String> imgUrls) {
		this.mContext = mContext;
		this.imgUrls = imgUrls;
	}

	@Override
	public int getCount() {
		return imgUrls.size();
	}
	
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
		if (mOnItemChangeListener != null)
			mOnItemChangeListener.onItemChange(position);
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		
//		final PhotoView photoView = new PhotoView(mContext);
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		photoView.setLayoutParams(params);
//		mImageFetcher.loadImage(imgUrls.get(position), photoView);
//		container.addView(photoView, 0);
//		return photoView;
		
		final PhotoViewWraper iv = new PhotoViewWraper(mContext);
		iv.setUrl(imgUrls.get(position));
		iv.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		container.addView(iv, 0);
		return iv;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	public void setOnItemChangeListener(OnItemChangeListener listener) {
		mOnItemChangeListener = listener;
	}
	
	public static interface OnItemChangeListener {
		public void onItemChange(int currentPosition);
	}
}
