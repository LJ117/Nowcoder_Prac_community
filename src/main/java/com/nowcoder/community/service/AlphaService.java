package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

//@Service
//@Scope("prototype")
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    // 注入 Spring 自动生成的 TransactionTemplate Bean
    @Autowired
    private TransactionTemplate transactionTemplate;

    public AlphaService() {
        System.out.println("实例化AlphaService");
    }

    // 这个方法会在构造器之后调用
    @PostConstruct
    public void init() {
        System.out.println("初始化AlphaService");
    }

    // 在销毁对象之前调用
    @PreDestroy
    public void destroy() {
        System.out.println("销毁AlphaService");
    }

    // 模拟查询
    public String find() {
        return alphaDao.select();
    }


    // @Transactional: Spring 自带的事务管理
    // 参数 isolation =  手动指定的隔离级别
    // 参数 propagation =  手动指定的事务传播机制, 下面是三个常用参数
        // 事务传播机制: 解决 业务互相调用时, 隔离级别以谁为准的问题
        // 例如: Service A 调用 Service B
        // REQUIRED: 支持当前事务【Service A，又叫外部事物，调用我的调用者】，如果不存在，则创建新事务
        // REQUIRES_NEW: 创建一个新的事务，并且暂停当前事务（外部事务），B 不管 A的事务，按自己的来
        // NESTED: 如果当前存在事务【外部事务存在】，则嵌套在该事务中执行（B在执行时，有独立的提交和回滚），外部不存在则同 REQUIRED
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1() {
        // 新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
        user.setEmail("alpha@qq.com");
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("hello");
        post.setContent("新人报道!");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        // 人为造错
        Integer.valueOf("abc");

        return "OK";
    }


    public Object save2(){
        // 手动设置隔离级别(参数是定义好的常量)
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        // 手动设置事务传播机制(参数是定义好的常量)
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        // 事务的实现. 调用 .execute() 方法, 参数是一个 TransactionCallback 类的对象, 事务回调对象,
        // 可用匿名内部类实现, 该对象的 泛型即 该方法的返回类型
        // Template底层 自动调用的, 方法逻辑由设计者自己实现
        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                // 新增用户
                User user = new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
                user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
                user.setEmail("beta@qq.com");
                user.setHeaderUrl("http://image.nowcoder.com/head/199t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                // 新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("你好");
                post.setContent("我是新人, 前来报道!");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                // 人为造错
                Integer.valueOf("abc");
                return "Ok";
            }
        });
    }
}
