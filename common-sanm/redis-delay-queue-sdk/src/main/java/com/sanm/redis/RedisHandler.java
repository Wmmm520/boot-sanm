package com.sanm.redis;

import com.sanm.base.Base;
import org.springframework.data.redis.core.types.RedisClientInfo;

import java.util.List;

/**
 * Author: Sanm
 * since: v1.0
 * description: redis的具体操作
 **/
public interface RedisHandler {

    /**
     * 新增一个任务
     *
     * @param title         标题
     * @param base          基础信息
     * @param runTimeMillis 执行时间戳
     */
    void addTask(String title, Base base, long runTimeMillis);


    /**
     * 新增一个重试任务
     *
     * @param title 标题
     * @param id    唯一标识
     * @param t     内容
     */
    void retryTask(String title, String id, Base base);

    /**
     * 删除一个任务
     *
     * @param title 标题
     * @param id    唯一标识
     */
    void deleteTask(String title, String id);

    /**
     * 搬运操作
     * 从待搬运zset中搬运元素到 待消费队列list中
     *
     * @return 搬运完成之后, 再返回zset中的队首元素的时间戳;
     * 如果zset没有元素了则返回  Long.MAXVALUE;
     */
    long moveAndRemoveTopScore();


    /**
     * 阻塞获取 待消费队列title_List中的元素;
     * 阻塞有超时时间; 如果超时则会返回Null
     *
     * @param title 标题
     * @return
     */
    Object BLPop(String title);

    /**
     * 阻塞获取元素
     * 1之内没有获取到数据则断开连接
     *
     * @param title 标题
     * @return
     */
    String BLPopKey(String title);

    String BLPop(String key, long timeout);

    /**
     * 获取最多maxGet元素 ;并且将这些元素删除
     *
     * @param title  标题
     * @param maxGet
     * @return
     */
    List<String> lRangeAndLTrim(String title, int maxGet);

    /**
     * 通过titleId获取Task内容
     *
     * @param titleId 标题Id
     * @return
     */
    Base getTask(String titleId);

    /**
     * 将元素push到队尾
     *
     * @param titleId 标题Id
     */
    void rPush(String titleId);


    /**
     * 获取所有redis的客户端
     *
     * @return
     */
    List<RedisClientInfo> getThisMachineAllBlPopClientList();


    /**
     * 杀掉指定客户端
     */
    void killClient(List<String> clients);
}
