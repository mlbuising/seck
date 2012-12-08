package com.pcwerk.seck.classification.categorized.hierarchy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DirectoryCrawlerTaskManager {
	public static int currentThreadCount = 0;

	private final long POLITE_INTERVAL = 500l;
	private final long MAX_TIMEOUT = 5000l;

	private final int MAX_THREAD_COUNT;
	private final int MAX_DEPTH;

	private final int MAX_BUFFER_SIZE = 50;

	private boolean hasAvailableThreads;

	private Queue<ParsedDocument> frontier;
	private Set<String> seenUrls;

	private URL rootUrl;
	
	private ScheduledExecutorService crawlerScheduler;

	public DirectoryCrawlerTaskManager(final int maxThreadCount,
			final int maxDepth) {
		this.MAX_THREAD_COUNT = maxThreadCount;
		this.MAX_DEPTH = maxDepth;
		this.hasAvailableThreads = true;
		this.frontier = new PriorityBlockingQueue<ParsedDocument>();
		this.seenUrls = Collections.synchronizedSet(new HashSet<String>());
		this.crawlerScheduler = new ScheduledThreadPoolExecutor(maxThreadCount);
	}

	public ScheduledExecutorService getCrawlerScheduler() {
		return crawlerScheduler;
	}

	public Queue<ParsedDocument> getFrontier() {
		return frontier;
	}

	public Set<String> getSeenUrls() {
		return seenUrls;
	}

	public int getMaxDepth() {
		return MAX_DEPTH;
	}

	public URL getRootUrl() {
		return rootUrl;
	}

	public void start(String rootUrl) throws MalformedURLException {

		// Initialize frontier
		this.rootUrl = new URL(rootUrl);
		
		try {
			frontier.add(new ParsedDocument(rootUrl, 0));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		synchronized (this) {

			long timeoutStart = System.currentTimeMillis();
			long timeoutEnd = 0;
			long timeoutCounter = 0;

			while (true) {
				timeoutEnd = System.currentTimeMillis();
				timeoutCounter = timeoutEnd - timeoutStart;

				if (timeoutCounter > MAX_TIMEOUT) {
					System.out.println("[i]   Frontier size: " + (frontier.size()));
					System.out
							.println("[i]   No more documents left in frontier. Spawning of crawler threads stopped.");
					System.out.println("[i]   Currently active thread count: "
							+ currentThreadCount);
					System.out.println("Total seen URLs: " + seenUrls.size());
					crawlerScheduler.shutdown();
					return;
				}

				// Create crawler threads at specified politeness interval
				if (currentThreadCount >= MAX_THREAD_COUNT) {
					this.hasAvailableThreads = false;
				} else {
					this.hasAvailableThreads = true;
				}

				if (timeoutCounter > POLITE_INTERVAL) {
					// Create crawlers threads from available pool
					while (hasAvailableThreads && !frontier.isEmpty()) {
						timeoutStart = System.currentTimeMillis();
						System.out.println("[i]   Frontier size: " + frontier.size());
						ParsedDocument newDoc = frontier.poll();
						if (newDoc == null) {
							continue;
						}

						crawlerScheduler.schedule(new DirectoryCrawlerTask(newDoc, this),
								POLITE_INTERVAL, TimeUnit.MILLISECONDS);

						System.out.println("[i]   Currently active thread count: "
								+ (++currentThreadCount));

						if (currentThreadCount >= MAX_THREAD_COUNT) {
							this.hasAvailableThreads = false;
						}
					}
				}
			}
		}
	}
}
