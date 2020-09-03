package com.sanm.listener.miss;

import com.sanm.listener.AbstractTriggerListener;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

/**
 * Author: Sanm
 * since: v1.0
 * description:
 * 默认全局TriggerListener
 **/
public class DefaultGlobalTriggerListener extends AbstractTriggerListener {
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {

    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {

    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        logger.info("任务：{}执行完成，开始执行：{}，上次执行：{}，下次执行：{}", trigger.getKey().toString(), trigger.getStartTime(), trigger.getPreviousFireTime(), trigger.getNextFireTime());
    }
}
