package com.sanm.utils;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * Author: Sanm
 * since: v1.0
 * description: 超时机制
 **/
public class TimeoutUtil {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void timeoutMethod(long timeout, Function function) throws InterruptedException, ExecutionException, TimeoutException {
        FutureTask futureTask = new FutureTask(()->(function.apply("")));
        executorService.execute(futureTask);
        //new Thread(futureTask).start();
        try {
            futureTask.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            //e.printStackTrace();
            futureTask.cancel(true);
            throw e;
        }

    }
}
