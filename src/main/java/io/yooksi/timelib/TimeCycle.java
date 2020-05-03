package io.yooksi.timelib;

public class TimeCycle {

	private static long speed = TimeProfile.DEFAULT_SPEED.value;
	public static long lastGameTime;

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
