package com.nowcoder.community.actuator;

import com.nowcoder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@Endpoint(id = "database")
public class DatabaseEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseEndpoint.class);

    // 尝试获取链接, 观察链接是否正常
    // 这里通过连接池获取链接
    @Autowired
    private DataSource dataSource;

    @ReadOperation// 指明该方法访问是"GET"请求的, "POST" 用 WriteOperation
    public String checkConnection(){
        try (
                Connection connection = dataSource.getConnection()
        ){
            return CommunityUtil.getJSONString(0,"获取链接成功");
        } catch (SQLException e) {
            logger.error("获取链接失败!"+e.getMessage());
            return CommunityUtil.getJSONString(1,"获取链接失败!");
        }
    }

}
