package com.sanm.register;

import com.sanm.annotation.jobOperation.DefaultQuartzJobEnum;
import com.sanm.annotation.jobOperation.DefaultQuartzTriggerEnum;
import com.sanm.annotation.jobOperation.JobOperation;
import com.sanm.annotation.jobOperationScan.JobOperationScanRegister;
import com.sanm.builder.QuartzJobBuilder;
import com.sanm.builder.QuartzTriggerBuilder;
import com.sanm.domain.QuartzJob;
import com.sanm.domain.QuartzTrigger;
import com.sanm.handler.DefaultQuartzTaskHandler;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Author: Sanm
 * since: v1.0
 * description: 将容器注入到spring 容器中   项目启动的时候,基于spring boot的声明周期实现。
 * 这里只是一个demo.不做深入操作。
 **/
public class QuartzTaskRegister implements ApplicationContextAware, InitializingBean {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ApplicationContext context;

    @Override
    public void afterPropertiesSet() throws Exception {
        initJob();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private void initJob() {
        Map<String, JobOperation> map = JobOperationScanRegister.JOB_OPERATION_MAP;
        DefaultQuartzTaskHandler defaultQuartzTaskHandler = context.getBean(DefaultQuartzTaskHandler.class);
        for (String key : map.keySet()) {

            JobOperation jobOperation = map.get(key);

            //去除默认的定时器
            if (!key.equals("com.sanm.defaults.DefaultJob") || map.size() < 2) {
                DefaultQuartzTriggerEnum quartzTriggerEnum = DefaultQuartzTriggerEnum.findByCode(jobOperation.triggerCode());
                QuartzTrigger quartzTrigger = QuartzTriggerBuilder.newTrigger().forJob(quartzTriggerEnum.getName(), quartzTriggerEnum.getGroupName())
                        .ofType(quartzTriggerEnum.getType()).startAt(quartzTriggerEnum.getStartTime()).endAt(quartzTriggerEnum.getEndTime()).withCronExpression(quartzTriggerEnum.getCronExpression())
                        .repeatForever().withDescription(quartzTriggerEnum.getDescription()).startNow(quartzTriggerEnum.isStartNow()).build();
                Set<QuartzTrigger> quartzTriggers = new HashSet<>();
                quartzTriggers.add(quartzTrigger);
                DefaultQuartzJobEnum quartzJobEnum = DefaultQuartzJobEnum.findByCode(jobOperation.jobCode());

                QuartzJob quartzJob = QuartzJobBuilder.newJob().setTrigger(quartzTriggers).withIdentity(quartzJobEnum.getName(), quartzJobEnum.getGroupName())
                        .forJob(quartzJobEnum.getJobClass()).withDescription(quartzJobEnum.getDescription()).build();

                try {
                    if (!defaultQuartzTaskHandler.checkExists(new JobKey(quartzTriggerEnum.getName(), quartzTriggerEnum.getGroupName()))) {
                        defaultQuartzTaskHandler.addJob(quartzJob);
                        log.info("add success------. {}", quartzJob.toString());
                    } else {
                        //先删除后,在进行添加更新
                        defaultQuartzTaskHandler.deleteJob(quartzJob);
                        defaultQuartzTaskHandler.updateJob(quartzJob);
                    }

                } catch (SchedulerException e) {
                    log.info("------------------");
                    e.printStackTrace();
                }
            }
        }

    }
}
