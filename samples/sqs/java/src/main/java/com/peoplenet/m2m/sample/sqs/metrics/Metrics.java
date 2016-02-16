package com.peoplenet.m2m.sample.sqs.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import java.util.concurrent.TimeUnit;

/**
 * Created by dhreines on 2/15/16.
 */
public class Metrics {

	// Metrics
	private static final MetricRegistry metrics = new MetricRegistry();
	private static final Meter sent;
	private static final Meter received;

	static {
		sent = metrics.meter("sent");
		received = metrics.meter("received");

		ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
				.convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS)
				.build();
		reporter.start(10, TimeUnit.SECONDS);
	}

	public void sent(int count) {
		sent.mark(count);
	}

	public void received() {
		received.mark();
	}

}
