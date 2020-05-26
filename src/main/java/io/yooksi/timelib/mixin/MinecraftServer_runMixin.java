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

import java.util.function.BooleanSupplier;

import io.yooksi.timelib.TickRate;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.profiler.DebugProfiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.concurrent.RecursiveEventLoop;
import net.minecraft.util.concurrent.TickDelayedTask;

@SuppressWarnings({ "unused", "FieldCanBeLocal" })
@Mixin(MinecraftServer.class)
public abstract class MinecraftServer_runMixin extends RecursiveEventLoop<TickDelayedTask> {

	@Shadow private volatile boolean serverIsRunning;
	@Shadow private volatile boolean serverRunning;

	@Shadow protected long serverTime;
	@Shadow private long timeOfLastWarning;
	@Shadow private static Logger LOGGER;
	@Shadow private boolean startProfiling;
	@Shadow private DebugProfiler profiler;
	@Shadow private boolean isRunningScheduledTasks;
	@Shadow private long runTasksUntil;

	@Shadow protected abstract void tick(BooleanSupplier booleanSupplier_1);
	@Shadow abstract boolean isAheadOfTime();
	@Shadow protected abstract void runScheduledTasks();

	private float msptAccum = 0.0f;

	public MinecraftServer_runMixin(String name) {
		super(name);
	}

	// Cancel while(this.serverRunning)
	@Redirect(method = "run", at = @At(value = "FIELD",
			target = "Lnet/minecraft/server/MinecraftServer;serverRunning:Z")
	)
	private boolean cancelRunLoop(MinecraftServer server) {
		return false;
	}
	// Replace the while loop
	@Inject(method = "run", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
			target = "Lnet/minecraft/server/MinecraftServer;applyServerIconToResponse" +
					"(Lnet/minecraft/network/ServerStatusResponse;)V")
	)
	private void modifiedRunLoop(CallbackInfo ci) {

		while (this.serverRunning)
		{
			long msThisTick, long_1;
			float tickMspt = TickRate.getMspt();

			// Tickrate changed. Ensure that we use the correct value.
			if (Math.abs(msptAccum - tickMspt) > 1.0f) {
				msptAccum = tickMspt;
			}
			msThisTick = (long) msptAccum; // regular tick
			msptAccum += tickMspt - msThisTick;
			long_1 = Util.milliTime() - this.serverTime;

			float float_1 = 1000L + 20 * TickRate.getMspt();
			float float_2 = 10000L + 100 * tickMspt;

			//smoothed out delay to include mcpt component. With 50L gives defaults.
			if (long_1 > float_1 && this.serverTime - this.timeOfLastWarning >= float_2)
			{
				long long_2 = (long) (long_1 / tickMspt);

				LOGGER.warn("Can't keep up! Is the server overloaded? " +
						"Running {}ms or {} ticks behind", long_1, long_2);

				this.serverTime += (long) (long_2 * tickMspt);
				this.timeOfLastWarning = this.serverTime;
			}
			this.serverTime += msThisTick;
			if (this.startProfiling)
			{
				this.startProfiling = false;
				this.profiler.getFixedProfiler().enable();
			}
			this.profiler.startTick();
			this.profiler.startSection("tick");
			this.tick(this::isAheadOfTime);
			this.profiler.endStartSection("nextTickWait");
			this.isRunningScheduledTasks = true;
			this.runTasksUntil = Math.max(Util.milliTime() + msThisTick, this.serverTime);
			this.runScheduledTasks();
			this.profiler.endSection();
			this.profiler.endTick();
			this.serverIsRunning = true;
		}
	}
}
