package com.midas.websocketclient.bean;

import java.util.List;

/**
 * ModelManager
 * <p/>
 * Created by Chang.Xiao on 2016/5/23.
 *
 * @version 1.0
 */
public class ZRSocketModelManager {

    public static ZRSocketModelManager mSocketModelManager;

    /**
     * 会话列表
     */
    private List<ZRConversationList> conversationList;

    private ZRSocketModelManager() {
    }

    /**
     * Get instance
     * @return
     */
    public static ZRSocketModelManager getInstance() {
        if (mSocketModelManager == null) {
            synchronized (ZRSocketModelManager.class) {
                if (mSocketModelManager == null) {
                    mSocketModelManager = new ZRSocketModelManager();
                }
            }
        }
        return mSocketModelManager;
    }

    /**
     * 初始化Model全部数据
     * 与DB同步
     * @return
     */
    public List<ZRConversationList> load() {
        return null;
    }

    /**
     * 获取未读数量总数，用sql
     * @return
     */
    public int getNotReadAllCount() {
        return 0;
    }

    /**
     * 创建新会话，包括新用户
     * @param user
     * @return
     */
    public ZRMessage createNewConversation(ZRUser user) {
        return null;
    }

    public void sendMessage() {}
}
