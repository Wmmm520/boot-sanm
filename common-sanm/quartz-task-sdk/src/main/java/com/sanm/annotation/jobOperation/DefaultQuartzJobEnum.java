package com.sanm.annotation.jobOperation;

import com.sanm.AbstractQuartzJobBean;
import com.sanm.defaults.DefaultJob;

import java.util.Map;

/**
 * Author: Sanm
 * since: v1.0
 * description:
 **/
public enum DefaultQuartzJobEnum {

    DEFAULT_QUARTZ_JOB_ENUM(-1, "default-name", "default-group", "这是默认定时任务", DefaultJob.class,
            null, true, true, false);


    private Integer code;

    //任务名称 任务组
    private String name;

    private String groupName;

    private String description;

    private Class<? extends AbstractQuartzJobBean> jobClass;

    private Map<String, Object> jobDataMap;

    //重启应用后是否删除任务的相关信息 默认为false
    private boolean volatility = false;

    //Job执行完后是否继续持久化到数据库，默认为false
    private boolean durability = false;

    // 服务器重启后是否忽略过期的任务，默认为false
    private boolean shouldRecover = false;


    public static DefaultQuartzJobEnum findByCode(Integer code) {
        for (DefaultQuartzJobEnum quartzJobEnum : DefaultQuartzJobEnum.values()) {
            if (quartzJobEnum.code.equals(code)) {
                return quartzJobEnum;
            }
        }
        throw new IllegalArgumentException(" don't find this element.");
    }


    DefaultQuartzJobEnum(Integer code, String name, String groupName, String description, Class<? extends AbstractQuartzJobBean> jobClass
            , Map<String, Object> jobDataMap, boolean volatility, boolean durability, boolean shouldRecover) {
        this.code = code;
        this.name = name;
        this.groupName = groupName;
        this.description = description;
        this.jobClass = jobClass;
        this.jobDataMap = jobDataMap;
        this.volatility = volatility;
        this.durability = durability;
        this.shouldRecover = shouldRecover;
    }

    public Integer getCode() {
        return code;
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

    public Class<? extends AbstractQuartzJobBean> getJobClass() {
        return jobClass;
    }

    public Map<String, Object> getJobDataMap() {
        return jobDataMap;
    }

    public boolean isVolatility() {
        return volatility;
    }

    public boolean isDurability() {
        return durability;
    }

    public boolean isShouldRecover() {
        return shouldRecover;
    }
}
