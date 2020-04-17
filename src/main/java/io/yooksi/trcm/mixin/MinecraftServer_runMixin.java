package io.yooksi.trcm.mixin;

import net.minecraft.profiler.DebugProfiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import net.minecraft.util.concurrent.RecursiveEventLoop;
import net.minecraft.util.concurrent.TickDelayedTask;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

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

	public MinecraftServer_runMixin(String name) {
		super(name);
	}

	// Cancel while(this.serverRunning)
	@Redirect(method = "run", at = @At(value = "FIELD",
			target = "Lnet/minecraft/server/MinecraftServer;serverRunning:Z")
	)
	private boolean cancelRunLoop(MinecraftServer server)
	{
		return false;
	}
	// Replace the while loop
	@Inject(method = "run", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
			target = "Lnet/minecraft/server/MinecraftServer;applyServerIconToResponse" +
					"(Lnet/minecraft/network/ServerStatusResponse;)V")
	)
	private void modifiedRunLoop(CallbackInfo ci) {

		while(this.serverRunning) {

			long i = Util.milliTime() - this.serverTime;
			if (i > 2000L && this.serverTime - this.timeOfLastWarning >= 15000L) {
				long j = i / 50L;
				LOGGER.warn("Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind", i, j);
				this.serverTime += j * 50L;
				this.timeOfLastWarning = this.serverTime;
			}

			this.serverTime += 50L;
			if (this.startProfiling) {
				this.startProfiling = false;
				this.profiler.getFixedProfiler().enable();
			}

			this.profiler.startTick();
			this.profiler.startSection("tick");
			this.tick(this::isAheadOfTime);
			this.profiler.endStartSection("nextTickWait");
			this.isRunningScheduledTasks = true;
			this.runTasksUntil = Math.max(Util.milliTime() + 50L, this.serverTime);
			this.runScheduledTasks();
			this.profiler.endSection();
			this.profiler.endTick();
			this.serverIsRunning = true;
		}
	}
}
