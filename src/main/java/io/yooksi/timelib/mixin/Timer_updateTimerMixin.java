/*
 *  Copyright (C) 2020 Matthew Cain
 *
 *  This file is part of TimeLib.
 *
 *  TimeLib is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with TimeLib. If not, see <https://www.gnu.org/licenses/>.
 */
package io.yooksi.timelib.mixin;

import io.yooksi.timelib.TickRate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.util.Timer;

@SuppressWarnings("unused")
@Mixin(Timer.class)
public class Timer_updateTimerMixin {

	@Redirect(method = "updateTimer", at = @At(value = "FIELD",
			target = "Lnet/minecraft/util/Timer;tickLength:F")
	)
	private float updateTickSpeed(Timer timer) {
		return Math.max(50.0f, TickRate.getMspt());
	}
}
