package com.sanm.domain;

import com.sanm.AbstractQuartzJobBean;
import org.quartz.JobKey;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Author: Sanm
 * description: 定时任务实体
 **/
public class QuartzJob implements Serializable {

    private static final long serialVersionUID = -2116518270025568628L;

    //任务名称 任务组
    private JobKey key;

    private String description;

    private Class<? extends AbstractQuartzJobBean> jobClass;

    private Map<String, Object> jobDataMap;

    //重启应用后是否删除任务的相关信息 默认为false
    private boolean volatility = false;

    //Job执行完后是否继续持久化到数据库，默认为false
    private boolean durability = false;

    // 服务器重启后是否忽略过期的任务，默认为false
    private boolean shouldRecover = false;

    private Set<QuartzTrigger> triggers;

    public void addTrigger(QuartzTrigger quartzTrigger) {
        if (quartzTrigger != null) {
            if (this.triggers == null) {
                this.triggers = new HashSet<>();
            }
            this.triggers.add(quartzTrigger);
        }
    }

    public JobKey getKey() {
        return key;
    }

    public void setKey(JobKey key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class<? extends AbstractQuartzJobBean> getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class<? extends AbstractQuartzJobBean> jobClass) {
        this.jobClass = jobClass;
    }

    public Map<String, Object> getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(Map<String, Object> jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    public boolean isVolatility() {
        return volatility;
    }

    public void setVolatility(boolean volatility) {
        this.volatility = volatility;
    }

    public boolean isDurability() {
        return durability;
    }

    public void setDurability(boolean durability) {
        this.durability = durability;
    }

    public boolean isShouldRecover() {
        return shouldRecover;
    }

    public void setShouldRecover(boolean shouldRecover) {
        this.shouldRecover = shouldRecover;
    }

    public Set<QuartzTrigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(Set<QuartzTrigger> triggers) {
        this.triggers = triggers;
    }

    @Override
    public String toString() {
        return "QuartzJob{" +
                "key=" + key +
                ", description='" + description + '\'' +
                ", jobClass=" + jobClass +
                ", jobDataMap=" + jobDataMap +
                ", volatility=" + volatility +
                ", durability=" + durability +
                ", shouldRecover=" + shouldRecover +
                ", triggers=" + triggers +
                '}';
    }
}
