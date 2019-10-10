package com.midas.websocketclient.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.changxiao.commonadapter.abslistview.MultiItemCommonAdapter;
import com.changxiao.commonadapter.abslistview.MultiItemTypeSupport;
import com.midas.websocketclient.R;
import com.midas.websocketclient.bean.ZRMessage;
import com.midas.websocketclient.utils.DateUtil;
import com.midas.websocketclient.utils.ZRContactUtils;

import java.util.Date;
import java.util.List;

public class ChatMessageAdapter extends MultiItemCommonAdapter<ZRMessage> {

	public ChatMessageAdapter(Context context, List<ZRMessage> datas, MultiItemTypeSupport multiItemTypeSupport) {
		super(context, datas, multiItemTypeSupport);
	}

	@Override
	protected void convert(com.changxiao.commonadapter.ViewHolder holder, ZRMessage message) {
//		holder.setText(R.id.tv_sendtime, DateUtil.getTimeDiffDesc(new Date()));
		holder.setText(R.id.tv_sendtime, DateUtil.getSessionTime(new Date().getTime()));
		holder.setText(R.id.tv_username, message.getFrom().getNickName());
		holder.setText(R.id.tv_chatcontent, message.getContent());
		ZRContactUtils.setContactAvatar(mContext, message.getFrom().getAvatar(), (ImageView) holder.getView(R.id.iv_userhead));
	}
}
