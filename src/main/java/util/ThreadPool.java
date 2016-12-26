package util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public enum ThreadPool {

	INSTANCE;

	private final static int MAX_REQUSTS_NUMBER = 5;
	private final ExecutorService pool = Executors.newFixedThreadPool(MAX_REQUSTS_NUMBER);

	public <T> Future<T> submit(Callable<T> callable) {
		return pool.submit(callable);
	}

}
