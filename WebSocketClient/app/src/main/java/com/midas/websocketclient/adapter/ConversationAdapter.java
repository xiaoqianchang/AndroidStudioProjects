package com.midas.websocketclient.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.changxiao.commonadapter.abslistview.CommonAdapter;
import com.midas.websocketclient.R;
import com.midas.websocketclient.bean.ZRConversation;
import com.midas.websocketclient.utils.DateUtil;
import com.midas.websocketclient.utils.ZRContactUtils;
import com.midas.websocketclient.widget.ZRListView;
import com.midas.websocketclient.widget.ZRSwipeLayout;

/**
 * 显示所有聊天记录adpater
 * 
 */
public class ConversationAdapter extends CommonAdapter<ZRConversation> {

	private static final String TAG = "ChatAllHistoryAdapter";

	public ConversationAdapter(Context context, int layoutId, List<ZRConversation> datas) {
		super(context, layoutId, datas);
	}

	@Override
	protected void convert(final com.changxiao.commonadapter.ViewHolder holder, final ZRConversation recentConversation) {
		ZRSwipeLayout swipeLayout = (ZRSwipeLayout) holder.getConvertView();
		// 必须设置此句，否则不知道点击的是哪个item中的menu
		swipeLayout.pos = holder.getmPosition();
		holder.getConvertView().findViewById(R.id.menu_delete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDatas.remove(holder.getmPosition());
				notifyDataSetChanged();
				((ZRListView) holder.getConvertView().getParent()).hideAllMenuView();
//				if (mOnDeleteClickListener != null) {
//					mOnDeleteClickListener.onDeleteClick(v, holder.getAdapterPosition());
//				}
			}
		});

		// 绑定数据
		ZRContactUtils.setContactAvatar(mContext, recentConversation.getUserHeadImg(), (ImageView) holder.getView(R.id.avatar));
		holder.setText(R.id.nick_name, recentConversation.getUserName());
		holder.setText(R.id.unread_msg_number, String.valueOf((int) (Math.random() * 999)));
		holder.setText(R.id.message, recentConversation.getContent());
//		holder.setText(R.id.time, recentConversation.getMessageTime());
		holder.setText(R.id.time, DateUtil.getTimeDiffDesc(new Date()));
	}

	/**
	 * 删除
	 */
	public OnDeleteClickListener mOnDeleteClickListener;
	public void setOnDeleteClickListener(OnDeleteClickListener listener) {
		this.mOnDeleteClickListener = listener;
	}
	public interface OnDeleteClickListener {

		/**
		 * Called when a delete has been clicked.
		 * @param view
		 */
		public void onDeleteClick(View view, int position);
	}

}
