package com.nowcoder.community.service;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    // 查找会话列表
    public List<Message> findConversations(int userId,int offset,int limit){
        return messageMapper.selectConversations(userId, offset, limit);
    }

    // 统计当前会话数
    public int findConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }

    // 查找私信列表
    public List<Message> findLetters(String conversationId,int offset,int limit){
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    // 统计私信数量
    public int findLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }

    // 统计未读消息数量
    public int findLetterUnreadCount(int userId,String conversationId){
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

}
