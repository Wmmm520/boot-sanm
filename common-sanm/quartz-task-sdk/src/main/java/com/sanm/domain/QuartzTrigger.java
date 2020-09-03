package com.sanm.domain;

import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Author: Sanm
 * since: v1.0
 * description: 定时任务触发器
 **/
public class QuartzTrigger implements Serializable {

    private static final long serialVersionUID = 446936256465096569L;


    /**
     * 定时任务触发器类别  后两种基本上没用过，这里不做介绍。
     */
    public enum TriggerType {
        /**
         * simpleTrigger
         * 默认使用这种类别。
         * 具体指的是 一个指定时间段内执行一次作业任务或者在指定时间段内多次执行任务
         * eg:
         * 距离当前时间4s后首次执行任务,6s后停止。每隔2s执行,执行3次。
         * //获取距离当前时间4秒后的时间
         * date.setTime(date.getTime()+4000);
         * //获取距离当前时间6秒后的时间
         * Date endDate=new Date();
         * endDate.setTime(endDate.getTime()+6000);
         * //创建一个SimpleTrigger实例，定义该Job4秒后执行，并且每隔两秒钟重复执行一次，6秒后停止
         * SimpleTrigger trigger=(SimpleTrigger)TriggerBuilder.newTrigger().withIdentity("myTrigger", "group1")//定义name/group
         * .startAt(date)   //设置开始时间
         * .endAt(endDate)	 //设置结束时间
         * .withSchedule(SimpleScheduleBuilder.simpleSchedule() //使用SimpleTrigger
         * .withIntervalInSeconds(2) //每隔两秒执行一次
         * .withRepeatCount(3) //重复执行三次
         * ).build();
         */
        SIMPLE,
        /**
         * CronTrigger 基于日历的作业调度。
         * 一般使用这种方式进行任务调度。因为SimpleTrigger能够实现的,该方式都可以实现。
         */
        CRON,

    }

    private TriggerType type = TriggerType.CRON; //默认为simple级别

    //表示与该Trigger绑定的Job实例的标识,触发器被触发时，该指定的Job实例会被执行
    private JobKey jobKey;

    private TriggerKey originalKey;

    private TriggerKey key;

    private String description;

    private String calendarName;

    //开始时间和结束时间本想设置为localDateTime 但是quartz文档要求设置为Date
    private Date startTime;

    private boolean startNow;

    private Date endTime;

    private Map<String, Object> jobData;

    /**
     * 重复次数,则设置该值为:{@link org.quartz.SimpleTrigger#REPEAT_INDEFINITELY}
     * <p>
     * int MISFIRE_INSTRUCTION_FIRE_NOW = 1;
     * int MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT = 2;
     * int MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT = 3;
     * int MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT = 4;
     * int MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT = 5;
     * int REPEAT_INDEFINITELY = -1;
     * </p>
     */
    private int repeatCount;

    /**
     * 重复时间间隔,单位:ms
     */
    private long repeatInterval;

    private String cronExpression;

    /**
     * 通用策略: MISFIRE_INSTRUCTION_SMART_POLICY （quartz框架自动选择适合的策略）
     * 解释: 以错过的第一个频率时间立刻开始执行.重做错过的所有频率周期后, 当下一次触发频率
     * 发生时间大于当前时间后,再按照正常的Cron频率依次执行
     * <p>
     * SimpleTrigger策略
     * MISFIRE_INSTRUCTION_FIRE_ONCE_NOW：
     * 针对 misfired job 马上执行一次；
     * MISFIRE_INSTRUCTION_DO_NOTHING：
     * 忽略 misfired job，等待下次触发；默认是
     * <p>
     * // 立即执行
     * public static final int MISFIRE_INSTRUCTION_FIRE_NOW = 1;
     * // 立即执行，并累计到已经执行次数，如果结束时间已经过了，则不会再执行。
     * public static final int MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT = 2;
     * // 立即执行，并累计到未执行次数，如果结束时间已经过了，则不会再执行。
     * public static final int MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT = 3;
     * // 告诉Quartz在下一次执行时间再次开始执行，并累计到未执行次数
     * public static final int MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT = 4;
     * // 告诉Quartz在下一次执行时间再次开始执行，并累计到已经执行次数
     * public static final int MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT = 5;
     */
    private int misfireInstruction = Trigger.MISFIRE_INSTRUCTION_SMART_POLICY;


    public TriggerType getType() {
        return type;
    }

    public void setType(TriggerType type) {
        this.type = type;
    }

    public JobKey getJobKey() {
        return jobKey;
    }

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    public TriggerKey getOriginalKey() {
        return originalKey;
    }

    public void setOriginalKey(TriggerKey originalKey) {
        this.originalKey = originalKey;
    }

    public TriggerKey getKey() {
        return key;
    }

    public void setKey(TriggerKey key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public boolean isStartNow() {
        return startNow;
    }

    public void setStartNow(boolean startNow) {
        this.startNow = startNow;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Map<String, Object> getJobData() {
        return jobData;
    }

    public void setJobData(Map<String, Object> jobData) {
        this.jobData = jobData;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public int getMisfireInstruction() {
        return misfireInstruction;
    }

    public void setMisfireInstruction(int misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }
}
