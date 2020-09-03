package com.sanm.annotation.jobOperation;

import com.sanm.domain.QuartzTrigger;
import org.quartz.Trigger;

import java.util.Date;
import java.util.Map;

/**
 * Author: Sanm
 * since: v1.0
 * description:
 **/
public enum DefaultQuartzTriggerEnum {

    DEFAULT_QUARTZ_TRIGGER_ENUM(-1, QuartzTrigger.TriggerType.CRON, "default-name", "default-group", "默认触发器",
            null, null, true, null, null, -1, 0l,
            "*/20 * * * * ?", Trigger.MISFIRE_INSTRUCTION_SMART_POLICY);

    public static DefaultQuartzTriggerEnum findByCode(Integer code) {
        for (DefaultQuartzTriggerEnum triggerEnum : DefaultQuartzTriggerEnum.values()) {
            if (triggerEnum.code.equals(code)) {
                return triggerEnum;
            }
        }
        throw new IllegalArgumentException(" don't find this element.");
    }

    private Integer code;

    private QuartzTrigger.TriggerType type = QuartzTrigger.TriggerType.CRON; //默认为simple级别

    private String name;

    private String groupName;

    private String description;

    private String calendarName;

    private Date startTime;

    private boolean startNow;

    private Date endTime;

    private Map<String, Object> jobData;

    private int repeatCount;

    /**
     * 重复时间间隔,单位:ms
     */
    private long repeatInterval;

    private String cronExpression;

    private int misfireInstruction = Trigger.MISFIRE_INSTRUCTION_SMART_POLICY;

    public Integer getCode() {
        return code;
    }

    public QuartzTrigger.TriggerType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDescription() {
        return description;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public boolean isStartNow() {
        return startNow;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Map<String, Object> getJobData() {
        return jobData;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public int getMisfireInstruction() {
        return misfireInstruction;
    }

    DefaultQuartzTriggerEnum(Integer code, QuartzTrigger.TriggerType type, String name, String groupName,
                             String description, String calendarName, Date startTime, boolean startNow,
                             Date endTime, Map<String, Object> jobData, int repeatCount, long repeatInterval,
                             String cronExpression, int misfireInstruction) {
        this.code = code;
        this.type = type;
        this.name = name;
        this.groupName = groupName;
        this.description = description;
        this.calendarName = calendarName;
        this.startTime = startTime;
        this.startNow = startNow;
        this.endTime = endTime;
        this.jobData = jobData;
        this.repeatCount = repeatCount;
        this.repeatInterval = repeatInterval;
        this.cronExpression = cronExpression;
        this.misfireInstruction = misfireInstruction;
    }
}
