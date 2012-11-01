package com.pcwerk.seck.crawling;

import java.net.MalformedURLException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pcwerk.seck.crawling.entities.ParsedDocument;

public class CrawlerTaskManager {
	public static int currentThreadCount = 0;

	private final Logger LOGGER = LoggerFactory.getLogger("CrawlerTaskManager");

	private final long POLITE_INTERVAL = 500l;
	private final long MAX_TIMEOUT = 5000l;

	private final int maxThreadCount;
	private final int maxDepth;

	private boolean hasAvailableThreads;

	private Queue<ParsedDocument> frontier;

	private ScheduledExecutorService crawlerScheduler;

	public CrawlerTaskManager(final int maxThreadCount, final int maxDepth) {
		this.maxThreadCount = maxThreadCount;
		this.maxDepth = maxDepth;
		this.hasAvailableThreads = true;
		this.frontier = new PriorityBlockingQueue<ParsedDocument>();
		this.crawlerScheduler = new ScheduledThreadPoolExecutor(maxThreadCount);
	}

	public ScheduledExecutorService getCrawlerScheduler() {
		return crawlerScheduler;
	}

	public Queue<ParsedDocument> getFrontier() {
		return frontier;
	}

	public int getMaxDepth() {
		return maxDepth;
	}

	public void start(Set<String> seedUrls) {

		// Initialize frontier
		for (String url : seedUrls) {
			try {
				frontier.add(new ParsedDocument(url, 0));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
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
					crawlerScheduler.shutdown();
					return;
				}

				// Create crawler threads at specified politeness interval
				if (currentThreadCount >= maxThreadCount) {
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

						crawlerScheduler.schedule(new CrawlerTask(newDoc, this),
								POLITE_INTERVAL, TimeUnit.MILLISECONDS);

						System.out.println("[i]   Current actively thread count: "
								+ (++currentThreadCount));

						if (currentThreadCount >= maxThreadCount) {
							this.hasAvailableThreads = false;
						}
					}
				}
			}
		}
	}
}
