package io.yooksi.timelib;

public enum TickProfile {

	SLOW(10.0f), DEFAULT(20.0f), FAST(30.0f);

	public final float rate;

	TickProfile(float value) {
		this.rate = value;
	}
}
