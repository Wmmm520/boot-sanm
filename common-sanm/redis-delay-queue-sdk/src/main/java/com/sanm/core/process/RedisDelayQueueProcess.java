package com.sanm.core.process;

import com.sanm.base.Base;
import com.sanm.base.RunTypeEnum;
import com.sanm.core.RedisDelayQueue;
import com.sanm.exception.RedisDelayQueueException;
import com.sanm.redis.RedisHandler;
import com.sanm.utils.NextTimeHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * Author: Sanm
 * since: v1.0
 * description: 提供给客户端使用的 延迟队列操作
 **/
public class RedisDelayQueueProcess implements RedisDelayQueue {


    private static final Logger logger = LoggerFactory.getLogger(RedisDelayQueueProcess.class);


    private RedisHandler redisHandler;
    private ConcurrentHashMap<String, AbstractTitleRegister> topicRegisterHolder;
    private ExecutorService executor;


    public RedisDelayQueueProcess(RedisHandler redisHandler, ConcurrentHashMap<String, AbstractTitleRegister> topicRegisterHolder, ExecutorService executor) {
        this.redisHandler = redisHandler;
        this.topicRegisterHolder = topicRegisterHolder;
        this.executor = executor;
    }

    @Override
    public void addAsync(Base base, String title, long delayTimeMillis) {
        add(base, delayTimeMillis, title, RunTypeEnum.ASYNC);
    }

    @Override
    public void add(Base base, String title, long runTimeMillis, RunTypeEnum runTypeEnum) {
        if (runTypeEnum == RunTypeEnum.ASYNC) {
            executor.execute(() -> addTask(base, title, runTimeMillis));
        } else {
            addTask(base, title, runTimeMillis);
        }
    }

    @Override
    public void add(Base base, long delayTimeMillis, String title, RunTypeEnum runTypeEnum) {
        if (runTypeEnum == RunTypeEnum.ASYNC) {
            executor.execute(() -> addTask(base, delayTimeMillis, title));
        } else {
            addTask(base, delayTimeMillis, title);
        }
    }

    @Override
    public void delete(String title, String id, RunTypeEnum runTypeEnum) {
        if (runTypeEnum == RunTypeEnum.ASYNC) {
            executor.execute(() -> redisHandler.deleteTask(title, id));
        } else {
            redisHandler.deleteTask(title, id);
        }
        logger.info("delete a delay task: Title:{}, id：{}", title, id);
    }

    @Override
    public void deleteAsync(String title, String id) {
        delete(title, id, RunTypeEnum.ASYNC);
    }

    private void addTask(Base base, long delayTimeMillis, String title) {
        preCheck(base, title, null, delayTimeMillis);
        long runTimeMillis = System.currentTimeMillis() + delayTimeMillis;
        redisHandler.addTask(title, base, runTimeMillis);
        //尝试更新下次的执行时间
        NextTimeHolder.tryUpdate(runTimeMillis);
    }

    private void addTask(Base base, String title, long runTimeMillis) {
        preCheck(base, title, runTimeMillis, null);
        redisHandler.addTask(title, base, runTimeMillis);
        //尝试更新下次的执行时间
        NextTimeHolder.tryUpdate(runTimeMillis);
    }

    private void preCheck(Base base, String title, Long runTimeMillis, Long delayTimeMillis) {
        if (checkStringEmpty(title) ||
                checkStringEmpty(base.getId())) {
            throw new RedisDelayQueueException("don't title or id");
        }
        if (runTimeMillis == null) {
            if (delayTimeMillis == null) {
                throw new RedisDelayQueueException("don't set delay queue!");
            }
        }
        if (title.contains(":")) {
            throw new RedisDelayQueueException("title can't contain :  !");
        }
        //check topic exist
        if (!checkTopicExist(title)) {
            throw new RedisDelayQueueException("title not register.");
        }
    }

    private boolean checkStringEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public boolean checkTopicExist(String topic) {
        for (Map.Entry<String, AbstractTitleRegister> entry : topicRegisterHolder.entrySet()) {
            if (entry.getKey().equals(topic)) {
                return true;
            }
        }
        return false;
    }

}
