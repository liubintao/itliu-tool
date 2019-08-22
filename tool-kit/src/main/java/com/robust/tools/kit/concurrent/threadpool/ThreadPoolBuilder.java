package com.robust.tools.kit.concurrent.threadpool;

import com.robust.tools.kit.base.Validate;
import com.robust.tools.kit.concurrent.TaskQueue;

import java.util.concurrent.*;

/**
 * @Description: ThreadPool创建的工具类
 * <p>
 * 对比JDK Executors中的newFixedThreadPool(),new CachedThreadPool(),new ScheduledThreadPool(),提供更多有用的配置项.
 * <p>
 * 另包含移植自Tomcat的QueuableCachedPool.
 * <p>
 * e.g
 *
 * <pre>
 *     ExecutorService ExecutorService = new FixedThreadPoolBuilder().setPoolSize(10).build();
 * </pre>
 * <p>
 * 参考文章 《Java ThreadPool的正确打开方式》http://calvin1978.blogcn.com/articles/java-threadpool.html
 * @Author: robust
 * @CreateDate: 2019/8/1 11:41
 * @Version: 1.0
 */
public class ThreadPoolBuilder {

    private static final RejectedExecutionHandler defaultRejectedHandler = new ThreadPoolExecutor.AbortPolicy();

    /**
     * @return 固定大小的线程池构建器
     * @see FixedThreadPoolBuilder
     */
    public static FixedThreadPoolBuilder fixedPool() {
        return new FixedThreadPoolBuilder();
    }

    /**
     * @return
     * @see CachedThreadPoolBuilder
     */
    public static CachedThreadPoolBuilder cachedPool() {
        return new CachedThreadPoolBuilder();
    }

    /**
     * @return 定时线程池构建器
     * @see ScheduledThreadPoolBuilder
     */
    public static ScheduledThreadPoolBuilder scheduledPool() {
        return new ScheduledThreadPoolBuilder();
    }

    /**
     * 可操作队列的缓存线程池构建器
     *
     * @return
     */
    public static QueuableCachedThreadPoolBuilder queuableCachedPool() {
        return new QueuableCachedThreadPoolBuilder();
    }

    /**
     * 创建FixedThreadPool,建议必须设置queueSize保证有界.
     * 1、任务提交时,如果线程数还没达到poolSize即创建新的线程并绑定任务(即poolSize次提交后线程总数必达到poolSize,不会重用之前的线程)
     * poolSize默认为1,即singleThreadPoll
     * 2、第poolSize次任务提交后,新增任务放入Queue中,pool中的所有线程从Queue中take任务执行.
     * Queue默认为无限长的LinkedBlockingQueue,但建议设置queueSize换成有界队列.
     * <p>
     * 如果使用有界队列,当队列满了之后,会调用RejectHandler进行处理,默认为AbortPolicy,抛出RejectedExecutionException异常.
     * 其他可选的policy包括静默放弃当前任务(Discard),放弃Queue里最老的任务(DiscardOldest),或由主线程来直接执行(CallerRuns).
     * <p>
     * 3、因为线程全部为core线程,所以不会在空闲时回收.
     */
    public static class FixedThreadPoolBuilder {
        private int poolSize = 1;
        private int queueSize = -1;

        private ThreadFactory threadFactory;
        private String threadNamePrefix;
        private Boolean daemon;

        private RejectedExecutionHandler rejectedHandler;

        /**
         * set the pool size, default 1, that is singleThreadPool
         *
         * @param poolSize
         * @return
         */
        public FixedThreadPoolBuilder setPoolSize(int poolSize) {
            Validate.isTrue(poolSize >= 1);
            this.poolSize = poolSize;
            return this;
        }

        /**
         * 不设置时为-1,使用不限长度的LinkedBlockingQueue.
         * 为正数时使用ArrayBlockingQueue.
         *
         * @param queueSize
         * @return
         */
        public FixedThreadPoolBuilder setQueueSize(int queueSize) {
            this.queueSize = queueSize;
            return this;
        }

        /**
         * 与threadNamePrefix互斥,优先使用threadFactory
         *
         * @param threadFactory
         * @return
         */
        public FixedThreadPoolBuilder setThreadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        /**
         * 与threadFactory互斥,优先使用threadFactory
         *
         * @param threadNamePrefix
         * @return
         */
        public FixedThreadPoolBuilder setThreadNamePrefix(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
            return this;
        }

