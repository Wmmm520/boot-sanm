package com.sanm;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

/**
 * Author: Sanm
 * Date: 2020/8/29 19:35
 * description: 默认定时任务QuartzJobBean
 * 将具体的执行方法抽象出来
 **/
public abstract class AbstractQuartzJobBean extends QuartzJobBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    public abstract String name();

    /**
     * 执行具体的业务代码
     */
    protected abstract void executeBusiness(JobExecutionContext context) throws JobExecutionException;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (this.logger.isInfoEnabled()) {
            this.logger.info(" this task  {} execute {}...", name(), LocalDateTime.now());
        }
        executeBusiness(context);
        if (this.logger.isInfoEnabled()) {
            this.logger.debug(" this task {} executed {}...", name(), LocalDateTime.now());
        }
    }
}
