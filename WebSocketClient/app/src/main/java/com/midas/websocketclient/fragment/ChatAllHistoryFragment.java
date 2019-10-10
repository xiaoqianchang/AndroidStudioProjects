package com.midas.websocketclient.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.midas.websocketclient.R;
import com.midas.websocketclient.adapter.ConversationAdapter;
import com.midas.websocketclient.bean.ZRUser;
import com.midas.websocketclient.bean.ZRConversation;
import com.midas.websocketclient.fragment.base.BaseFragment;

/**
 * 显示所有会话记录，比较简单的实现，更好的可能是把陌生人存入本地，这样取到的聊天记录是可控的
 * 
 */
public class ChatAllHistoryFragment extends BaseFragment {

	private ListView mConversation;
	private ConversationAdapter mAdapter;
	private List<ZRConversation> conversationList = new ArrayList<ZRConversation>();

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (mAdapter != null) {
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_conversation_history, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mConversation = (ListView) getView().findViewById(R.id.conversion_listview);
		conversationList = getconversation();
		mAdapter = new ConversationAdapter(mContext, R.layout.row_chat_history, conversationList);
		// 设置adapter
		mConversation.setAdapter(mAdapter);

	}

	/**
	 * 根据最后一条消息的时间排序
	 */
	private void sortConversationByLastChatTime(List<Pair<Long, ZRUser>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, ZRUser>>() {
			@Override
			public int compare(final Pair<Long, ZRUser> con1, final Pair<Long, ZRUser> con2) {

				if (con1.first == con2.first) {
					return 0;
				} else if (con2.first > con1.first) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	public List<ZRConversation> getconversation() {
		List<ZRConversation> contactlist = new ArrayList<ZRConversation>();
		for (int i = 0; i < 10; i++) {
			ZRUser contact = new ZRUser();
			contact.set_id(String.valueOf(i));
			contact.setName("xc" + i);
			contact.setNickName("xc" + i);
			contact.setAvatar("");
			contact.setSignature("我是一个好人！");
		}
		return contactlist;
	}
}
