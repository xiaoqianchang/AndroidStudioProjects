package com.midas.websocketclient.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.changxiao.commonadapter.abslistview.MultiItemTypeSupport;
import com.midas.websocketclient.R;
import com.midas.websocketclient.adapter.ChatMessageAdapter;
import com.midas.websocketclient.bean.ZRConversation;
import com.midas.websocketclient.bean.ZRMessage;
import com.midas.websocketclient.bean.ZRUser;
import com.midas.websocketclient.db.dao.ZRMessageDao;

public class ChatActivity extends AppCompatActivity implements OnClickListener{

    private static final int ITEM_TYPE_COUNT = 2;//消息类型总数

    private Button mBtnSend;// 发送btn
    private EditText mEditTextContent;
    private ListView mListView;

    private ChatMessageAdapter mAdapter;
    private List<ZRMessage> mMessages= new ArrayList<ZRMessage>();// 消息对象数组
    private ZRUser mContact;
    private ZRMessageDao messageDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageDao = new ZRMessageDao(this);
        mContact = new ZRUser(((ZRConversation) getIntent().getSerializableExtra("contact")).getUserid());
        initView();// 初始化view
        loadDataFromDB();// 初始化数据


    }
    /**
     * 初始化View
     */
    public void initView(){
        mListView=(ListView)findViewById(R.id.listView);
        mBtnSend=(Button)findViewById(R.id.chat_send);
        mBtnSend.setOnClickListener(this);
        mEditTextContent=(EditText)findViewById(R.id.chat_editmessage);
    }

    /**
     * 加载历史消息，从数据库中读取
     */
    public void loadDataFromDB(){
        /*mMessages=messageDao.getMsgByContactId(mContact.get_id());
        *//*if(mMessages.size()>0){
            for(ZRMessage message : mMessages){
                mMessages.add(message);
            }
            Collections.reverse(mMessages);
        }*//*
        mAdapter=new ChatMessageAdapter(this, mMessages, multiItemTypeSupport);
        mListView.setAdapter(mAdapter);
        mListView.setSelection(mAdapter.getCount()-1);*/

        for (int i = 0; i < 10; i++) {
            ZRMessage message = new ZRMessage();
            message.set_id(String.valueOf(i));
            message.setDate("12:30");
            message.setContent("dddddddddddddddd");
            if (i % 2 == 0) {
                message.setFrom(new ZRUser("小明", "http://ww1.sinaimg.cn/crop.7.22.1192.1192.1024/5c6defebjw8epti0r9noaj20xc0y1n0x.jpg"));
                message.setDirect(ZRMessage.Direct.RECEIVE);
            } else {
                message.setFrom(new ZRUser("小华", "http://ww1.sinaimg.cn/crop.0.0.800.800.1024/735510dbjw8eoo1nn6h22j20m80m8t9t.jpg"));
                message.setDirect(ZRMessage.Direct.SEND);
            }
            mMessages.add(message);
        }
        mAdapter=new ChatMessageAdapter(this, mMessages, multiItemTypeSupport);
        mListView.setAdapter(mAdapter);
        mListView.setSelection(mAdapter.getCount()-1);

    }

    /**
     * 对个item类型
     */
    MultiItemTypeSupport multiItemTypeSupport = new MultiItemTypeSupport() {
        @Override
        public int getLayoutId(int position, Object o) {
            ZRMessage message = (ZRMessage) o;
            if (message.getDirect() == ZRMessage.Direct.RECEIVE) {
                return R.layout.chatting_item_left;
            } else {
                return R.layout.chatting_item_right;
            }
        }

        @Override
        public int getViewTypeCount() {
            return ITEM_TYPE_COUNT;
        }

        @Override
        public int getItemViewType(int position, Object o) {
            ZRMessage message = (ZRMessage) o;
            if(message.getDirect() == ZRMessage.Direct.RECEIVE){//收到的消息
                return ZRMessage.Direct.RECEIVE.getValue();
            }else{//自己发送的消息
                return ZRMessage.Direct.SEND.getValue();
            }
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.chat_send:
                send();
                break;
        }
    }

    /**
     * 发送消息
     */
    public void send(){
        String contString = mEditTextContent.getText().toString();
        if(contString.length()>0){
            ZRMessage message = createMsg();

            //保存到数据库
//            messageDao.saveMsg(mContact.get_id(), message);
            //添加到List，更新ListView
            mMessages.add(message);
            mAdapter.notifyDataSetChanged();
            // 清空编辑栏
            mEditTextContent.setText("");
            //定位List。发送一条，将ListView定位到最后一条
            mListView.setSelection(mListView.getCount()-1);
            //发送到数据流

        }
    }

    /**
     * create msg
     * @return
     */
    private ZRMessage createMsg() {
        ZRMessage message = new ZRMessage();
        message.set_id("");
        message.setDate("11:12");
        message.setContent("dddddddddddddddd");
        message.setFrom(new ZRUser("小华", "http://ww1.sinaimg.cn/crop.0.0.800.800.1024/735510dbjw8eoo1nn6h22j20m80m8t9t.jpg"));
        message.setDirect(ZRMessage.Direct.SEND);
        return message;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        // 退出时更新列表
        /*if(mDataArrays.size()>0){
            RecentChatEntity entity1 = new RecentChatEntity(user.getId(),
                    user.getImg(), 0, user.getName(), MyDate.getDate(),
                    mDataArrays.get(mDataArrays.size()-1).getMessage());
            application.getmRecentList().remove(entity1);
            application.getmRecentList().addFirst(entity1);
        }
        application.getmRecentAdapter().notifyDataSetChanged();
        messageDB.close();*/
        super.onDestroy();
    }
    /*@Override
    public void getMessage(TranObject msg) {
        // TODO Auto-generated method stub
        switch (msg.getType()) {
            case MESSAGE:
                TextMessage tm = (TextMessage) msg.getObject();
                String message = tm.getMessage();
                ZRMessage entity = new ZRMessage(user.getName(),
                        MyDate.getDateEN(), message, user.getImg(), true);// 收到的消息
                if (msg.getFromUser() == user.getId() || msg.getFromUser() == 0) {// 如果是正在聊天的好友的消息，或者是服务器的消息

                    messageDB.saveMsg(user.getId(), entity);

                    mDataArrays.add(entity);
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(mListView.getCount() - 1);
                    MediaPlayer.create(this, R.raw.msg).start();
                } else {
                    messageDB.saveMsg(msg.getFromUser(), entity);// 保存到数据库
                    Toast.makeText(ChatActivity.this,
                            "您有新的消息来自：" + msg.getFromUser() + ":" + message, 0)
                            .show();// 其他好友的消息，就先提示，并保存到数据库
                    MediaPlayer.create(this, R.raw.msg).start();
                }
                break;
            default :
                break;
        }
    }*/


}
