package com.midas.websocketclient;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * WebSocketClient客服端
 * 自己可以在对应的函数里面做响应的处理，比如当链接发生错误时要重新去打开该链接，
 * 收到消息时即时的保存聊天记录和发送系统通知来提醒用户查看新消息等等
 * <p/>
 * Created by Chang.Xiao on 2016/5/12.
 *
 * @version 1.0
 */
public class ChatClient extends WebSocketClient {

    // 上下文对象
    private Context mContext;
    // socket返回json解析类对象
    private WebSocketParser mParser;
    // 系统通知管理
    private NotificationManager mNotificationManager;
    // 通知
    private Notification mNotification;
    // 意图
    private PendingIntent mPendingIntent;

    public ChatClient(URI serverURI) {
        super(serverURI);
    }

    public ChatClient(Context context, URI serverUri, Draft draft) {
        super(serverUri, draft);
        this.mContext = context;
        // 新消息的解析类
        this.mParser = WebSocketParser.getInstance();
        // 收到新消息发送的通知
        this.mNotificationManager = (NotificationManager) this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        this.mNotification = new Notification(R.mipmap.ic_launcher, "您有新消息！", System.currentTimeMillis());
    }

    /**
     * 链接打开
     * send broadcast <SocketConstant>ON_OPEN filter if this socket opened
     * socket 打开时发送的广播，若想提示，可以接受并处理
     * @param handshakedata
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
//        Intent intent = new Intent(SocketConstant.ON_OPEN);
//        mContext.sendBroadcast(intent);
        Toast.makeText(mContext, "链接上服务器", Toast.LENGTH_SHORT).show();
    }

    /**
     * 新消息
     * send broadcast <SocketConstant>ON_RECEIVE filter if this socket closed
     * socket 发送消息时发送的广播，若想提示，可以接受并处理
     * @param message
     */
    @Override
    public void onMessage(String message) {
        // 这里message不同---parser
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 链接关闭
     * send broadcast <SocketConstant>ON_CLOSE filter if this socket closed
     * socket 发生关闭时发送的广播，若想提示，可以接受并处理
     * @param code
     * @param reason
     * @param remote
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        // 更改保存的链接状态...

//        mNotificationManager.cancelAll();
//        Intent intent = new Intent(SocketConstant.ON_CLOSE);
//        intent.putExtra(SocketConstant.ON_CLOSE, reason);
//        mContext.sendBroadcast(intent);
        Toast.makeText(mContext, "链接关闭", Toast.LENGTH_SHORT).show();
    }

    /**
     * 链接发生错误
     * send broadcast <SocketConstant>ON_ERROR filter if this socket has error
     * socket 发生错误发送的广播，若想提示，可以接受并处理
     * @param ex
     */
    @Override
    public void onError(Exception ex) {
//        Intent intent = new Intent(SocketConstant.ON_ERROR);
//        intent.putExtra(SocketConstant.ON_ERROR, ex.toString());
//        mContext.sendBroadcast(intent);
        Toast.makeText(mContext, "链接发生错误", Toast.LENGTH_SHORT).show();
    }
}
