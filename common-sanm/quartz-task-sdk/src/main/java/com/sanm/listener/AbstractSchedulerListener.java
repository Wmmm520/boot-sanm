package com.sanm.listener;

import org.quartz.SchedulerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Sanm
 * since: v1.0
 * description:
 * 定义配置后.系统可自动注册
 **/
public abstract class AbstractSchedulerListener implements SchedulerListener {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
}
