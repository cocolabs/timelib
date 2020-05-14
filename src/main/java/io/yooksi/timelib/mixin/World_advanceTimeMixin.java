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

import io.yooksi.timelib.TimeCycle;
import io.yooksi.timelib.define.TimeProfile;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class World_advanceTimeMixin extends CapabilityProvider<World> {

	protected World_advanceTimeMixin(Class<World> baseClass) {
		super(baseClass);
	}

	@Shadow protected WorldInfo worldInfo;

	@Shadow public abstract void setDayTime(long time);

	@Inject(method = "advanceTime", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/World;setDayTime(J)V"),
			cancellable = true
	)
	private void onAdvanceTime(CallbackInfo ci) {

		final long cycleSpeed = TimeCycle.getSpeed();
		if (cycleSpeed < 0)
		{
			long currentGameTime = worldInfo.getGameTime();
			if (TimeCycle.lastGameTime + Math.abs(cycleSpeed) < currentGameTime)
			{
				this.setDayTime(this.worldInfo.getDayTime() + TimeProfile.DEFAULT_SPEED.value);
				TimeCycle.lastGameTime = worldInfo.getGameTime();
			}
		}
		else if (cycleSpeed > 0) {
			this.setDayTime(this.worldInfo.getDayTime() + cycleSpeed);
		}
		ci.cancel();
	}
}
