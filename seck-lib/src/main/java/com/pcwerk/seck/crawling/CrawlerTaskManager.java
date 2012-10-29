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

	private final long POLITE_INTERVAL = 5000l;
	private final long MAX_TIMEOUT = 30000l;

	private final int maxThreadCount;

	private Queue<ParsedDocument> frontier;

	public Queue<ParsedDocument> getFrontier() {
		return frontier;
	}

	ScheduledExecutorService crawlerScheduler;

	public CrawlerTaskManager(final int maxThreadCount) {
		this.maxThreadCount = maxThreadCount;
		this.frontier = new PriorityBlockingQueue<ParsedDocument>();
		this.crawlerScheduler = new ScheduledThreadPoolExecutor(maxThreadCount);
	}

	public void start(Set<String> seedUrls) {

		// Initialize frontier
		for (String url : seedUrls) {
			try {
				frontier.add(new ParsedDocument(url));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
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
					System.out.println("No more sites left in frontier. Spawning of crawler threads stopped.");
					return;
				}

				if (timeoutCounter > POLITE_INTERVAL) {
					if (currentThreadCount < maxThreadCount && !frontier.isEmpty()) {
						timeoutStart = System.currentTimeMillis();
						System.out.println(frontier.size());
						ParsedDocument newDoc = frontier.poll();
						
						if (newDoc == null) {
							continue;
						}

						crawlerScheduler.schedule(new CrawlerTask(newDoc, this),
								POLITE_INTERVAL, TimeUnit.MILLISECONDS);

					}
				}
			}
		}
	}
}
