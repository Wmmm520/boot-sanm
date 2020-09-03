package com.sanm.base;

import java.io.Serializable;

/**
 * Author: Sanm
 * since: v1.0
 * description: 基础入参信息
 **/
public class Base implements Serializable {

    private static final long serialVersionUID = 999999L;

    /**
     * 唯一主键,不能为空且不能重复
     */
    private String id;

    /**
     * 已经重试的次数:
     * 默认为2次,最多三次
     * 在添加任务的时候,设置为0，表示不希望重试
     */
    private int retryCount;

    /**
     * 重入次数
     * 标记是 当前异常情况导致并没有消费到,然后重新进入消费池子中的次数
     * 当次数大于3次时,则丢弃。
     */
    private int reEntryCount;


    public Base() {
    }

    public Base(String id) {
        this.id = id;
    }

    public Base(String id, int retryCount) {
        this.id = id;
        this.retryCount = retryCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getReEntryCount() {
        return reEntryCount;
    }

    public void setReEntryCount(int reEntryCount) {
        this.reEntryCount = reEntryCount;
    }

    @Override
    public String toString() {
        return "Base{" +
                "id='" + id + '\'' +
                ", retryCount=" + retryCount +
                ", reEntryCount=" + reEntryCount +
                '}';
    }
}
