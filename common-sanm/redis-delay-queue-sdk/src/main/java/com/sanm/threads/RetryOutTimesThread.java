package com.sanm.threads;

import com.sanm.base.Base;
import com.sanm.core.process.AbstractTitleRegister;
import com.sanm.redis.RedisHandler;
import com.sanm.utils.BaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Sanm
 * since: v1.0
 * description: 失败重试线程
 **/
public class RetryOutTimesThread {

    private static final Logger logger = LoggerFactory.getLogger(RetryOutTimesThread.class);

    private RetryOutTimesThread() {

    }

    private static RetryOutTimesThread instance = new RetryOutTimesThread();

    //静态内部类单例模式
    public static RetryOutTimesThread getInstance() {
        return instance;
    }

    private static ExecutorService NOTIFY_RETRY_OUT_TIME = Executors.newCachedThreadPool();


    public void callBackExceptionTryRetry(AbstractTitleRegister register, Base base, RedisHandler redisHandler) {
        if (base.getRetryCount() > 1) {
            //重试了2次了,不再重试了; 异步执行回调通知接口;
            NOTIFY_RETRY_OUT_TIME.execute(() -> register.retryOutTime(base));
        } else if (base.getRetryCount() >= 0) {
            base.setRetryCount(base.getRetryCount() + 1);
            redisHandler.retryTask(register.getTopic(), base.getId(), base);
            logger.warn("失败任务第{}次放入重试:{}:topicId:{},Args:{}", base.getRetryCount(),
                    BaseUtil.getTitleId(register.getTopic(), base.getId()), base);
        }
    }

    //停机
    public void toStop() {
        ShutdownThread.closeExecutor(NOTIFY_RETRY_OUT_TIME, "重试任然失败通知线程池");
    }
}

