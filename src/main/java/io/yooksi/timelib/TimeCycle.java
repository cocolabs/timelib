package io.yooksi.timelib;

public class TimeCycle {

	/**
	 * The maximum speed of the day/night cycle.
	 *
	 * This value also represents the maximum proportional length of days to vanilla.
	 * For example if we wanted the limit the days to be maximally {@code 3} times longer
	 * or shorter we would set this value to be {@code 3}.
	 */
	public static final long MAX_SPEED = 72;

	/**
	 * The speed of day/night cycle representing the proportional length of days to vanilla.
	 * <ul>
	 *     <li>Positive values speed up the cycle.</li>
	 *     <li>Negative values slow down the cycle.</li>
	 *     <li>Value of {@code 0} disables the cycle.</li>
	 * </ul>
	 * For example, setting this value to {@code 3} would make days {@code 3} times longer,
	 * while setting it a value of {@code -3} would make them {@code 3} times shorter.
	 */
	private static long speed = TimeProfile.DEFAULT_SPEED.value;

	/**
	 * Game time of last cycle update in {@code World#advanceTime()}.<br>
	 * This should be the last recorded time of updating {@code dayTime}.
	 */
	public static long lastGameTime;

	/**
	 * Sets the time cycle speed to it's default value
	 * @return the value of {@link TimeProfile#DEFAULT_SPEED}
	 */
	public static long resetSpeed() {
		return setSpeed(TimeProfile.DEFAULT_SPEED.value);
	}

	public static long setSpeed(long value) {
		return speed = value;
	}

	public static long getSpeed() {
		return speed;
	}
}
