package io.yooksi.timelib;

public class TimeCycle {

	public static final long MAX_SPEED = 100;

	public static final long SLOW_SPEED = -1;
	public static final long DEFAULT_SPEED = 1;
	public static final long FAST_SPEED = 2;

	private static long speed = DEFAULT_SPEED;

	public static long lastGameTime;

	public static long resetSpeed() {
		return setSpeed(DEFAULT_SPEED);
	}

	public static long setSpeed(long value) {
		return speed = value;
	}

	public static long getSpeed() {
		return speed;
	}
}
