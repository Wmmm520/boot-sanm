package com.sanm.defaults;

import com.sanm.AbstractQuartzJobBean;
import com.sanm.annotation.jobOperation.JobOperation;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Author: Sanm
 * since: v1.0
 * description: 基于默认数据的操作。
 **/
@JobOperation(jobCode = -1, triggerCode = -1)
public class DefaultJob extends AbstractQuartzJobBean {
    @Override
    public String name() {
        return "default-job";
    }

    @Override
    protected void executeBusiness(JobExecutionContext context) throws JobExecutionException {
        System.out.println("默认数据....");
    }

}
