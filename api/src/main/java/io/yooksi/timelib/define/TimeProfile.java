package io.yooksi.timelib.define;

public enum TimeProfile {

	INERT(0),
	SLOW_SPEED(-1),
	DEFAULT_SPEED(1),
	FAST_SPEED(2);

	public final long value;

	TimeProfile(long value) {
		this.value = value;
	}
}
