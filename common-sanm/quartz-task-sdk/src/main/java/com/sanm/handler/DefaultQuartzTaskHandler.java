package com.sanm.handler;

import com.sanm.domain.QuartzJob;
import com.sanm.domain.QuartzTrigger;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: Sanm
 * Date: 2020/8/29 19:21
 * description: 默认实现类型
 **/
public class DefaultQuartzTaskHandler extends AbstractQuartzTaskHandler {

    @Override
    public void addJob(QuartzJob quartzJob) throws SchedulerException {
        this.operationJob(false, quartzJob);
    }

    @Override
    public void addJob(QuartzJob... quartzJobs) throws SchedulerException {
        Assert.notNull(quartzJobs, "jobs isn't null.");
        for (QuartzJob quartzJob : quartzJobs) {
            this.addJob(quartzJob);
        }
    }

    @Override
    public void asyncAddJob(QuartzJob quartzJob) {
        new Thread(() -> {
            try {
                addJob(quartzJob);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void asyncAddJob(QuartzJob... quartzJobs) {
        Assert.notNull(quartzJobs, "jobs isn't null.");
        for (QuartzJob quartzJob : quartzJobs) {
            this.asyncAddJob(quartzJob);
        }
    }

    @Override
    public void updateJob(QuartzJob quartzJob) throws SchedulerException {
        this.operationJob(true, quartzJob);
    }

    @Override
    public void updateJob(QuartzJob... quartzJobs) throws SchedulerException {
        Assert.notNull(quartzJobs, "jobs isn't null.");
        for (QuartzJob quartzJob : quartzJobs) {
            this.updateJob(quartzJob);
        }
    }

    @Override
    public void pauseJob(QuartzJob quartzJob) throws SchedulerException {
        pauseJob(quartzJob.getKey());
    }

    @Override
    public void pauseJob(QuartzJob... quartzJobs) throws SchedulerException {
        Assert.notNull(quartzJobs, "jobs aren't null.");

        List<JobKey> jobKeys = Stream.of(quartzJobs).parallel()
                .map(QuartzJob::getKey).collect(Collectors.toList());
        pauseJob(jobKeys);

    }

    @Override
    public void pauseJob(JobKey jobKey) throws SchedulerException {
        Assert.notNull(jobKey, "the job isn't null.");
        Assert.notNull(jobKey.getName(), "job's name isn't null.");

        this.scheduler.pauseJob(jobKey);

    }

    @Override
    public void pauseJob(List<JobKey> jobKeys) throws SchedulerException {
        Assert.notNull(jobKeys, "jobs aren't null.");

        for (JobKey jobKey : jobKeys) {
            pauseJob(jobKey);
        }
    }

    @Override
    public void pauseJob(GroupMatcher<JobKey> matcher) throws SchedulerException {
        Assert.notNull(matcher, "matcher isn't null/");

        this.scheduler.pauseJobs(matcher);
    }

    @Override
    public void resumeJob(QuartzJob quartzJob) throws SchedulerException {
        resumeJob(quartzJob.getKey());
    }

    @Override
    public void resumeJob(QuartzJob... quartzJobs) throws SchedulerException {

        Assert.notNull(quartzJobs, "jobs isn't null.");
        List<JobKey> jobKeys = Stream.of(quartzJobs).parallel().map(QuartzJob::getKey)
                .collect(Collectors.toList());
        resumeJob(jobKeys);
    }

    @Override
    public void resumeJob(JobKey jobKey) throws SchedulerException {
        Assert.notNull(jobKey, "jobKey isn't null.");
        Assert.notNull(jobKey.getName(), "job's name's not null.");
        this.scheduler.resumeJob(jobKey);
    }

    @Override
    public void resumeJob(List<JobKey> jobKeys) throws SchedulerException {
        Assert.notNull(jobKeys, "jobs aren't null.");

        for (JobKey jobKey : jobKeys) {
            resumeJob(jobKey);
        }
    }

    @Override
    public void resumeJob(GroupMatcher<JobKey> matcher) throws SchedulerException {

        Assert.notNull(matcher, "matcher isn't null.");

        this.scheduler.resumeJobs(matcher);

    }

    @Override
    public void triggerJob(QuartzTrigger quartzTrigger) throws SchedulerException {
        Assert.notNull(quartzTrigger, "trigger isn't null.");
        Assert.notNull(quartzTrigger.getJobKey(), "trigger's job isn't null.");
        Assert.notNull(quartzTrigger.getJobKey().getName(), "trigger's jobJey isn't null.");
        Assert.notNull(quartzTrigger.getKey(), "triggerKey isn't null.");
        Assert.notNull(quartzTrigger.getKey().getName(), "triggerKey's name isn't null.");

        JobKey jobKey = quartzTrigger.getJobKey();
        TriggerKey triggerKey = quartzTrigger.getKey();

        JobDataMap jobDataMap = null;

        if (!ObjectUtils.isEmpty(quartzTrigger.getJobData())) {
            jobDataMap = new JobDataMap(quartzTrigger.getJobData());
        } else {
            //查找该Trigger是否在Scheduler已存在且有JobData
            if (checkExists(triggerKey)) {
                Trigger triggerInScheduler = getTrigger(triggerKey);
                jobDataMap = triggerInScheduler.getJobDataMap();
            }
        }

        triggerJob(jobKey, jobDataMap);
    }

    @Override
    public void triggerJob(QuartzTrigger... quartzTriggers) throws SchedulerException {
        Assert.notNull(quartzTriggers, "quartzTriggers isn't null.");

        for (QuartzTrigger quartzTrigger : quartzTriggers) {
            triggerJob(quartzTrigger);
        }
    }

    @Override
    public void triggerJob(JobKey jobKey) throws SchedulerException {
        triggerJob(jobKey, null);
    }

    @Override
    public void triggerJob(JobKey jobKey, JobDataMap jobDataMap) throws SchedulerException {
        Assert.notNull(jobKey, "jobKey isn't null.");
        Assert.notNull(jobKey.getName(), "jobKey's name isn't null.");

        this.scheduler.triggerJob(jobKey, jobDataMap);
    }

    @Override
    public void deleteJob(QuartzJob quartzJob) throws SchedulerException {
        deleteJob(quartzJob.getKey());
    }

    @Override
    public void deleteJob(QuartzJob... quartzJobs) throws SchedulerException {
        Assert.notNull(quartzJobs, "quartzJobs aren't null.");

        List<JobKey> jobKeys = Stream.of(quartzJobs).parallel().map(QuartzJob::getKey).collect(Collectors.toList());

        deleteJob(jobKeys);
    }

    @Override
    public void deleteJob(JobKey jobKey) throws SchedulerException {
        Assert.notNull(jobKey, "jobKey isn't null.");
        Assert.notNull(jobKey.getName(), "jobKey's name isn't null.");

        this.scheduler.deleteJob(jobKey);
    }

    @Override
    public void deleteJob(List<JobKey> jobKeys) throws SchedulerException {
        Assert.notNull(jobKeys, "jobKeys aren't null.");

        this.scheduler.deleteJobs(jobKeys);
    }

    @Override
    public void addTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        Assert.notNull(quartzTrigger, "trigger's not null.");
        Assert.notNull(quartzTrigger.getJobKey(), "trigger's key isn't null.");
        Assert.notNull(quartzTrigger.getJobKey().getName(), "JobKey's name isn't null.");
        Assert.notNull(quartzTrigger.getKey(), "triggerKey isn't null.");
        Assert.notNull(quartzTrigger.getKey().getName(), "triggerKey's name isn't null.");

        Trigger trigger = createTrigger(quartzTrigger);

        this.scheduler.scheduleJob(trigger);
    }

    @Override
    public void addTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException {
        Assert.notNull(quartzTriggers, "triggers aren't null.");

        for (QuartzTrigger quartzTrigger : quartzTriggers) {
            addTrigger(quartzTrigger);
        }
    }

    @Override
    public void updateTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        Assert.notNull(quartzTrigger, "trigger's not null.");
        Assert.notNull(quartzTrigger.getJobKey(), "trigger'jobKey isn't null.");
        Assert.notNull(quartzTrigger.getJobKey().getName(), "jobKey's name isn't null.");
        Assert.notNull(quartzTrigger.getOriginalKey(), "original's Key  isn't null.");
        Assert.notNull(quartzTrigger.getOriginalKey().getName(), "original's Key name isn't null.");
        Assert.notNull(quartzTrigger.getKey(), "key isn't null.");
        Assert.notNull(quartzTrigger.getKey().getName(), "key's name isn't null.");

        TriggerKey originalKey = quartzTrigger.getOriginalKey();

        if (!checkExists(originalKey)) {
            throw new SchedulerException("this trigger isn't exist in the scheduler.");
        } else {
            JobKey jobKeyOriginal = getTrigger(originalKey).getJobKey();
            JobKey jobKey = quartzTrigger.getJobKey();

            if (!jobKeyOriginal.equals(jobKey)) {
                throw new SchedulerException("the trigger can't update its job.");
            }
        }

        Trigger trigger = createTrigger(quartzTrigger);

        this.scheduler.rescheduleJob(originalKey, trigger);
    }

    @Override
    public void updateTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException {
        Assert.notNull(quartzTriggers, "triggers aren't null.");

        for (QuartzTrigger quartzTrigger : quartzTriggers) {
            updateTrigger(quartzTrigger);
        }
    }

    @Override
    public void pauseTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        pauseTrigger(quartzTrigger.getKey());
    }

    @Override
    public void pauseTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException {
        Assert.notNull(quartzTriggers, "triggers aren't null.");

        List<TriggerKey> triggerKeys = Stream.of(quartzTriggers).parallel()
                .map(QuartzTrigger::getKey).collect(Collectors.toList());

        pauseTrigger(triggerKeys);
    }

    @Override
    public void pauseTrigger(TriggerKey triggerKey) throws SchedulerException {
        Assert.notNull(triggerKey, "trigger isn't null.");
        Assert.notNull(triggerKey.getName(), "trigger's name isn't null.'");

        this.scheduler.pauseTrigger(triggerKey);
    }

    @Override
    public void pauseTrigger(List<TriggerKey> triggerKeys) throws SchedulerException {
        Assert.notNull(triggerKeys, "triggers can't null.");

        for (TriggerKey triggerKey : triggerKeys) {
            pauseTrigger(triggerKey);
        }
    }

    @Override
    public void pauseTrigger(GroupMatcher<TriggerKey> matcher) throws SchedulerException {
        Assert.notNull(matcher, "matcher isn't null.");
        this.scheduler.pauseTriggers(matcher);
    }

    @Override
    public void pauseAll() throws SchedulerException {
        this.scheduler.pauseAll();
    }

    @Override
    public void resumeTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        resumeTrigger(quartzTrigger.getKey());
    }

