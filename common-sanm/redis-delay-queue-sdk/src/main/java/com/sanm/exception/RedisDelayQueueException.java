package com.sanm.exception;

/**
 * Author: Sanm
 * since: v1.0
 * description: 延时队列异常
 **/
public class RedisDelayQueueException extends RuntimeException {

    public RedisDelayQueueException(String message) {
        super(message);
    }
}
