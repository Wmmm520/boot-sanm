package com.sanm.core;

import com.sanm.base.Base;

/**
 * Author: Sanm
 * since: v1.0
 * description: 回调
 **/
public interface CallBack<T extends Base> {

    //执行回调接口
    void execute(T t);

    /**
     * 重试超过2次(总共3次)回调接口;
     * 消费者可以在这个方法里面发送钉钉警告邮件警告等等
     * 回调这个接口是一个单独的线程
     */
    void retryOutTime(T t);
}
