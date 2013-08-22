import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Based on Chapter 10: Item 69 from Effective Java, 2nd Ed. by Joshua Bloch
 */
public class ConcurrentTimer {

	static class MyRunnable implements Runnable {

		final Algorithm algorithm;
		final int[] source;
		int[] target;

		public MyRunnable(final int rowCount) throws Exception {
			algorithm = new OriginalSort();
			source = MainHarness.readSource(rowCount);
		}

		@Override
		public void run() {
			target = algorithm.go(source);
		}
	}

	public static void main(String[] args) throws Exception {
		int concurrency = 4;
		int rowCount = 100;
		ExecutorService executor = Executors.newFixedThreadPool(concurrency);
		MyRunnable action = new MyRunnable(rowCount);
		long duration = time(executor, concurrency, action);
		System.out.println(String.format("%s", duration));
		IndexValue[] ivs = new IndexValue[rowCount];
		for (int i = 0; i < rowCount; i++) {
			ivs[i] = new IndexValue(i, action.source);
		}
		Arrays.sort(ivs);
		for (int j = 0; j < rowCount; j++) {
			IndexValue iv = ivs[j];
			System.out.println(String.format("%s %s", action.target[iv.index], iv.getValue()));
		}
		executor.shutdown();
	}

	// Simple framework for timing concurrent execution
	public static long time(Executor executor, int concurrency, final Runnable action) throws InterruptedException {
		final CountDownLatch ready = new CountDownLatch(concurrency);
		final CountDownLatch start = new CountDownLatch(1);
		final CountDownLatch done = new CountDownLatch(concurrency);
		for (int i = 0; i < concurrency; i++) {
			executor.execute(new Runnable() {
				public void run() {
					ready.countDown(); // Tell timer we're ready
					try {
						start.await(); // Wait till peers are ready
						action.run();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} finally {
						done.countDown(); // Tell timer we're done
					}
				}
			});
		}
		ready.await(); // Wait for all workers to be ready
		long startNanos = System.nanoTime();
		start.countDown(); // And they're off!
		done.await(); // Wait for all workers to finish
		return System.nanoTime() - startNanos;
	}

}
