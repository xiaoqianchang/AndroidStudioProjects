package cn.feng.skin.manager.entity;

import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.feng.skin.manager.util.ListUtils;

public class SkinItem {

	public WeakReference<View> view;

	public List<SkinAttr> attrs;

	public SkinItem(){
		attrs = new ArrayList<SkinAttr>();
	}

	public void apply(){
		if(ListUtils.isEmpty(attrs) || view == null){
			return;
		}
		View trueView = view.get();
		if (trueView != null) {
			for(SkinAttr at : attrs){
				at.apply(trueView);
			}
		}
	}

	public void clean(){
		if(ListUtils.isEmpty(attrs)){
			return;
		}
		if (view != null) {
			view.clear();
		}
		for(SkinAttr at : attrs){
			at = null;
		}
	}

	@Override
	public String toString() {
		return "SkinItem [view=" + view.getClass().getSimpleName() + ", attrs=" + attrs + "]";
	}
}
