package com.sanm.utils;

import com.sanm.base.QueueBaseKey;
import com.sanm.context.RedisDelayQueueContext;

/**
 * Author: Sanm
 * since: v1.0
 * description:
 **/
public class BaseUtil {

    // 加上项目名称 加入 [] 中
    public static String getProjectName() {
        return "[".concat(RedisDelayQueueContext.PROJECT_NAME).concat("]");
    }


    //获取REDIS_BASE_LIST的key前缀
    public static String getTitleListPreKey() {
        return getProjectName().concat(":").concat(QueueBaseKey.REDIS_BASE_LIST);
    }

    //获取REDIS_BASE_LIST中某个title的key
    public static String getTitleListKey(String title) {
        return getTitleListPreKey().concat(title);
    }

    // 根据member获取title
    public static String getTopicKeyByMember(String member) {
        String[] s = member.split(":");
        return s[0];
    }

    //拼接TITLE:ID
    public static String getTitleId(String title, String id) {
        return title.concat(":").concat(id);
    }

    //从member中解析出TitleList的key
    public static String getTitleListKeyByMember(String member) {
        return BaseUtil.getTitleListKey(getTitleListKeyByMember(member));
    }

    //获取延时队列ZSET的key
    public static String getBucketKey() {
        return getProjectName().concat(":").concat(QueueBaseKey.REDIS_BASE_ZSET);
    }

    //获取所有TASK数据的hash的key
    public static String getDelayQueueTableKey() {
        return getProjectName().concat(":").concat(QueueBaseKey.REDIS_BASE_TABLE);
    }

}
