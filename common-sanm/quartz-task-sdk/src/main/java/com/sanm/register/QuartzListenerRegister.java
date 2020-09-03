package com.sanm.register;

import com.sanm.listener.AbstractJobListener;
import com.sanm.listener.AbstractSchedulerListener;
import com.sanm.listener.AbstractTriggerListener;
import org.apache.commons.collections.MapUtils;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * Author: Sanm
 * since: v1.0
 * description:
 * 利用spring boot的生命周期,启动的时候,将项目注册到spring容器中
 **/
public class QuartzListenerRegister implements ApplicationContextAware, InitializingBean {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ApplicationContext context;

    private Scheduler scheduler;


    @Override
    public void afterPropertiesSet() throws Exception {
        awareSchedulerListeners();
        awareJobListeners();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * 注册SchedulerListener
     */
    private void awareSchedulerListeners() throws Exception {
        log.info("------ loading schedulerListener starting ------");
        Map<String, AbstractSchedulerListener> schedulerListenerMap = context.getBeansOfType(AbstractSchedulerListener.class);
        if (MapUtils.isEmpty(schedulerListenerMap)) {
            log.info("can't register schedulerListener in system.");
            return;
        }

        for (AbstractSchedulerListener schedulerListener : schedulerListenerMap.values()) {
            scheduler.getListenerManager().addSchedulerListener(schedulerListener);
        }

        log.info("-------------loading schedulerListener end.----------------");
    }

    /**
     * 自动注册 JobListener
     */
    private void awareJobListeners() throws Exception {
        log.info("-------------loading JobListener starting.----------------------");
        Map<String, AbstractJobListener> jobListenerMap = context.getBeansOfType(AbstractJobListener.class);

        if (MapUtils.isEmpty(jobListenerMap)) {
            log.info("can't register jobListener in system.");
            return;
        }

        for (AbstractJobListener jobListener : jobListenerMap.values()) {
            scheduler.getListenerManager().addJobListener(jobListener);
        }

        log.info("-------------loading jobListener end.----------------");
    }

    /**
     * 自动注册触发器
     */
    private void awareTriggerListeners() throws Exception {
        log.info("-------------loading TriggerListeners starting.----------------------");

        Map<String, AbstractTriggerListener> triggerListenerMap = context.getBeansOfType(AbstractTriggerListener.class);

        if (MapUtils.isEmpty(triggerListenerMap)) {
            log.info("系统未注册TriggerListener！");
            return;
        }

        for (AbstractTriggerListener abstractTriggerListener : triggerListenerMap.values()) {
            log.info("--------" + abstractTriggerListener.getClass().getName());
            scheduler.getListenerManager().addTriggerListener(abstractTriggerListener);
        }

        log.info("-------------loading TriggerListeners ending.----------------------");
    }

}
