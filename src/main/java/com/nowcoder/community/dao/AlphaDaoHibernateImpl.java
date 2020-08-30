package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

//注解后加("...") , 里面的字符串就是自定义的 Bean 的名字
@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao {
    @Override
    public String select() {
        return "Hibernate";
    }
}
