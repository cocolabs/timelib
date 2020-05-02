package io.yooksi.timelib;

public class TickRate {

	public static final float DEFAULT_RATE = 20.0f;

	private static float rate = DEFAULT_RATE;
	private static float mspt = (float)(1000.0 / rate);

	public static float changeRate(float rate) {

		TickRate.rate = rate;
		long mspt = (long) (1000.0 / TickRate.rate);
		if (mspt <= 0L) {
			mspt = 1L; TickRate.rate = 1000.0f;
		}
		TickRate.mspt = (float) mspt;
		return TickRate.rate;
	}

	public static float resetRate() {
		return changeRate(DEFAULT_RATE);
	}

	public static float getRate() {
		return rate;
	}

	public static float getMspt() {
		return mspt;
	}
}
