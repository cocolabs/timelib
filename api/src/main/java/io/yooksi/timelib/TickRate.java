package io.yooksi.timelib;

import io.yooksi.timelib.define.TickProfile;

public class TickRate {

	/**
	 * Maximum ticks per second allowed by TimeLib.<br>
	 * <i>This is to prevent users from going crazy with numbers.</i>
	 */
	public static final float MAXIMUM = 500.0f;

	private static float rate = TickProfile.DEFAULT.rate;
	private static float mspt = (float)(1000.0 / rate);

	/**
	 * Set the game tick rate to a predefined profile value.<br>
	 * For more gradual results see {@link #set(float)}
	 */
	public static void set(TickProfile profile) {
		TickRate.set(profile.rate);
	}

	/**
	 * Set the game tick rate to a desired value between negative and positive {@link #MAXIMUM}.
	 * <p>
	 * <i>Note that setting the tick rate to too high a value might negatively impact performance
	 * and setting it too low might increase the time it takes for the chat box to open</i>
	 *
	 * @see #set(TickProfile)
	 */
	public static float set(float rate) {

		TickRate.rate = rate;
		long mspt = (long) (1000.0 / TickRate.rate);
		if (mspt <= 0L) {
			mspt = 1L; TickRate.rate = 1000.0f;
		}
		TickRate.mspt = (float) mspt;
		return TickRate.rate;
	}

	/**
	 * Sets the game tick rate value to {@link TickProfile#DEFAULT} value.
	 */
	public static float reset() {
		return set(TickProfile.DEFAULT.rate);
	}

	/**
	 * @return current game tick rate as dictated by TimeLib.
	 */
	public static float get() {
		return rate;
	}

	/**
	 * @return the duration of each game tick measured in milliseconds.
	 */
	public static float getMspt() {
		return mspt;
	}
}
