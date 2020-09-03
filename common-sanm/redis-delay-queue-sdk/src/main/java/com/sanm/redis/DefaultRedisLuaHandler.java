package com.sanm.redis;

import com.google.common.collect.Lists;
import com.sanm.base.Base;
import com.sanm.utils.ExceptionUtil;
import com.sanm.utils.BaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Sanm
 * since: v1.0
 * description: redis操作lua脚本
 **/
public class DefaultRedisLuaHandler extends DefaultRedisHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRedisLuaHandler.class);

    private RedisTemplate redisTemplate;

    public DefaultRedisLuaHandler(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    //新增一个任务
    @Override
    public void addTask(String title, Base base, long runTimeMillis) {
        List<String> keys = Lists.newArrayList();
        keys.add(BaseUtil.getDelayQueueTableKey());
        keys.add(BaseUtil.getBucketKey());
        DefaultRedisScript redisScript = new DefaultRedisScript();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/addJob.lua")));
        redisTemplate.execute(redisScript, keys, BaseUtil.getTitleId(title, base.getId()));
        logger.info("add task in pool --> base is [{}].", base.toString());
    }

    //获取延时队列池中的task的详情
    @Override
    public Base getTask(String titleId) {
        List<String> keys = new ArrayList<>(1);
        keys.add(BaseUtil.getDelayQueueTableKey());
        DefaultRedisScript<Base> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Base.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/getJob.lua")));
        Object base = redisTemplate.execute(redisScript, keys, titleId);
        if (base == null)
            return null;
        logger.info("getTask --> get info [{}]", titleId);
        return (Base) base;
    }


    // 重试任务
    @Override
    public void retryTask(String title, String id, Base base) {
        List<String> keys = Lists.newArrayList();
        keys.add(BaseUtil.getDelayQueueTableKey());
        keys.add(BaseUtil.getTitleListKey(title));
        DefaultRedisScript redisScript = new DefaultRedisScript();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/retryJob.lua")));
        logger.info(" add a retry task success. --> titleId is [{}]", title);
    }

    //删除任务
    @Override
    public void deleteTask(String title, String id) {
        List<String> keys = Lists.newArrayList();
        keys.add(BaseUtil.getDelayQueueTableKey());
        keys.add(BaseUtil.getBucketKey());
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/deleteJob.lua")));
        redisTemplate.execute(redisScript, keys, BaseUtil.getTitleId(title, id));
        logger.info(" delete a task success. --> title is [{}]", title);
    }

    /**
     * 这个方法 线程安全, rang获取和remove不是原子操作
     **/
    @Override
    public long moveAndRemoveTopScore() {
        List<String> keys = new ArrayList<>(2);
        //移动到的待消费列表key  这里是前缀: 在lua脚本会解析真正的topic
        keys.add(BaseUtil.getTitleListPreKey());
        //被移动的zset
        keys.add(BaseUtil.getBucketKey());
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(String.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/moveAndRtTopScore.lua")));
        String newTime = (String) redisTemplate.execute(redisScript, redisTemplate.getValueSerializer(),
                redisTemplate.getStringSerializer(), keys, System.currentTimeMillis());
        if (StringUtils.isEmpty(newTime)) return Long.MAX_VALUE;
        return Long.parseLong(newTime);
    }


    @Override
    public List<String> lRangeAndLTrim(String title, int maxGet) {
        //lua 是以0开始为
        maxGet = maxGet - 1;
        List<String> keys = new ArrayList<>(1);
        //移动到的待消费列表key
        keys.add(BaseUtil.getTitleListKey(title));
        DefaultRedisScript<Object> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Object.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/lrangAndLTrim.lua")));
        Object values;
        try {
            values = redisTemplate.execute(redisScript, redisTemplate.getValueSerializer(), redisTemplate.getStringSerializer(), keys, maxGet);
        } catch (RedisSystemException e) {
            if (e.getCause() instanceof NullPointerException) {
                return null;
            } else {
                logger.error("lRangeAndLTrim 操作异常;{}", ExceptionUtil.getStackTrace(e));
                throw e;
            }
        }
        return (List<String>) values;
    }


}
