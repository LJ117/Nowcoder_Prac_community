package com.nowcoder.community.config;

import com.nowcoder.community.quartz.AlphaJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * 仅在初始化, 读取数据库中信息到配置, 之后Quartz 直接访问 DB 获取配置文件
 * <p>
 * 配置 -> 数据库 -> 调用
 */
@Configuration
public class QuartzConfig {

    // FactoryBean : 可简化Bean 的实例化过程:
    // 1. Spring 通过FactoryBean 封装Bean 的实例化过程.
    // 2. 将 FactoryBean 装配到Spring 容器里.
    // 3. 将 FactoryBean 注入给其他的Bean.
    // 4. 该Bean[ 3.中的Bean] 得到的是 FactoryBean 所管理的对象实例.

    // 配置 JobDetail
    //@Bean
    public JobDetailFactoryBean alphaJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");
        factoryBean.setGroup("alphaJobGroup");
        // 持久性设定
        factoryBean.setDurability(true);
        // 可恢复性
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }

    // 配置 Trigger(SimpleTriggerFactoryBean[简单的], CronTriggerFactoryBean[复杂的, 更详细的时间设定])
    // Spring 优先注入与参数同名的 JobDetail
    //@Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDeatil) {
        // 依赖 JobDetail
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDeatil);
        factoryBean.setName("alphaTrigger");
        factoryBean.setGroup("alphaTriggerGroup");
        factoryBean.setRepeatInterval(3000);
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }


}
