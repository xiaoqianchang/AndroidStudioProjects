package com.midas.websocketclient.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midas.websocketclient.ChatClient;
import com.midas.websocketclient.R;
import com.midas.websocketclient.adapter.ConversationAdapter;
import com.midas.websocketclient.bean.ZRUser;
import com.midas.websocketclient.bean.ZRConversation;
import com.midas.websocketclient.data.JSONDatas;
import com.midas.websocketclient.widget.ZRListView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 显示所有会话记录，比较简单的实现，更好的可能是把陌生人存入本地，这样取到的聊天记录是可控的
 * 
 * Created by Chang.Xiao on 2016/5/17.
 * @version 1.0
 */
public class ConversationActivity extends AppCompatActivity {

    private ZRListView mConversation;
    private ConversationAdapter mAdapter;
    private List<ZRConversation> conversationList = new ArrayList<ZRConversation>();

    public static String serverPath = "ws://172.16.101.73:8887"; // ws://172.16.101.39:9000/chat
//    public static String serverPath = "ws://172.16.101.39:9000/chat";

    private WebSocketClient cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_conversation_history);

        mConversation = (ZRListView) findViewById(R.id.conversion_listview);
        conversationList = getconversation();
        mAdapter = new ConversationAdapter(this, R.layout.row_chat_history, conversationList);
        // 设置adapter
        mConversation.setAdapter(mAdapter);

        mConversation.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(ConversationActivity.this, id+"aaaaaaaaaaaaaaaaa"+position, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ConversationActivity.this, ChatActivity.class).putExtra("contact", conversationList.get(position)));
            }
        });
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
//        List<ZRContact> contactlist = new ArrayList<ZRContact>();
        Gson gson = new Gson();
        List<ZRConversation> conversations = (List<ZRConversation>) gson.fromJson(JSONDatas.conversation, new TypeToken<List<ZRConversation>>(){}.getType());

        return conversations;
    }

    /**
     * 发送消息
     */
    private void performed() {
//        startActivity(new Intent(this, OtherActivity.class));
        try {
            // cc = new ChatClient(new URI(uriField.getText()), area, ( Draft ) draft.getSelectedItem() );
            cc = new ChatClient( this, new URI( serverPath ), new Draft_17());
            cc.connect();
        } catch ( URISyntaxException ex ) {
            Toast.makeText(this, "serverPath + \" is not a valid WebSocket URI\\n\"", Toast.LENGTH_LONG).show();
        }
    }

}
