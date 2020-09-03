package com.sanm.base;

/**
 * Author: Sanm
 * since: v1.0
 * description: 延时队列全局参数
 **/
public interface QueueBaseKey {


    /**
     * hash结构  存储了所有的延时队列的信息 (可以使用数据库持久化,但是懒以及性能的问题  没有选择)
     * key: title:id  value: 信息内容
     */
    String REDIS_BASE_TABLE = "REDIS_BASE_TABLE";


    /**
     * 延迟队列的有序集合; 存放K=TITLE:ID 和需要的执行时间戳;
     * 根据时间戳排序;
     */
    String REDIS_BASE_ZSET = "REDIS_BASE_ZSET:";

    //  list  每个title一个list list存放当前需要被消费的job
    String REDIS_BASE_LIST = "REDIS_BASE_LIST:";

}
