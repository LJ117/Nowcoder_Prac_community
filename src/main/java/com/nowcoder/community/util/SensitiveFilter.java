package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private final static Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //定义替换敏感词的符号“***”
    private static final String REPLACEMENT = "***";

    // 初始化前缀树
    // 根节点, 一个空节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        // 编译后的 target 文件夹下 classes 里面
        // 记得关闭这个输入流
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                // 为了我们阅读方便,需要将字节流转成我们方便阅读的字符流
                // 字符流转为带缓冲的字符流, 提升效率
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 读到敏感词
                // 将该敏感词添加到前缀树对象中
                this.addKeyword(keyword);
            }

        } catch (IOException e) {
            logger.error("加载敏感词文件失败: " + e.getMessage());
        }
    }

    // 将敏感词添加到前缀树中
    private void addKeyword(String keyword) {
        // 声明一棵临时树, 挂到前缀树根节点
        // 将当前指针指向根节点
        TrieNode tempNode = rootNode;

        for (int i = 0; i < keyword.length(); i++) {
            // 获得本轮遍历得到的字符
            char c = keyword.charAt(i);

            // 获取当前树的子节点[ root 层的子节点 ]
            TrieNode subNode = tempNode.getSubNode(c);

            // 判断当前树中 这一层 是否已经有该子节点存在
            if (subNode == null) {
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            // 让指针指向子节点, 进入下一轮循环
            tempNode = subNode;

            // 设置结束标识
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        // 对传入文本判空
        if (StringUtils.isBlank(text)) {
            return null;
        }

        // 指针1: 只想敏感词 前缀树, 默认指向根
        TrieNode tempNode = rootNode;

        // 指针2
        int begin = 0;

        // 指针3
        int position = 0;

        // 记录最终结果, 用变长类型, 方便追加, 提升效率
        StringBuilder sb = new StringBuilder();


        // 指针3 到结尾, 遍历结束; 效率优于基于指针2遍历
        while (position < text.length()) {
            char c = text.charAt(position);

            // 跳过符号
            if (isSymbol(c)) {
                // 若指针1 处于根节点, 将此符号计入结果, 让指针2向下走一步
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头或中间, 指针3 都向下走一步
                position++;
                continue;
            }

            // 检测下级节点, 即将当前节点向下走
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // 该节点下级没有节点
                // 以 begin 为开头的字符串不是敏感词
                sb.append(text.charAt(begin));

                // 进入下一个位置,
                // position 也跟随 begin 一起移动
                position = ++begin;

                // 指针1 重新指回根节点, 判断下一个字符开头是否为敏感词
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                // 发现了敏感词, 将 begin 到 position 进行替换
                sb.append(REPLACEMENT);
                // 指针下移到下一个位置
                begin = ++position;
                // 指针1 重新指回根节点, 判断下一个字符开头是否为敏感词
                tempNode = rootNode;
            } else {
                // 继续检查下一个字符,
                // 当前的 指针1 也还在前缀树的有效位置,
                // 指针2、3也都满足前缀树上有效位
                 position++;
            }
        }

        // 循环走完了, 但是剩下的字符都不是敏感词
        // 将最后一批字符手动计入结果
        sb.append(text.substring(begin));

        return sb.toString();
    }

    // 判断当前字符是否为符号
    private boolean isSymbol(Character c) {
        // CharUtils.isAsciiAlphanumeric: 特殊字符返回 False
        // c < 0x2E80 || c > 0x9FFF: 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }


    // 定义一个内部类，前缀树类
    // 描述前缀树的某一个节点
    private class TrieNode {

        // 关键词结束标识
        private boolean isKeywordEnd = false;

        // 当前节点的子节点(key: 下级字符，value：下级节点)
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }

    }


}