    @Override
    public void resumeTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException {
        Assert.notNull(quartzTriggers, "triggers aren't null.");
        List<TriggerKey> triggerKeys = Stream.of(quartzTriggers).parallel().map(QuartzTrigger::getKey).collect(Collectors.toList());
        resumeTrigger(triggerKeys);
    }

    @Override
    public void resumeTrigger(TriggerKey triggerKey) throws SchedulerException {
        Assert.notNull(triggerKey, "trigger's not null.");
        Assert.notNull(triggerKey.getName(), "trigger's name isn't null.'");

        this.scheduler.resumeTrigger(triggerKey);
    }

    @Override
    public void resumeTrigger(List<TriggerKey> triggerKeys) throws SchedulerException {
        Assert.notNull(triggerKeys, "triggers aren't null.");
        for (TriggerKey triggerKey : triggerKeys) {
            resumeTrigger(triggerKey);
        }
    }

    @Override
    public void resumeTrigger(GroupMatcher<TriggerKey> matcher) throws SchedulerException {
        Assert.notNull(matcher, "matcher isn't null.");

        this.scheduler.resumeTriggers(matcher);
    }

    @Override
    public void deleteTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        deleteTrigger(quartzTrigger.getKey());
    }

    @Override
    public void deleteTrigger(QuartzTrigger... quartzTriggers) throws SchedulerException {
        Assert.notNull(quartzTriggers, "triggers aren't null.");

        List<TriggerKey> triggerKeys = Stream.of(quartzTriggers).parallel().map(QuartzTrigger::getKey).collect(Collectors.toList());

        deleteTrigger(triggerKeys);
    }

    @Override
    public void deleteTrigger(TriggerKey triggerKey) throws SchedulerException {
        Assert.notNull(triggerKey, "trigger isn't null.");
        Assert.notNull(triggerKey.getName(), "trigger's name isn't null.");

        this.scheduler.unscheduleJob(triggerKey);
    }

    @Override
    public void deleteTrigger(List<TriggerKey> triggerKeys) throws SchedulerException {
        Assert.notNull(triggerKeys, "triggers aren't null.");

        this.scheduler.unscheduleJobs(triggerKeys);
    }
}
