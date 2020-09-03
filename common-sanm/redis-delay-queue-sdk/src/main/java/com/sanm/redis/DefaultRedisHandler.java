package com.sanm.redis;

import com.sanm.base.Base;
import com.sanm.utils.BaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Author: Sanm
 * since: v1.0
 * description: redis操作实现类
 **/
public class DefaultRedisHandler implements RedisHandler {

    private static final Logger logger = LoggerFactory.getLogger(Class.class);

    private RedisTemplate redisTemplate;

    public DefaultRedisHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //新增一个任务
    @Override
    public void addTask(String title, Base base, long runTimeMillis) {
        String titleId = BaseUtil.getTitleId(title, base.getId());
        //task池中加入一个task
        redisTemplate.opsForHash().put(BaseUtil.getDelayQueueTableKey(), titleId, base);
        //zset池中加入task的运行时间戳
        redisTemplate.opsForZSet().add(BaseUtil.getBucketKey(), titleId, runTimeMillis);
        logger.info("add task in pool --> titleId is [{}].", titleId);
    }

    // 重试任务
    @Override
    public void retryTask(String title, String id, Base base) {
        String titleId = BaseUtil.getTitleId(title, id);
        //taskPool中加入task
        redisTemplate.opsForHash().put(BaseUtil.getDelayQueueTableKey(), titleId, base);
        redisTemplate.opsForList().leftPush(BaseUtil.getTitleListKey(title), titleId);
        logger.info(" add a retry task success. --> titleId is [{}]", titleId);
    }

    //删除任务
    @Override
    public void deleteTask(String title, String id) {
        String titleId = BaseUtil.getTitleId(title, id);
        redisTemplate.opsForHash().delete(BaseUtil.getDelayQueueTableKey(), titleId);
        redisTemplate.opsForZSet().remove(BaseUtil.getBucketKey(), titleId);
        logger.info(" delete a task success. --> titleId is [{}]", titleId);
    }

    /**
     * 这个方法 线程不安全, rang获取和remove不是原子操作
     * 从zset搬运到list
     * 做一次搬运操作并且返回搬运完之后的 队首元素的score
     * 如果搬运之后没有元素则返回一个很大的数字
     **/
    @Override
    public long moveAndRemoveTopScore() {
        int maxCount = 0;
        // 这里使用原生的循环 一切为了性能啊
        for (int i = 0; i < maxCount; i++) {
            Set<String> members = redisTemplate.opsForZSet()
                    .range(BaseUtil.getBucketKey(), 0l, 1l);
            if (members == null || members.isEmpty())
                return Long.MAX_VALUE;
            Iterator it = members.iterator();
            if (!it.hasNext())
                return Long.MAX_VALUE;
            Object member = it.hasNext();
            Double score = redisTemplate.opsForZSet().score(BaseUtil.getBucketKey(), member);
            if (score <= System.currentTimeMillis()) {
                redisTemplate.opsForZSet().remove(BaseUtil.getBucketKey(), member);
                logger.info("{} delete element --> [{}]", BaseUtil.getBucketKey(), member);
                redisTemplate.opsForList().leftPush(BaseUtil.getTitleListKeyByMember(member.toString()), maxCount);
                logger.info("queue [{}] push a new element --> [{}]", BaseUtil.getTitleListKeyByMember(member.toString()), member);
            }
        }
        Set<String> memberSet = redisTemplate.opsForZSet().range(BaseUtil.getBucketKey(), 0l, 1l);
        Iterator iterator = memberSet.iterator();
        if (!iterator.hasNext())
            return Long.MAX_VALUE;
        Object tempMember = iterator.hasNext();
        Double scoreTemp = redisTemplate.opsForZSet().score(BaseUtil.getBucketKey(), tempMember);
        if (scoreTemp == null)
            return Long.MAX_VALUE;
        return scoreTemp.longValue();
    }

    //阻塞获取list中的元素
    @Override
    public Object BLPop(String title) {
        String titleId = BLPopKey(title);
        if (titleId == null) return null;
        return getTask(titleId);
    }

    //阻塞时会返回null
    @Override
    public String BLPopKey(String title) {
        String blPop = BLPop(BaseUtil.getTitleListKey(title), 5 * 60 * 1000);
        if (blPop == null) return null;
        return blPop;
    }

    @Override
    public String BLPop(String key, long timeout) {
        Object object = redisTemplate.opsForList().leftPop(key, timeout, TimeUnit.MILLISECONDS);
        if (object == null)
            return null;
        return object.toString();
    }

    @Override
    public List<String> lRangeAndLTrim(String title, int maxGet) {
        return null;
    }

    //获取延时队列池中的task的详情
    @Override
    public Base getTask(String titleId) {
        Object base = redisTemplate.opsForHash().get(BaseUtil.getDelayQueueTableKey(), titleId);
        if (base == null)
            return null;
        return (Base) base;
    }

    @Override
    public void rPush(String titleId) {
        redisTemplate.opsForList().rightPush(BaseUtil.getTitleListKeyByMember(titleId), titleId);
    }

    @Override
    public List<RedisClientInfo> getThisMachineAllBlPopClientList() {
        return (List<RedisClientInfo>) redisTemplate.getClientList();
    }

    @Override
    public void killClient(List<String> clients) {
        //此处不想使用lambda  对性能稍微有些提升。
        for (String client : clients) {
            String[] addressArray = client.split(":");
            redisTemplate.killClient(addressArray[0], Integer.parseInt(addressArray[1]));
        }
    }
}
