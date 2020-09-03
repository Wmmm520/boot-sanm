package com.sanm.listener;

import org.quartz.JobListener;
import org.quartz.Matcher;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.EverythingMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Author: Sanm
 * since: v1.0
 * description:
 * 可匹配任务.定义并配置,系统自动注册
 **/
public abstract class AbstractJobListener implements JobListener {

    protected final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 返回匹配某类job的匹配策略
     */
    public Matcher<TriggerKey> matcher() {
        return EverythingMatcher.allTriggers();
    }
}
