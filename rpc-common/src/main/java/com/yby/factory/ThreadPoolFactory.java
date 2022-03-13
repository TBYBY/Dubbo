package com.yby.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 创建 ThreadPool 的工具类
 */
public class ThreadPoolFactory {
    /**
     * 线程池参数
     * 三个基本参数
     * CORE_POOL_SIZE 核心线程数
     * MAXIMUM_POOL_SIZE 最大线程数
     * KEEP_ALIVE_TIME 当没有进入队列时的等待时间
     * WORK_QUEUE_CAPACITY 队列大小
     * Handler 饱和策略
     */
    private static final int CORE_POOL_SIZE = 10;
    private static final int MAXIMUM_POOL_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    private final static Logger logger = LoggerFactory.getLogger(ThreadPoolFactory.class);

    private static Map<String, ExecutorService> threadPollsMap = new ConcurrentHashMap<>();

    // 使用单例模式
    private ThreadPoolFactory() {
    }

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix){
        return createDefaultThreadPool(threadNamePrefix, false);
    }

    public static ExecutorService createDefaultThreadPool(String threadNamePrefix, Boolean daemon){
        ExecutorService pool = threadPollsMap.computeIfAbsent(threadNamePrefix, k -> createDefaultThreadPool(threadNamePrefix, daemon));
        if(pool.isShutdown() || pool.isTerminated()){
            threadPollsMap.remove(threadNamePrefix);
            pool = createDefaultThreadPool(threadNamePrefix, daemon);
            threadPollsMap.put(threadNamePrefix, pool);
        }
        return pool;
    }

    /**
     * 关闭英雄池
     */
    public static void shutDownAll(){
        logger.info("关闭所有线程池...");
        threadPollsMap.entrySet().parallelStream().forEach(entry ->{
            ExecutorService executorService = entry.getValue();
            executorService.shutdown();
            logger.info("关闭线程池 [{}] [{}]", entry.getKey(), executorService.isTerminated());
            try{
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error("关闭线程池失败！");
                executorService.shutdown();
            }
        });
    }

    /**
     * 创建线程池
     */
    private static ExecutorService createThreadPool(String threadNamePrefix, Boolean daemon){
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = createThreadFactory(threadNamePrefix, daemon);
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MINUTES, workQueue, threadFactory);
    }

    /**
     * 创建 ThreadFactory。如果 threadNamePrefix 不为空则需要自建 ThreadFactory，否则使用 defaultThreadFactory
     *
     */
    private static ThreadFactory createThreadFactory(String threadNamePrefix, Boolean daemon){
        if(threadNamePrefix != null){
            if(daemon != null){
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix + "-%d").setDaemon(daemon).build();
            }else{
                return new ThreadFactoryBuilder().setNameFormat(threadNamePrefix +"-%d").build();
            }
        }
        return Executors.defaultThreadFactory();
    }

}
