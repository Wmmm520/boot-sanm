package com.sanm.listener.miss;

import com.sanm.listener.AbstractJobListener;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Matcher;
import org.quartz.TriggerKey;

import java.time.LocalDateTime;

/**
 * Author: Sanm
 * since: v1.0
 * description: 默认全局JobListen
 **/
public class DefaultGlobalJobListener extends AbstractJobListener {
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Matcher<TriggerKey> matcher() {
        return super.matcher();
    }


    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Job {} is starting.", new Object[]{jobExecutionContext.getJobDetail()});
        }
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Job {} 在 {} 被拒绝，将不再执行",
                    new Object[]{jobExecutionContext.getJobDetail(), LocalDateTime.now()});
        }
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Job {} 于 {} 执行，",
                    new Object[]{jobExecutionContext.getJobDetail(), LocalDateTime.now()});

            if (e != null) {
                this.logger.debug("Job {} 执行中发生异常，异常信息为 {}，",
                        new Object[]{jobExecutionContext.getJobDetail(), e});
            }
        }
    }
}
