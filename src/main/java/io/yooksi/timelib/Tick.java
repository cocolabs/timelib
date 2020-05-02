package io.yooksi.timelib;

public class Tick {

	public static final float DEFAULT_RATE = 20.0f;

	public static boolean process_entities = true;
	private static float rate = DEFAULT_RATE;
	private static float mspt = (float)(1000.0 / rate);

	public static float changeRate(float rate) {

		Tick.rate = rate;
		long mspt = (long) (1000.0 / Tick.rate);
		if (mspt <= 0L) {
			mspt = 1L; Tick.rate = 1000.0f;
		}
		Tick.mspt = (float) mspt;
		return Tick.rate;
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
