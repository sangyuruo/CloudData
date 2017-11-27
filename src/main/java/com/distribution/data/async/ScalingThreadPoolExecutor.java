package com.distribution.data.async;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ScalingThreadPoolExecutor extends ThreadPoolExecutor {
	/**
	 *
	 * number of threads that are actively executing tasks
	 */
	private final AtomicInteger activeCount = new AtomicInteger();

	public ScalingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	public int getActiveCount() {
		return activeCount.get();
	}

	public void execute(Runnable task, long startTimeout) {
		execute(task);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		activeCount.incrementAndGet();
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		activeCount.decrementAndGet();
	}
}
