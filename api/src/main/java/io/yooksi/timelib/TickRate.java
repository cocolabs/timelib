package io.yooksi.timelib;

import io.yooksi.timelib.define.TickProfile;

public class TickRate {

	// Hardcoded limit so users don't go crazy with values
	public static final float MAXIMUM = 500.0f;

	private static float rate = TickProfile.DEFAULT.rate;
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
		return set(TickProfile.DEFAULT.rate);
	}

	public static float get() {
		return rate;
	}

	public static float getMspt() {
		return mspt;
	}
}
