package com.distribution.data.collector.error;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public interface RetryLevel {
	public final static int DELAY_ONE_MINUTE = 1 * 60 * 1000;
	public final static int DELAY_FIVE_MINUTES = DELAY_ONE_MINUTE * 5;
	public final static int DELAY_THIRTY_MINUTES = DELAY_ONE_MINUTE * 30;
	public final static int DELAY_ONE_HOUR = DELAY_ONE_MINUTE * 60;
	public final static int DELAY_FIVE_HOURS = DELAY_ONE_HOUR * 5;
	public final static int DELAY_ONE_DAY = DELAY_ONE_HOUR * 24;
	public final static int DELAY_FOREVER = -1;
}
