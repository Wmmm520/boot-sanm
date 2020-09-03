package com.sanm.config;

import com.sanm.handler.AbstractQuartzTaskHandler;
import com.sanm.handler.DefaultQuartzTaskHandler;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Author: Sanm
 * since: v1.0
 * description:
 **/
@Configuration
@ConditionalOnMissingBean(AbstractQuartzTaskHandler.class)
public class QuartzTaskConfig {

    @Resource
    private Scheduler scheduler;

    @Bean
    public DefaultQuartzTaskHandler defaultQuartzTaskHandler() {
        DefaultQuartzTaskHandler defaultQuartzTaskHandler = new DefaultQuartzTaskHandler();
        defaultQuartzTaskHandler.setScheduler(scheduler);
        return defaultQuartzTaskHandler;
    }
}
