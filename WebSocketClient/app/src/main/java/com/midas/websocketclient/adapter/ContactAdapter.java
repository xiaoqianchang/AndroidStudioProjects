package com.midas.websocketclient.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;

import com.changxiao.commonadapter.abslistview.CommonAdapter;
import com.midas.websocketclient.R;
import com.midas.websocketclient.bean.ZRUser;
import com.midas.websocketclient.utils.ZRContactUtils;

/**
 * 简单的好友Adapter实现
 *
 */
public class ContactAdapter extends CommonAdapter<ZRUser> {

    private static final String TAG = "ContactAdapter";

	public ContactAdapter(Context context, int resource, List<ZRUser> datas) {
		super(context, resource, datas);
	}

	@Override
	protected void convert(com.changxiao.commonadapter.ViewHolder holder, ZRUser contact) {
		// 绑定数据
		ZRContactUtils.setContactAvatar(mContext, contact.getAvatar(), (ImageView) holder.getView(R.id.avatar));
		holder.setText(R.id.nick_name, contact.getNickName());
		holder.setText(R.id.signature, contact.getSignature());
	}
	
}
