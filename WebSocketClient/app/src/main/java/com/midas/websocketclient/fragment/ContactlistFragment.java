package com.midas.websocketclient.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.midas.websocketclient.R;
import com.midas.websocketclient.activity.ChatActivity;
import com.midas.websocketclient.adapter.ContactAdapter;
import com.midas.websocketclient.bean.ZRUser;
import com.midas.websocketclient.fragment.base.BaseFragment;

/**
 * 联系人列表页
 * 
 */
public class ContactlistFragment extends BaseFragment {

	public static final String TAG = "ContactlistFragment";

	private ContactAdapter mAdapter;
	private List<ZRUser> contactList;
	private ListView listView;

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
		return inflater.inflate(R.layout.fragment_contact_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listView = (ListView) getView().findViewById(R.id.list);
		contactList = getContactList();
		// 设置adapter
		mAdapter = new ContactAdapter(getActivity(), R.layout.row_contact, contactList);
		listView.setAdapter(mAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("user", new ZRUser(contactList.get(position).get_id())));
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 获取联系人列表，并过滤掉黑名单和排序
	 */
	private List<ZRUser> getContactList() {
//		contactList.clear();
//		//获取本地好友列表
//		contactList = ZRHelper.getInstance().getContactList();
		List<ZRUser> contactList = new ArrayList<ZRUser>();
		for (int i = 0; i < 10; i++) {
			ZRUser contact = new ZRUser();
			contact.set_id(String.valueOf(i));
			contact.setNickName("xc" + i);
			contact.setAvatar("http://ww1.sinaimg.cn/crop.7.22.1192.1192.1024/5c6defebjw8epti0r9noaj20xc0y1n0x.jpg");
			contactList.add(contact);
		}
		return contactList;
	}
	
}