        /**
         * 与threadFactory互斥,优先使用threadFactory
         * 默认为NULL,不进行设置,使用JDK的默认值.
         *
         * @param daemon
         * @return
         */
        public FixedThreadPoolBuilder setDaemon(Boolean daemon) {
            this.daemon = daemon;
            return this;
        }

        public FixedThreadPoolBuilder setRejectedHandler(RejectedExecutionHandler rejectedHandler) {
            this.rejectedHandler = rejectedHandler;
            return this;
        }

        public ThreadPoolExecutor build() {
            BlockingQueue<Runnable> queue =
                    this.queueSize < 1 ? new LinkedBlockingQueue<>() : new ArrayBlockingQueue<>(queueSize);

            threadFactory = createThreadFactory(threadFactory, threadNamePrefix, daemon);

            if (rejectedHandler == null) {
                rejectedHandler = defaultRejectedHandler;
            }
            return new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, queue,
                    threadFactory, rejectedHandler);
        }


    }

    /**
     * 创建CachedThreadPool, maxSize建议设置
     * <p>
     * 1. 任务提交时, 如果线程数还没达到minSize即创建新线程并绑定任务(即minSize次提交后线程总数必达到minSize, 不会重用之前的线程)
     * <p>
     * minSize默认为0, 可设置保证有基本的线程处理请求不被回收.
     * <p>
     * 2. 第minSize次任务提交后, 新增任务提交进SynchronousQueue后，如果没有空闲线程立刻处理，则会创建新的线程, 直到总线程数达到上限.
     * <p>
     * maxSize默认为Integer.Max, 可以进行设置.
     * <p>
     * 如果设置了maxSize, 当总线程数达到上限, 会调用RejectHandler进行处理, 默认为AbortPolicy, 抛出RejectedExecutionException异常.
     * 其他可选的Policy包括静默放弃当前任务(Discard)，或由主线程来直接执行(CallerRuns).
     * <p>
     * 3. minSize以上, maxSize以下的线程, 如果在keepAliveTime中都poll不到任务执行将会被结束掉, keepAliveTimeJDK默认为10秒.
     * JDK默认值60秒太高，如高达1000线程时，要低于16QPS时才会开始回收线程, 因此改为默认10秒.
     */
    public static class CachedThreadPoolBuilder {
        private int minSize = 0;
        private int maxSize = Integer.MAX_VALUE;
        private long keepAliveTime = 10;
        private TimeUnit timeUnit = TimeUnit.SECONDS;

        private ThreadFactory threadFactory;
        private String threadNamePrefix;
        private Boolean daemon;

        private RejectedExecutionHandler rejectedHandler;

        public CachedThreadPoolBuilder setMinSize(int minSize) {
            this.minSize = minSize;
            return this;
        }

        /**
         * Max默认Integer.MAX_VALUE的，建议设置
         *
         * @param maxSize
         * @return
         */
        public CachedThreadPoolBuilder setMaxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        /**
         * JDK默认值60秒太高，如高达1000线程时，要低于16QPS时才会开始回收线程, 因此改为默认10秒.
         *
         * @param keepAliveTime
         */
        public CachedThreadPoolBuilder setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
            return this;
        }

        /**
         * @param timeUnit 时间单位
         */
        public CachedThreadPoolBuilder setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        /**
         * 与threadNamePrefix互斥, 优先使用ThreadFactory
         */
        public CachedThreadPoolBuilder setThreadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        /**
         * 与ThreadFactory互斥, 优先使用ThreadFactory
         */
        public CachedThreadPoolBuilder setThreadNamePrefix(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
            return this;
        }

        /**
         * 与ThreadFactory互斥, 优先使用ThreadFactory
         */
        public CachedThreadPoolBuilder setDaemon(Boolean daemon) {
            this.daemon = daemon;
            return this;
        }

        public CachedThreadPoolBuilder setRejectedHandler(RejectedExecutionHandler rejectedHandler) {
            this.rejectedHandler = rejectedHandler;
            return this;
        }

        public ThreadPoolExecutor build() {
            threadFactory = createThreadFactory(threadFactory, threadNamePrefix, daemon);
            if (rejectedHandler == null) {
                rejectedHandler = defaultRejectedHandler;
            }
            return new ThreadPoolExecutor(minSize, maxSize, keepAliveTime, timeUnit,
                    new SynchronousQueue<>(), threadFactory, rejectedHandler);
        }
    }

    /**
     * 创建ScheduledThreadPool
     */
    public static class ScheduledThreadPoolBuilder {
        private int poolSize = 1;
        private ThreadFactory threadFactory;
        private String threadNamePrefix;

        /**
         * @param poolSize 默认为1
         * @return ScheduledThreadPoolBuilder
         */
        public ScheduledThreadPoolBuilder setPoolSize(int poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        /**
         * 与threadNamePrefix冲突,优先使用ThreadFactory
         *
         * @param threadFactory 线程工厂
         * @return ScheduledThreadPoolBuilder
         */
        public ScheduledThreadPoolBuilder setThreadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        /**
         * 与ThreadFactory冲突,优先使用ThreadFactory
         *
         * @param threadNamePrefix 线程名称前缀
         * @return ScheduledThreadPoolBuilder
         */
        public ScheduledThreadPoolBuilder setThreadNamePrefix(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
            return this;
        }

        public ScheduledThreadPoolExecutor build() {
            threadFactory = createThreadFactory(threadFactory, threadNamePrefix, Boolean.TRUE);
            return new ScheduledThreadPoolExecutor(poolSize, threadFactory);
        }

    }


    /**
     * 从Tomcat移植过来的可扩展可用Queue缓存任务的ThreadPool
     *
     * @see QueuableCachedThreadPool
     */
    public static class QueuableCachedThreadPoolBuilder {
        private int minSize = 0;
        private int maxSize = Integer.MAX_VALUE;
        private int keepAlive = 10;
        private TimeUnit timeUnit = TimeUnit.SECONDS;
        private int queueSize = 100;

        private ThreadFactory threadFactory;
        private String threadNamePrefix;
        private Boolean daemon;

        private RejectedExecutionHandler rejectedHandler;

        public QueuableCachedThreadPoolBuilder setMinSize(int minSize) {
            this.minSize = minSize;
            return this;
        }

        public QueuableCachedThreadPoolBuilder setMaxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        /**
         * LinkedQueue长度, 默认100
         */
        public QueuableCachedThreadPoolBuilder setQueueSize(int queueSize) {
            this.queueSize = queueSize;
            return this;
        }

        public QueuableCachedThreadPoolBuilder setKeepAlive(int keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        public QueuableCachedThreadPoolBuilder setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        /**
         * 与threadNamePrefix互斥, 优先使用ThreadFactory
         */
        public QueuableCachedThreadPoolBuilder setThreadFactory(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
            return this;
        }

        /**
         * 与threadFactory互斥, 优先使用ThreadFactory
         */
        public QueuableCachedThreadPoolBuilder setThreadNamePrefix(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
            return this;
        }

        /**
         * 与threadFactory互斥, 优先使用ThreadFactory
         * <p>
         * 默认为NULL，不进行设置，使用JDK的默认值.
         */
        public QueuableCachedThreadPoolBuilder setDaemon(Boolean daemon) {
            this.daemon = daemon;
            return this;
        }

        public QueuableCachedThreadPoolBuilder setRejectedHandler(RejectedExecutionHandler rejectHandler) {
            this.rejectedHandler = rejectHandler;
            return this;
        }

        public QueuableCachedThreadPool build() {
            threadFactory = createThreadFactory(threadFactory, threadNamePrefix, daemon);
            if (rejectedHandler == null) {
                rejectedHandler = defaultRejectedHandler;
            }
            return new QueuableCachedThreadPool(minSize, maxSize, keepAlive, timeUnit,
                    new TaskQueue(queueSize), threadFactory, rejectedHandler);
        }
    }

    /**
     * 优先使用threadFactory，否则如果threadNamePrefix不为空则使用自建ThreadFactory，否则使用defaultThreadFactory
     */
    private static ThreadFactory createThreadFactory(ThreadFactory threadFactory, String threadNamePrefix,
                                                     Boolean daemon) {
        if (threadFactory != null) {
            return threadFactory;
        }

        if (threadNamePrefix != null) {
            if (daemon != null) {
                return ThreadPoolUtil.buildThreadFactory(threadNamePrefix, daemon);
            } else {
                return ThreadPoolUtil.buildThreadFactory(threadNamePrefix);
            }
        }
        return Executors.defaultThreadFactory();
    }


}
