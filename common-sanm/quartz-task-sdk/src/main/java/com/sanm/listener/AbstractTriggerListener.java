package com.sanm.listener;

import org.quartz.Matcher;
import org.quartz.TriggerKey;
import org.quartz.TriggerListener;
import org.quartz.impl.matchers.EverythingMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Sanm
 * since: v1.0
 * description:
 * 可匹配任务,配置后可自动注册
 **/
public abstract class AbstractTriggerListener implements TriggerListener {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public Matcher<TriggerKey> matcher() {
        return EverythingMatcher.allTriggers();
    }
}
