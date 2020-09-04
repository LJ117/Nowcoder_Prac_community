package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * 这个 Mapper的实现方式 为 : 注解方法实现
 */
@Mapper
public interface LoginTicketMapper {

    // 插入数据
    // @Insert({"","",""}) 将小括号里的大括号的 所有字符串拼接成一个sql,
    // 下面的 所有 CRUD 同理
    @Insert({
            // 记得在字符串换行的时候, 在末尾加一个 空格
            "INSERT INTO login_ticket (user_id,ticket,status,expired) ",
            // values(..) 里面是 形参的属性
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    // 主键回填, 要设置主键自动生成, 指定 主键
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    // 查询方法, 凭证: ticket
    @Select({
            "SELECT id,user_id,ticket,status,expired ",
            "FROM login_ticket WHERE ticket = #{ticket};"
    })
    LoginTicket selectByTicket(String ticket);

    // 修改凭证状态, 用户退出后, 修改用户状态
    // 也可以使用动态语句： if。。。；
    // 但是需要像下面的代码添加 <script> 标签， 在之中写
    @Update({
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket} ",
            "<if test=\"ticket!=null\"> ",
            "and 1=1 ",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket, int status);

}
