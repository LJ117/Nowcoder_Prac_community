package com.nowcoder.community.service;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

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

    // 添加新消息
    public int addMessage(Message message){

        // 过滤 Html 标签
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));

        // 对新消息进行敏感词过滤
        message.setContent(sensitiveFilter.filter(message.getContent()));

        // 调用 mapper 插入新消息
        return messageMapper.insertMessage(message);
    }

    // 变更未读消息为已读
    public int readMessage(List<Integer> ids){
        // 已读状态: 1
        return messageMapper.updateStatus(ids,1);
    }

}
