package com.sanm.core;

import com.sanm.base.Base;
import com.sanm.base.RunTypeEnum;

/**
 * Author: Sanm
 * since: v1.0
 * description: 提供给客户端使用
 **/
public interface RedisDelayQueue {

    /**
     * 异步新增一个延迟任务
     *
     * @param base            基本信息
     * @param title           话题
     * @param delayTimeMillis 延时时间 单位: 毫秒
     */
    void addAsync(Base base, String title, long delayTimeMillis);

    /**
     * 新增一个延迟任务
     *
     * @param base          用户入参
     * @param topic         话题
     * @param runTimeMillis 执行时间 单位: 毫秒
     */
    void add(Base base, String topic, long runTimeMillis, RunTypeEnum runTypeEnum);


    /**
     * 新增一个延迟任务
     *
     * @param base            入参信息
     * @param delayTimeMillis 需要延迟的时间:  单位: 毫秒
     * @param title           话题
     */
    void add(Base base, long delayTimeMillis, String title, RunTypeEnum runTypeEnum);

    /**
     * 删除一个延迟队列
     *
     * @param title 话题
     * @param id    主键
     */
    void delete(String title, String id, RunTypeEnum runTypeEnum);

    /**
     * 异步删除一个延迟队列
     *
     * @param title 话题
     * @param id    主键
     */
    void deleteAsync(String title, String id);


}
