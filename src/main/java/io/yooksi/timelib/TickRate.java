package io.yooksi.timelib;

public class TickRate {


	private static float rate = DEFAULT;
	private static float mspt = (float)(1000.0 / rate);

	public static float set(float rate) {

		TickRate.rate = rate;
		long mspt = (long) (1000.0 / TickRate.rate);
		if (mspt <= 0L) {
			mspt = 1L; TickRate.rate = 1000.0f;
		}
		TickRate.mspt = (float) mspt;
		return TickRate.rate;
	}

	public static float reset() {
		return set(DEFAULT);
	}

	public static float get() {
		return rate;
	}

	public static float getMspt() {
		return mspt;
	}
}
