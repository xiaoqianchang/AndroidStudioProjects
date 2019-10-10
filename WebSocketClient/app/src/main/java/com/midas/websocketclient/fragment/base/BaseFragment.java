package com.midas.websocketclient.fragment.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.midas.websocketclient.R;

/**
 * Fragment的基类
 * 
 * @author Chang Xiao
 * @date 2015年12月7日 下午5:35:57
 * @version 1.0
 */
public abstract class BaseFragment extends Fragment {

	protected Context mContext;
	private ProgressDialog progressDialog;
	private View titleView;

	/*
	 * 以下是Fragment的生命周期中(部分方法执行顺序) (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#setMenuVisibility(boolean)
	 */
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return createView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	/**
	 * 注入title视图
	 * 
	 * @param view
	 */
	protected void setTitleView(View view) {
		this.titleView = view;
	}
	
	/**
     * 得到标题view
     * 
     * @return
     */
    protected View getTitleView() {
    	if (titleView == null) {
			Toast.makeText(mContext, "请注入Title视图！", Toast.LENGTH_SHORT).show();
			return LayoutInflater.from(mContext).inflate(R.layout.fragment_header, null);
		} else {
			return titleView.findViewById(R.id.fragment_title_view);
		}
	}
    
    /**
	 * 默认的右边图片点击事件调用方法(重设背景图片和动作事件)
	 * 
	 * @param bckgImage
	 * @param listener
	 */
	protected void setTitleBarRightImageAndListener(int bckgImage,
			OnClickListener listener) {
		ImageView rightImage = (ImageView) getTitleView().findViewById(R.id.fragment_title_right_image);
		rightImage.setVisibility(View.VISIBLE);
		rightImage.setBackgroundResource(bckgImage);
		rightImage.setOnClickListener(listener);
	}
	
	/**
	 * 设置右边图片隐藏
	 */
	protected void setTitleBarRightImageGone() {
		ImageView rightImage = (ImageView) getTitleView().findViewById(R.id.fragment_title_right_image);
		rightImage.setVisibility(View.GONE);
	}
	
	protected void setTitleBarRightImage(int bckgImage) {
		ImageView rightImage = (ImageView) getTitleView().findViewById(R.id.fragment_title_right_image);
		rightImage.setBackgroundResource(bckgImage);
	}

	/**
	 * 设置右边图片显示
	 */
	protected void setTitleBarRightImageVisible() {
		ImageView rightImage = (ImageView) getTitleView().findViewById(R.id.fragment_title_right_image);
		rightImage.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 得到右边按钮的视图
	 */
	protected ImageView getTitleBarRightView() {
		ImageView rightImage = (ImageView) getTitleView().findViewById(R.id.fragment_title_right_image);
		return rightImage;
	}
    
    /**
	 * 设置标题内容
	 * 
	 * @param titleStr
	 */
	protected void setTitleBarTitle(String titleStr) {
		((TextView) (getTitleView().findViewById(R.id.fragment_title_text))).setText(titleStr);
	}
	
	/**
	 * 设置标题内容
	 * 
	 * @param title
	 */
	protected void setTitleBarTitle(int title) {
		((TextView) (getTitleView().findViewById(R.id.fragment_title_text))).setText(title);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	protected void showLoadingProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(getActivity());
			CharSequence msg = getResources().getString(
					R.string.progress_loading);
			progressDialog.setMessage(msg);
		}
		progressDialog.show();
	}

	protected void closeLoadingProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.cancel();
		}
		progressDialog = null;
	}

	/**
	 * onCreateView
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	public abstract View createView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

}
